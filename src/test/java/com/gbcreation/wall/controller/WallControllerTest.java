package com.gbcreation.wall.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.service.WallItem;
import com.gbcreation.wall.service.WallService;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WallControllerTest {
	 
	 @Autowired
	 private WallController controller;
	 
	 @Autowired
	 private MockMvc mockMvc;
	
	 @Mock
	 private WallService wallServiceMock;
	  
	 private  List<Item> items;
	  
	@Before
	public void setup() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  
	 items=createSomeItems();
	 
	 when(wallServiceMock.retrieveAllItems()).thenReturn(items);
	 controller.setWallService(wallServiceMock);
	}

//TODO est-ce vraiment TU vu .... je vais masmock toutes les methodes dy service.......?
	
	@Test
	    public void contex_loads() throws Exception {
	        assertThat(controller).isNotNull();
	    }
	  
	    @Test
	    public void test_retrieve_all_items_OK() throws Exception {
	   
	        mockMvc.perform(get("/api/wall/all"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        	    .andExpect(jsonPath("$[*]", hasSize(12)))
	        	    .andExpect(jsonPath("$[0].file").value("picture1.jpg"))
	        	    .andExpect(jsonPath("$[11].file").value("codevideo5"))
	        		;
	        
	        verify(wallServiceMock).retrieveAllItems();
	        verifyNoMoreInteractions(wallServiceMock);
	    }
	    
	    @Test
	    public void test_retrieve_all_pictures_OK() throws Exception {
	   
	        mockMvc.perform(get("/api/wall/pictures"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        	    .andExpect(jsonPath("$[*]", hasSize(7)))
	        	    .andExpect(jsonPath("$[0].file").value("picture1.jpg"))
	        	    .andExpect(jsonPath("$[6].file").value("picture7.jpg"))
	        		;
	        
	        verify(wallServiceMock).retrieveAllPictures();
	        verifyNoMoreInteractions(wallServiceMock);
	    }
	    
	    
	    @Test
	    public void test_retrieve_all_videos_OK() throws Exception {
	   
	        mockMvc.perform(get("/api/wall/videos"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        	    .andExpect(jsonPath("$[*]", hasSize(5)))
	        	    .andExpect(jsonPath("$[0].file").value("codevideo1"))
	        	    .andExpect(jsonPath("$[4].file").value("codevideo5"))
	        		;
	        
	        verify(wallServiceMock).retrieveAllVideos();
	        verifyNoMoreInteractions(wallServiceMock);
	    }
	    
	    
	    
	    @Test
	    public void testRetrieveAllError() throws Exception{

	         this.mockMvc.perform(get("/api/wall/all")
	                 .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	                 .andExpect(status().isOk())
	                 .andExpect(content().contentType("application/json"));
	    }

	    
	    private List<Item> createSomeItems() {
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
		  return items;
		}
	    /*private List<WallItem> createSomeItems2() {
			  items2 = new ArrayList<WallItem>();
			  items2.add(new WallItem(1,"/some/local/folder/", "picture1.jpg", "First Picture"));
			  items2.add(new WallItem(2,"/some/local/folder/", "picture2.jpg", "Second Picture"));
			  items2.add(new WallItem(3,"http://youtube.com/some/path/", "codevideo1", "Demo video 1"));
			  items2.add(new WallItem(4,"/some/local/folder/", "picture3", "Third Picture"));
			  items2.add(new WallItem(5,"http://youtube.com/some/path/", "codevideo2", "Demo video 2"));
			  items2.add(new WallItem(6,"http://youtube.com/some/path/", "codevideo3", "Demo video 3"));
			  items2.add(new WallItem(7,"/some/local/folder/", "picture4.jpg", "Fourth Picture"));
			  items2.add(new WallItem(8,"/some/local/folder/", "picture5.jpg", "Fifth Picture"));
			  items2.add(new WallItem(9,"/some/local/folder/", "picture6.jpg", "Sixth Picture"));
			  items2.add(new WallItem(10,"http://youtube.com/some/path/", "codevideo4", "Demo video 4"));
			  items2.add(new WallItem(11,"/some/local/folder/", "picture7.jpg", "Seventh Picture"));
			  items2.add(new WallItem(12,"http://youtube.com/some/path/", "codevideo5", "Demo video 5"));
			  return items2;
		}*/
	    
}
