package com.gbcreation.wall.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.gbcreation.wall.WallApplication;


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
		.andExpect(jsonPath("$.count-all").value(28))
		.andExpect(jsonPath("$.count-pictures").value(26))
		.andExpect(jsonPath("$.count-videos").value(2))
		.andExpect(jsonPath("$.count-comments").value(4))
		;
	}
	
	@Test
	public void test_retrieve_all_items_OK() throws Exception {

		mockMvc.perform(get(PATH + "/all"))
			.andDo(print())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[*]").isArray())
			.andExpect(jsonPath("$[*]", hasSize(28)))
			.andExpect(jsonPath("$[0].file").value("bender-futurama-robot-p-753df5.jpg"))
			.andExpect(jsonPath("$[27].file").value("big3-1cd860.gif"))
		;
	}
	 
	 @Test
	    public void test_retrieve_all_pictures_OK() throws Exception {
	    	
	        mockMvc.perform(get(PATH+"/pictures"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        	    .andExpect(jsonPath("$[*]", hasSize(26)))
	        		.andExpect(jsonPath("$[0].file").value("bender-futurama-robot-p-753df5.jpg"))
	        		.andExpect(jsonPath("$[25].file").value("big3-1cd860.gif"))
	        		;
	    }
	 
	 @Test
	    public void test_retrieve_all_videos_OK() throws Exception {
	        mockMvc.perform(get(PATH+"/videos"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        	    .andExpect(jsonPath("$[*]", hasSize(2)))
	        	    .andExpect(jsonPath("$[0].file").value("http://player.vimeo.com/video/12915013"))
	        	    .andExpect(jsonPath("$[1].file").value("http://www.youtube.com/embed/GLpO-OvJU74"))
	        		;
	    }
	 @Test
	    public void test_search_title() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/title/ipod"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(1)))
	        		.andExpect(jsonPath("$[0].file").value("Futurama-iPod-1024x768-05f116.jpg"))
	        		;
	        
	        mockMvc.perform(get(PATH+"/search/title/IPOD"))
		    		.andDo(print())
		    		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		    		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		    		.andExpect(status().isOk())
		    		.andExpect(jsonPath("$[*]").isArray())
		    		.andExpect(jsonPath("$[*]", hasSize(1)))
		    		.andExpect(jsonPath("$[0].file").value("Futurama-iPod-1024x768-05f116.jpg"))
		    		;
	        
        		mockMvc.perform(get(PATH+"/search/title/simpsons"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(2)))
	        		.andExpect(jsonPath("$[0].file").value("SIMPSONS-FUTURAMA-612x381-d8f865.jpg"))
	        		.andExpect(jsonPath("$[1].file").value("the-simpsons-futurama-crossover-9de538.jpg"))
	        		;
	        		
        		mockMvc.perform(get(PATH+"/search/title/bender"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(5)))
	        		.andExpect(jsonPath("$[0].file").value("bender-futurama-robot-p-753df5.jpg"))
	        		.andExpect(jsonPath("$[1].file").value("air-bender-617cca.jpg"))
	        		.andExpect(jsonPath("$[2].file").value("bender-futurama-poker-4d8463.jpg"))
	        		.andExpect(jsonPath("$[3].file").value("futurama-bender2-b0b52e.jpg"))
	        		.andExpect(jsonPath("$[4].file").value("254638-bender-fry-leela-futurama-futurama-p-6fb68e.jpg"))
	        		;
	    }
	 @Test
	    public void test_search_title_noResults() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/title/hdsf12ff"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	 }
	 @Test
	    public void test_search_description() throws Exception {
 		
	        mockMvc.perform(get(PATH+"/search/description/bender"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(4)))
	        		.andExpect(jsonPath("$[0].description").value("bender"))
	        		.andExpect(jsonPath("$[1].description").value("Air bender"))
	        		.andExpect(jsonPath("$[2].description").value("Bender switch off"))
	        		.andExpect(jsonPath("$[3].description").value("bender cartoon"))
	        		;
	        
	        mockMvc.perform(get(PATH+"/search/description/bender"))
		        .andDo(print())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$[*]").isArray())
		        .andExpect(jsonPath("$[*]", hasSize(4)))
		        .andExpect(jsonPath("$[0].description").value("bender"))
		        .andExpect(jsonPath("$[1].description").value("Air bender"))
		        .andExpect(jsonPath("$[2].description").value("Bender switch off"))
		        .andExpect(jsonPath("$[3].description").value("bender cartoon"))
		        ;
	        
	        mockMvc.perform(get(PATH+"/search/description/ipod"))
		        .andDo(print())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$[*]").isArray())
		        .andExpect(jsonPath("$[*]", hasSize(1)))
		        .andExpect(jsonPath("$[0].description").value("Ipod, style"))
	        ;
	        
	        mockMvc.perform(get(PATH+"/search/description/affiche"))
		        .andDo(print())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$[*]").isArray())
		        .andExpect(jsonPath("$[*]", hasSize(2)))
		        .andExpect(jsonPath("$[0].description").value("Encore une affiche"))
		        .andExpect(jsonPath("$[1].description").value("Affiche"))
	        ;
	    }

	 	@Test
	    public void test_search_description_noResults() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/description/a@&fg!fterh"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	    }
	 	
	 	
	 	@Test
	    public void test_search_comments() throws Exception {
    		
	        mockMvc.perform(get(PATH+"/search/comment/picture"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(3)))
	        		.andExpect(jsonPath("$[0].comment").value("Where was the picture taken?"))
	        		.andExpect(jsonPath("$[2].comment").value("Great picture"))
	        		;
	        
	        mockMvc.perform(get(PATH+"/search/comment/this picture"))
		    		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		    		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		    		.andExpect(status().isOk())
		    		.andExpect(jsonPath("$[*]").isArray())
		    		.andExpect(jsonPath("$[*]", hasSize(1)))
		    		.andExpect(jsonPath("$[0].comment").value("I prefer this picture"))
		    		;
	        
	    }
	    
	    @Test
	    public void test_search_comments_noResults() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/comment/another one"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	    }
	    
	    @Test
	    public void test_search_authors() throws Exception {
    		
	        mockMvc.perform(get(PATH+"/search/author/ther"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andDo(print())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(3)))
	        		.andExpect(jsonPath("$[0].author").value("father"))
	        		.andExpect(jsonPath("$[1].author").value("father"))
	        		.andExpect(jsonPath("$[2].author").value("brother"))
	        		;
	        
	    }
	    
	    @Test
	    public void test_search_author_noResults() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/author/another one"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	    }
}
