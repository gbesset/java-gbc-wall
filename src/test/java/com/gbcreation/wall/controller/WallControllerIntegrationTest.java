package com.gbcreation.wall.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
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
		.andExpect(jsonPath("$.count-all").value(28))
		.andExpect(jsonPath("$.count-pictures").value(26))
		.andExpect(jsonPath("$.count-videos").value(2))
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
		.andExpect(jsonPath("$[0].file").value("!!!!!"))
		.andExpect(jsonPath("$[27].file").value("!!!!!!!"))
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
	        	    .andExpect(jsonPath("$[0].file").value("!!!!!!.jpg"))
	        	    .andExpect(jsonPath("$[25].file").value("!!!!!!.jpg"))
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
	        	    .andExpect(jsonPath("$[0].file").value("!!!!!"))
	        	    .andExpect(jsonPath("$[1].file").value("!!!!!"))
	        		;
	    }
	 @Test
	    public void test_search_title() throws Exception {
	    		
	        mockMvc.perform(get(PATH+"/search/title/!!!!!"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(12)))
	        		.andExpect(jsonPath("$[0].file").value("!!!!!!!.jpg"))
	        		.andExpect(jsonPath("$[11].file").value("!!!!!!!"))
	        		;
	    }
	 @Test
	    public void test_search_description() throws Exception {
 		
	        mockMvc.perform(get(PATH+"/search/description/!!!!!"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(12)))
	        		.andExpect(jsonPath("$[0].file").value("!!!!!!.jpg"))
	        		.andExpect(jsonPath("$[11].file").value("!!!!!"))
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
}
