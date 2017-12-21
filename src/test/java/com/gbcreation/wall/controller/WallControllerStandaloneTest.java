package com.gbcreation.wall.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.service.WallItem;
import com.gbcreation.wall.service.WallService;

//Approche standalone
//plus légère et proche du test unitaire

public class WallControllerStandaloneTest {

	@InjectMocks
	WallController controller;
	
	private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .build();
	
  private  List<Item> items;
  @Mock
  private WallService wallService;
	  
	  @Before
	    public void setup() throws Exception {
		  MockitoAnnotations.initMocks(this);
		  
		  items = new ArrayList<Item>();
		  items.add(new Item("picture1.jpg","/some/local/folder/","First Picture"));
		  items.add(new Item("picture2.jpg","/some/local/folder/","Second Picture"));
		  items.add(new Item("codevideo1","http://youtube.com/some/path/", "Demo video 1"));
		  items.add(new Item("picture3","/some/local/folder/",  "Third Picture"));
		  items.add(new Item("codevideo2","http://youtube.com/some/path/", "Demo video 2"));
		  items.add(new Item("codevideo3","http://youtube.com/some/path/", "Demo video 3"));
		  items.add(new Item("picture4.jpg","/some/local/folder/", "Fourth Picture"));
		  items.add(new Item("picture5.jpg","/some/local/folder/","Fifth Picture"));
		  items.add(new Item("picture6.jpg","/some/local/folder/", "Sixth Picture"));
		  items.add(new Item("codevideo4","http://youtube.com/some/path/", "Demo video 4"));
		  items.add(new Item("picture7.jpg","/some/local/folder/", "Seventh Picture"));
		  items.add(new Item("codevideo5","http://youtube.com/some/path/", "Demo video 5"));
		  
		  when(wallService.retrieveAllItems()).thenReturn(items);
	    }

	    @Test
	    public void test_retrieve_all_items_OK() throws Exception {
	   
	        mockMvc.perform(get("/api/wall/all"))
	        		.andDo(print())
	        		//MediaType.APPLICATION_JSON_VALUE
	        		.andExpect(content().contentType("application/json;charset=UTF-8"))
	        		.andExpect(status().isOk())
	        		;
	    }
	
}
