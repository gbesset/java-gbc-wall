package com.gbcreation.wall.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public interface StorageService {

	Map<String,String> store(MultipartFile file, String path);
	
	boolean delete(String path, String filename);
	
	void rotateLeft(String path, String fileName) throws IOException;
    
    void rotateRight(String path, String fileName) throws IOException;
	
    Resource loadAsResource(String path, String filename);
    
    
    
    
    Stream<Path> loadAll( String path);

   
    
    
    void init();

    void deleteAll();
    
    

}
