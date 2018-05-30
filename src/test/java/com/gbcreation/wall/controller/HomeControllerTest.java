package com.gbcreation.wall.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	private static String PATH = "/home";

	
	@Test
	public void retrieveCarousel() throws Exception {
    		mockMvc.perform(get(PATH+"/carousel"))
    		.andDo(print())
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.path").value("assets/images/carousel/demo/"))
    		.andExpect(jsonPath("$.imgs[*]").isArray())
    		.andExpect(jsonPath("$.imgs[*]", hasSize(3)))
    		.andExpect(jsonPath("$.imgs[0]").value("header0.jpg"))
    		.andExpect(jsonPath("$.imgs[2]").value("header2.jpg"))
    		;
    		
  }
}
