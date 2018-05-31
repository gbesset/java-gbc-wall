package com.gbcreation.wall.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gbcreation.wall.controller.exception.StorageException;
import com.gbcreation.wall.controller.exception.StorageFileNotFoundException;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class FileSystemStorageService implements StorageService{
	
	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(@Value("${images.folder}") String rootPath) {
		this.rootLocation = Paths.get(rootPath);
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
				Path dest =  Paths.get(this.rootLocation.toString()+path);
				if(!Files.exists(dest)) {
					log.info("Directory {} doesn't exist. Need to create {}", dest, path);
					Files.createDirectory(dest);
					log.info("Directory {} Created !!", dest);;
				}
				Files.copy(inputStream,dest.resolve(fileNewName), StandardCopyOption.REPLACE_EXISTING);
				log.info("FileSystemStorageService :  copy file {} DONE", fileNewName);
				
				Map<String,String> response = new HashMap<>();
				response.put("file",fileNewName);
				response.put("createdAt",""+createdAt.getTime());
				return response;
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
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
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			log.debug("FileSystemStorageService : init before creting repo {}",rootLocation);
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
