package com.gbcreation.wall.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbcreation.wall.WallApplication;
import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = WallApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integrationTest")
public class WallControllerIntegrationTest {

	 @Autowired
	 private MockMvc mockMvc;
	 
	 @Autowired
	 private WallController controller;
	 
	private static String PATH = "/api/wall";
	
	@Test
	public void contex_loads() throws Exception {
		assertThat(controller).isNotNull();
	}
	
	@Test
	public void test_count() throws Exception {
	
		mockMvc.perform(get(PATH + "/count"))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.all").value(28))
		.andExpect(jsonPath("$.pictures").value(26))
		.andExpect(jsonPath("$.videos").value(2))
		.andExpect(jsonPath("$.comments").value(4))
		;
	}
	
	@Test
	public void test_retrieve_all_items_OK() throws Exception {

		mockMvc.perform(get(PATH + "/items?size=30"))
			.andDo(print())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.totalElements").value(28))
			.andExpect(jsonPath("$.content[*]").isArray())
			.andExpect(jsonPath("$.content[*]", hasSize(28)))
			.andExpect(jsonPath("$.content[0].file").value("bender-futurama-robot-p-753df5.jpg"))
			.andExpect(jsonPath("$.content[27].file").value("big3-1cd860.gif"))
		;
	}
	 
	 @Test
	    public void test_retrieve_all_pictures_OK() throws Exception {
	    	
	        mockMvc.perform(get(PATH+"/pictures?size=30"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(26))
	        		.andExpect(jsonPath("$.content[*]").isArray())
	        	    .andExpect(jsonPath("$.content[*]", hasSize(26)))
	        		.andExpect(jsonPath("$.content[0].file").value("bender-futurama-robot-p-753df5.jpg"))
	        		.andExpect(jsonPath("$.content[25].file").value("big3-1cd860.gif"))
	        		;
	    }
	 
	 @Test
	    public void test_retrieve_all_videos_OK() throws Exception {
	        mockMvc.perform(get(PATH+"/videos"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(2))
	        		.andExpect(jsonPath("$.content[*]").isArray())
	        	    .andExpect(jsonPath("$.content[*]", hasSize(2)))
	        	    .andExpect(jsonPath("$.content[0].file").value("http://player.vimeo.com/video/12915013"))
	        	    .andExpect(jsonPath("$.content[1].file").value("http://www.youtube.com/embed/GLpO-OvJU74"))
	        		;
	    }
	 @Test
	    public void test_search_item_title() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/item/title/ipod"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.content[*]").isArray())
	        		.andExpect(jsonPath("$.content[*]", hasSize(1)))
	        		.andExpect(jsonPath("$.content[0].file").value("Futurama-iPod-1024x768-05f116.jpg"))
	        		;
	        
	        mockMvc.perform(get(PATH+"/search/item/title/IPOD"))
		    		.andDo(print())
		    		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		    		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		    		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(1))
		    		.andExpect(jsonPath("$.content[*]").isArray())
		    		.andExpect(jsonPath("$.content[*]", hasSize(1)))
		    		.andExpect(jsonPath("$.content[0].file").value("Futurama-iPod-1024x768-05f116.jpg"))
		    		;
	        
        		mockMvc.perform(get(PATH+"/search/item/title/simpsons"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(2))
	        		.andExpect(jsonPath("$.content[*]").isArray())
	        		.andExpect(jsonPath("$.content[*]", hasSize(2)))
	        		.andExpect(jsonPath("$.content[0].file").value("SIMPSONS-FUTURAMA-612x381-d8f865.jpg"))
	        		.andExpect(jsonPath("$.content[1].file").value("the-simpsons-futurama-crossover-9de538.jpg"))
	        		;
	        		
        		mockMvc.perform(get(PATH+"/search/item/title/bender"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(5))
	        		.andExpect(jsonPath("$.content[*]").isArray())
	        		.andExpect(jsonPath("$.content[*]", hasSize(5)))
	        		.andExpect(jsonPath("$.content[0].file").value("bender-futurama-robot-p-753df5.jpg"))
	        		.andExpect(jsonPath("$.content[1].file").value("air-bender-617cca.jpg"))
	        		.andExpect(jsonPath("$.content[2].file").value("bender-futurama-poker-4d8463.jpg"))
	        		.andExpect(jsonPath("$.content[3].file").value("futurama-bender2-b0b52e.jpg"))
	        		.andExpect(jsonPath("$.content[4].file").value("254638-bender-fry-leela-futurama-futurama-p-6fb68e.jpg"))
	        		;
	    }
	 @Test
	    public void test_search_item_title_noResults() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/item/title/hdsf12ff"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(0))
	        		.andExpect(jsonPath("$.content[*]", hasSize(0)))
	        		;
	 }
	 @Test
	    public void test_search_item_description() throws Exception {
 		
	        mockMvc.perform(get(PATH+"/search/item/description/bender"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(4))
	        		.andExpect(jsonPath("$.content[*]").isArray())
	        		.andExpect(jsonPath("$.content[*]", hasSize(4)))
	        		.andExpect(jsonPath("$.content[0].description").value("bender"))
	        		.andExpect(jsonPath("$.content[1].description").value("Air bender"))
	        		.andExpect(jsonPath("$.content[2].description").value("Bender switch off"))
	        		.andExpect(jsonPath("$.content[3].description").value("bender cartoon"))
	        		;
	       
	        
	        mockMvc.perform(get(PATH+"/search/item/description/ipOD"))
		        .andDo(print())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
        			.andExpect(jsonPath("$.totalElements").value(1))
		        .andExpect(jsonPath("$.content[*]").isArray())
		        .andExpect(jsonPath("$.content[*]", hasSize(1)))
		        .andExpect(jsonPath("$.content[0].description").value("Ipod, style"))
	        ;
	        
	        mockMvc.perform(get(PATH+"/search/item/description/aFFicHe"))
		        .andDo(print())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
        			.andExpect(jsonPath("$.totalElements").value(2))
		        .andExpect(jsonPath("$.content[*]").isArray())
		        .andExpect(jsonPath("$.content[*]", hasSize(2)))
		        .andExpect(jsonPath("$.content[0].description").value("Encore une affiche"))
		        .andExpect(jsonPath("$.content[1].description").value("Affiche"))
	        ;
	    }

	 	@Test
	    public void test_search_item_description_noResults() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/item/description/afterhoa"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(0))
	        		.andExpect(jsonPath("$.content[*]", hasSize(0)))
	        		;
	    }
	 	
	 	 @Test
	    public void test_search_item_comments() throws Exception {
 		
	        mockMvc.perform(get(PATH+"/search/item/comment/prEFeR?page=0"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
        			.andExpect(jsonPath("$.totalElements").value(1))
    		        .andExpect(jsonPath("$.content[*]").isArray())
    		        .andExpect(jsonPath("$.content[*]", hasSize(1)))
    		        .andExpect(jsonPath("$.content[0].file").value("bender-futurama-robot-p-753df5.jpg"))
    		        ;
	        			    }
	 	 
	 	@Test
	    public void test_search_item_comments_noResults() throws Exception {
 		
	        mockMvc.perform(get(PATH+"/search/item/comment/dafgsfdfrg"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(0))
	        		.andExpect(jsonPath("$.content[*]", hasSize(0)))
	        		;
	 	}
	 	
	 	 @Test
		    public void test_search_item_author() throws Exception {
	 		
		        mockMvc.perform(get(PATH+"/search/item/author/faTHer?page=0"))
		        		.andDo(print())
		        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		        		.andExpect(status().isOk())
	        			.andExpect(jsonPath("$.totalElements").value(2))
	    		        .andExpect(jsonPath("$.content[*]").isArray())
	    		        .andExpect(jsonPath("$.content[*]", hasSize(2)))
	    		        .andExpect(jsonPath("$.content[0].file").value("big3-1cd860.gif"))
	    		        .andExpect(jsonPath("$.content[1].file").value("bender-futurama-robot-p-753df5.jpg"))
	    		        ;
		    }
		 	 
		 	@Test
		    public void test_search_item_author_noResults() throws Exception {
	 		
		        mockMvc.perform(get(PATH+"/search/item/comment/dafgsfdfrg"))
		        		.andDo(print())
		        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		        		.andExpect(status().isOk())
		        		.andExpect(jsonPath("$.totalElements").value(0))
		        		.andExpect(jsonPath("$.content[*]", hasSize(0)))
		        		;
		 	}
	 	
	 	@Test
	    public void test_search_comment_comments() throws Exception {
    		
	        mockMvc.perform(get(PATH+"/search/comment/comment/picture"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(3))
	        		.andExpect(jsonPath("$.content[*]").isArray())
	        		.andExpect(jsonPath("$.content[*]", hasSize(3)))
	        		.andExpect(jsonPath("$.content[0].comment").value("Where was the picture taken?"))
	        		.andExpect(jsonPath("$.content[2].comment").value("Great picture"))
	        		;
	        
	        mockMvc.perform(get(PATH+"/search/comment/comment/this picture"))
		    		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		    		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		    		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(1))
		    		.andExpect(jsonPath("$.content[*]").isArray())
		    		.andExpect(jsonPath("$.content[*]", hasSize(1)))
		    		.andExpect(jsonPath("$.content[0].comment").value("I prefer this picture"))
		    		;
	        
	    }
	    
	    @Test
	    public void test_search_comment_comments_noResults() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/comment/comment/another one"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(0))
	        		.andExpect(jsonPath("$.content[*]", hasSize(0)))
	        		;
	        
	    }
	    
	    @Test
	    public void test_search_comment_authors() throws Exception {
    		
	        mockMvc.perform(get(PATH+"/search/comment/author/ther"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andDo(print())
	        		.andExpect(jsonPath("$.totalElements").value(3))
	        		.andExpect(jsonPath("$.content[*]").isArray())
	        		.andExpect(jsonPath("$.content[*]", hasSize(3)))
	        		.andExpect(jsonPath("$.content[0].author").value("father"))
	        		.andExpect(jsonPath("$.content[1].author").value("father"))
	        		.andExpect(jsonPath("$.content[2].author").value("brother"))
	        		;
	        
	    }
	    
	    @Test
	    public void test_search_comment_author_noResults() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/comment/author/another one"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.totalElements").value(0))
	        		.andExpect(jsonPath("$.content[*]", hasSize(0)))
	        		;
	    }
	    	    
	   /* @Test
	    public void test_search_item_from_comment() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/item/comment/2"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$.id").value(230))
	        		.andExpect(jsonPath("$.file").value("bender-futurama-robot-p-753df5.jpg"))
	        		;
	    }
	    
	    @Test
	    public void test_search_item_from_comment_noResults() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/item/comment/12"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().is(400))
	        		;
	    }*/
}
