package com.gbcreation.wall.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.service.ItemService;
import com.gbcreation.wall.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/resources")
@CrossOrigin(origins = {"${settings.cors_origin}"})
public class ResourceController {
	
	private	StorageService storageService;
  
	@Autowired
	 public ResourceController(StorageService storageService) {
	        this.storageService = storageService;
	    }
	
	@Autowired 
	ItemService itemService;
	 
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
 
	
	@GetMapping("/delete")
	public ResponseEntity<Map<String,String>>deleteFile(@RequestParam(value="id") Long id) {
		Map<String,String> response = new HashMap<>();
		
		if (id == null) {
			response.put("msg", "Please select a file!");
            return new ResponseEntity(response, HttpStatus.OK);
        }

		try {
			Item item = itemService.findById(id);
			
			boolean retour = storageService.delete(item.getPath(), item.getFile());
			 		
			if(retour) {
				response.put("msg", "File: " + item.getFile() + " deleted !");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			else {
				response.put("msg", "Error while deleting of File: " + item.getFile() + "  !");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			
		} catch (StorageException se) {
			response.put("msg", "FAIL to delete file from item id " + id + "!. " + se.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		}catch (Exception e) {
			response.put("msg", "FAIL to delete file from item id " + id+ "!");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		}
	}
 
	
	@GetMapping("/rotate")
	public ResponseEntity<Map<String,String>> rotateFile(@RequestParam(value="id") Long id, @RequestParam(value="angle") String angle) {
		Map<String,String> response = new HashMap<>();
		
		if (id == null) {
			response.put("msg", "Please select a file!");
            return new ResponseEntity(response, HttpStatus.OK);
        }

		try {
			Item item = itemService.findById(id);
			
			if(item != null && "LEFT".equalsIgnoreCase(angle)) {
				storageService.rotateLeft(item.getPath(), item.getFile());
			}
			else if (item != null && "RIGHT".equalsIgnoreCase(angle)) {
				storageService.rotateRight(item.getPath(), item.getFile());
			}
			else {
				response.put("msg", "Item not found with id "+id+" or Angle not defined: " + angle + " [Right/left]");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			 		
			response.put("msg", "File: " + item.getFile() + " rotated !");
			return ResponseEntity.status(HttpStatus.OK).body(response);
			
		} catch (StorageException se) {
			response.put("msg", "FAIL to rotate file from item id " + id + "!. " + se.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		}catch (Exception e) {
			response.put("msg", "FAIL to rotate file from item id " + id+ "!");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		}
	}
 
	@GetMapping("/get/{path}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String path, @RequestParam String fileName) {
		log.info("retrieve picture in path {} named {}", "/"+path, fileName);
		Resource file = storageService.loadAsResource("/"+path,fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
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
	public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
		//TODO    NE PEUTMARCHER... a virer ?
		//a quoi ca sert a partir du file name?
		Resource file = storageService.loadAsResource("", fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
