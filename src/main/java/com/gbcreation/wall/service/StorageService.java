package com.gbcreation.wall.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	Map<String,String> store(MultipartFile file, String path);

    Stream<Path> loadAll( String path);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
