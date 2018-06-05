package com.gbcreation.wall.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gbcreation.wall.controller.exception.StorageFileNotFoundException;
import com.gbcreation.wall.service.StorageService;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ResourceControllerTest {
	
	private static String PATH = "/api/resources";
	
	
	 @Autowired
	    private MockMvc mvc;

	    @MockBean
	    private StorageService storageService;

	   /* @Test
	    public void shouldListAllFiles() throws Exception {
	        given(this.storageService.loadAll(any()))
	                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

	        this.mvc.perform(get(PATH+"/all"))
        			.andExpect(status().isOk())
        			.andExpect(jsonPath("$[*]").isArray())
        			.andExpect(jsonPath("$[*]", hasSize(2)))
        			.andExpect(jsonPath("$[0]").value("http://localhost/api/resources/first.txt"))
        			.andExpect(jsonPath("$[1]").value("http://localhost/api/resources/second.txt"))
	        ;
	        
	        then(this.storageService).should().loadAll("/"+LocalDate.now().getYear());
	    }*/
	    
	   /* @Test
	    public void shouldListAllFilesWithPath() throws Exception {
	        given(this.storageService.loadAll(any()))
	                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));
	        this.mvc.perform(get(PATH+"/all?path=/TEST"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$[*]").isArray())
				.andExpect(jsonPath("$[*]", hasSize(2)))
				.andExpect(jsonPath("$[0]").value("http://localhost/api/resources/first.txt"))
				.andExpect(jsonPath("$[1]").value("http://localhost/api/resources/second.txt"))
			;
		
	        then(this.storageService).should().loadAll("/TEST");
	    }*/
	    
	    @Test
	    public void shouldSaveUploadedFile() throws Exception {
	        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt","text/plain", "Spring Framework".getBytes());
	        
	        Map<String, String> response1 = new HashMap<>();
	        response1.put("file","file-renamed.jpg");
	        	response1.put("createdAt", ""+ Date.from(Instant.parse("2018-05-31T12:17:47.720Z")));
	        	when(storageService.store(any(), any())).thenReturn(response1);
	        
	        
	        this.mvc.perform(fileUpload(PATH+"/post").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("File: test.txt uploaded !"))
                .andExpect(jsonPath("$.file").isString())
                .andExpect(jsonPath("$.file").value("file-renamed.jpg"))
                .andExpect(jsonPath("$.createdAt").isString())
                .andExpect(jsonPath("$.createdAt").value(""+Date.from(Instant.parse("2018-05-31T12:17:47.720Z"))))
	        ;

	        then(this.storageService).should().store(multipartFile,"/"+LocalDate.now().getYear());
	        
	        this.mvc.perform(fileUpload(PATH+"/post?path=/TEST").file(multipartFile))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.msg").value("File: test.txt uploaded !"))
	            .andExpect(jsonPath("$.file").value("file-renamed.jpg"))
	            .andExpect(jsonPath("$.createdAt").value(""+Date.from(Instant.parse("2018-05-31T12:17:47.720Z"))))
	            ;

	        then(this.storageService).should().store(multipartFile,"/TEST");
	    }

	   /* 
	    * pb test.txt   le point n'est pas pris? 
	    * 
	    * @SuppressWarnings("unchecked")
	    @Test
	    public void should404WhenMissingFile() throws Exception {
	        given(this.storageService.loadAsResource("", "test.txt")).willThrow(StorageFileNotFoundException.class);

	        this.mvc.perform(get(PATH+"/test.txt"))
	        .andExpect(status().isNotFound());
	    }*/

}
