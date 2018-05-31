package com.gbcreation.wall.controller;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.gbcreation.wall.controller.exception.StorageException;
import com.gbcreation.wall.controller.exception.StorageFileNotFoundException;
import com.gbcreation.wall.service.StorageService;
import com.gbcreation.wall.service.StorageServiceEx1;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/resources")
@CrossOrigin(origins = {"http://localhost:4200", "http://k.g.gbcreation.fr"})
public class ResourceController {
	
	private	StorageService storageService;
  
	@Autowired
	 public ResourceController(StorageService storageService) {
	        this.storageService = storageService;
	    }
	 
	@PostMapping("/post")
	public ResponseEntity<Map<String,String>>handleFileUpload(@RequestParam("file") MultipartFile file,
																@RequestParam(value="path", required = false) String path) {
		Map<String,String> response = new HashMap<>();
		
		if (file.isEmpty()) {
			response.put("msg", "Please select a file!");
            return new ResponseEntity(response, HttpStatus.OK);
        }

		try {
			if(path == null) {
				path = "/"+LocalDate.now().getYear();
				log.info("Path non fourni. On défini :"+path);
			}
						
			//Create path folder if deosnt exist, rename file and store it
			response = storageService.store(file, path);
			 		
			response.put("msg", "File: " + file.getOriginalFilename() + " uploaded !");
			return ResponseEntity.status(HttpStatus.OK).body(response);
			
		} catch (StorageException se) {
			response.put("msg", "FAIL to store file " + file.getOriginalFilename() + "!. " + se.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		}catch (Exception e) {
			response.put("msg", "FAIL to upload " + file.getOriginalFilename() + "!");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		}
	}
 
	

	@GetMapping("/all")
	public ResponseEntity<List<String>> listUploadedFiles(Model model,@RequestParam(value="path", required = false) String path) throws IOException {
		
		if(path == null) {
			path = "/"+LocalDate.now().getYear();
			log.info("Path non fourni. On défini :"+path);
		}
		
		List<String> fileNames = storageService.loadAll(path).map(pathFile -> MvcUriComponentsBuilder.fromMethodName(
					ResourceController.class,"serveFile", pathFile.getFileName().toString()).build().toString()
					)
					.collect(Collectors.toList());
				

		//????? https://spring.io/guides/gs/uploading-files/
		//model.addAttribute("files", fileNames);
				 
		return ResponseEntity.ok().body(fileNames);
	}
 
	@GetMapping("/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
