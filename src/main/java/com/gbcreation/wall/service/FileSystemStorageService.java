package com.gbcreation.wall.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gbcreation.wall.controller.exception.StorageException;
import com.gbcreation.wall.controller.exception.StorageFileNotFoundException;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class FileSystemStorageService implements StorageService{
	
	private final Path rootLocation;
	
	private final int maxSize;
	
	private final String tempPath = "/temp";

	@Resource
	ImageResizer imageResizer;
		
	@Autowired
	public FileSystemStorageService(@Value("${images.folder}") String rootPath, @Value("${images.maxSize}") int maxSize) {
		this.rootLocation = Paths.get(rootPath);
		this.maxSize = new Integer(maxSize);
	}

	@Override
	public Map<String,String> store(MultipartFile file, String path) {

		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		log.info("FileSystemStorageService : store file before clean:[{}],  After clean: [{}] ",file.getOriginalFilename(),filename);

		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException("Cannot store file with relative path outside current directory " + filename);
			}
			
			Date createdAt = retrieveOrDefineDate(filename);
			int hash = createdAt.hashCode();
			
			String extension = filename.substring(filename.lastIndexOf('.'), filename.length());
			String fileNewName = filename.substring(0, file.getOriginalFilename().lastIndexOf('.')) + "-" + hash + extension;
			
			try (InputStream inputStream = file.getInputStream()) {
				//Copy file into temp folder with fileName !! (to replace if multiple upload))
				Path tempFolder =  Paths.get(this.rootLocation.toString()+tempPath);
				if(!Files.exists(tempFolder)) {
					log.info("Directory {} doesn't exist. Need to create {}", tempFolder, tempPath);
					Files.createDirectory(tempFolder);
					log.info("Directory {} Created !!", tempPath);;
				}
				Files.copy(inputStream,tempFolder.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
				log.info("FileSystemStorageService :  copy file {} DONE", filename);
				
				//Prepare Dest folder
				Path dest =  Paths.get(this.rootLocation.toString()+path);
				if(!Files.exists(dest)) {
					log.info("Directory {} doesn't exist. Need to create {}", dest, path);
					Files.createDirectory(dest);
					log.info("Directory {} Created !!", dest);;
				}
				
				BufferedImage bufferedImage = (BufferedImage)ImageIO.read(file.getInputStream());
				double percent = calculatePercentage(bufferedImage);
				//No resize needed
				if(percent == 1.0) {
					Files.copy(file.getInputStream(), dest.resolve(fileNewName), StandardCopyOption.REPLACE_EXISTING);
					log.info("FileSystemStorageService :  copy file {} DONE", fileNewName);
				}
				else {
					log.info("Pourcentage calculé: {} pour tomber sur resolution max {}",percent, maxSize);
					//resize file and store it into dest folder
					imageResizer.resize(bufferedImage, dest.resolve(fileNewName), percent);
				}
					
				//Suppression image dans /temp
				log.debug("Delete file in  /temp {}",tempFolder.resolve(filename).toString());
				tempFolder.resolve(filename).toFile().delete();
				log.debug("Delete file in /temp DONE");
			
				
				Map<String,String> response = new HashMap<>();
				response.put("file",fileNewName);
				response.put("createdAt",""+createdAt.getTime());
				return response;
			}
			
		}
		catch (Exception e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	private double calculatePercentage(BufferedImage bufferedImage) throws IOException {

		int max = Math.max(bufferedImage.getWidth(), bufferedImage.getHeight());

		if(max <= maxSize) {
			return 1.0;
		}
		else{
			double percent = (100 * maxSize / max);
			return percent / 100;
		}
	}

	private Date retrieveOrDefineDate(String originalFilename) {
		
		//TODO
		//REGEX pattern pour trouver date dans fichier (tel notament) ou autre...autre..
		//SI ok on set sinon non
		//(faire un new Date a partir de ce que trouvé et catché exception si ok => ok sinon new Date))
		//LocalDate localDate = LocalDate.of(2013, 05, 26);
		//http://blog.soat.fr/2013/06/java-8-whats-new-23-date-and-time/
		
		
		//https://openclassrooms.com/courses/apprenez-a-programmer-en-java/la-nouvelle-api-de-gestion-des-dates-de-java-8
		//faire unLocalDateTime plutot?!
		
		return new Date();
	}

	@Override
	public boolean delete(String path, String fileName) {
		Path folder =  Paths.get(this.rootLocation.toString()+path);
		
		return folder.resolve(fileName).toFile().delete();
	}
	
	@Override
	public void rotateLeft(String path, String fileName) throws IOException {
		Path folder =  Paths.get(this.rootLocation.toString()+path);
		BufferedImage image = ImageIO.read(folder.resolve(fileName).toFile());
		
		BufferedImage imageRotated = imageResizer.rotate(image, Math.toRadians(-90));
		String formatName = fileName.substring(fileName
                .lastIndexOf(".") + 1);
		
		ImageIO.write(imageRotated, formatName, folder.resolve(fileName).toFile());
		
	}

	@Override
	public void rotateRight(String path, String fileName) throws IOException {
		Path folder =  Paths.get(this.rootLocation.toString()+path);
		BufferedImage image = ImageIO.read(folder.resolve(fileName).toFile());
		
		BufferedImage imageRotated = imageResizer.rotate(image, Math.toRadians(90));
		
		String formatName = fileName.substring(fileName
                .lastIndexOf(".") + 1);
		
		ImageIO.write(imageRotated, formatName, folder.resolve(fileName).toFile());
	}
	
	@Override
	public Stream<Path> loadAll(String path) {
		try {
			Path directory =  Paths.get(this.rootLocation.toString()+path);
			
			return Files.walk(directory, 1)
					.filter(localPath -> !localPath.equals(directory))
					.map(directory::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public org.springframework.core.io.Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			org.springframework.core.io.Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		//FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		/*try {
			log.debug("FileSystemStorageService : init before creting repo {}",rootLocation);
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}*/
	}

}
