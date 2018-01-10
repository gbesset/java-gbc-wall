package com.gbcreation.wall.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
import com.gbcreation.wall.service.ItemService;


@RunWith(SpringRunner.class)
@WebMvcTest(WallController.class)
public class WallControllerUnitTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemServiceMock;

	private static String PATH = "/api/wall";
	
	private  List<Item> items;
	 
	  
	@Test
	public void test_count() throws Exception {
		when(itemServiceMock.countAll()).thenReturn(12L);
		when(itemServiceMock.countPictures()).thenReturn(7L);
		when(itemServiceMock.countVideos()).thenReturn(5L);

		mockMvc.perform(get(PATH+"/count"))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.count-all").value(12))
		.andExpect(jsonPath("$.count-pictures").value(7))
		.andExpect(jsonPath("$.count-videos").value(5))
		;
		
		verify(itemServiceMock).countAll();
		verify(itemServiceMock).countPictures();
		verify(itemServiceMock).countVideos();
		verifyNoMoreInteractions(itemServiceMock);
	}
	
	@Test
	public void test_retrieve_from_id() throws Exception {
		when(itemServiceMock.findById(1234567L)).thenReturn(generateItems(null).get(4));

		mockMvc.perform(get(PATH+"/1234567"))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.prev").value("todo (id item)"))
		.andExpect(jsonPath("$.item").exists())
		.andExpect(jsonPath("$.item.file").value("codevideo2"))
		.andExpect(jsonPath("$.next").value("todo (id item)"))
		;

		verify(itemServiceMock).findById(1234567L);
		verifyNoMoreInteractions(itemServiceMock);
	}
	@Test
	public void test_retrieve_from_id_notFound() throws Exception {
		when(itemServiceMock.findById(1234567L)).thenReturn(generateItems(null).get(4));

		mockMvc.perform(get(PATH+"/0000000"))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.prev").value(""))
		.andExpect(jsonPath("$.item").value("no item found"))
		.andExpect(jsonPath("$.next").value(""))
		;

		verify(itemServiceMock).findById(0000000L);
		verifyNoMoreInteractions(itemServiceMock);
	}
	
	
	@Test
	public void test_retrieve_all_items_OK() throws Exception {
		when(itemServiceMock.retrieveAllItems()).thenReturn(generateItems(null));

		mockMvc.perform(get(PATH+"/all"))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[*]").isArray())
		.andExpect(jsonPath("$[*]", hasSize(12)))
		.andExpect(jsonPath("$[0].file").value("picture1.jpg"))
		.andExpect(jsonPath("$[11].file").value("codevideo5"))
		;

		verify(itemServiceMock).retrieveAllItems();
		verifyNoMoreInteractions(itemServiceMock);
	}
	@Test
	public void test_retrieve_all_items_empty() throws Exception {
		mockMvc.perform(get(PATH+"/all"))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string("[]"))
		;

		verify(itemServiceMock).retrieveAllItems();
		verifyNoMoreInteractions(itemServiceMock);
	}
	    
	    @Test
	    public void test_retrieve_all_pictures_OK() throws Exception {
	    		when(itemServiceMock.retrieveAllPictures()).thenReturn(generateItems(ItemType.PICTURE));
	    	
	        mockMvc.perform(get(PATH+"/pictures"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        	    .andExpect(jsonPath("$[*]", hasSize(7)))
	        	    .andExpect(jsonPath("$[0].file").value("picture1.jpg"))
	        	    .andExpect(jsonPath("$[6].file").value("picture7.jpg"))
	        		;
	        
	        verify(itemServiceMock).retrieveAllPictures();
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_retrieve_all_pictures_empty() throws Exception {
	    	
	        mockMvc.perform(get(PATH+"/pictures"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(itemServiceMock).retrieveAllPictures();
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    
	    @Test
	    public void test_retrieve_all_videos_OK() throws Exception {
	    		when(itemServiceMock.retrieveAllVideos()).thenReturn(generateItems(ItemType.VIDEO));
	        mockMvc.perform(get(PATH+"/videos"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        	    .andExpect(jsonPath("$[*]", hasSize(5)))
	        	    .andExpect(jsonPath("$[0].file").value("codevideo1"))
	        	    .andExpect(jsonPath("$[4].file").value("codevideo5"))
	        		;
	        
	        verify(itemServiceMock).retrieveAllVideos();
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_retrieve_all_videos_empty() throws Exception {
	        mockMvc.perform(get(PATH+"/videos"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(itemServiceMock).retrieveAllVideos();
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_search_title() throws Exception {
	    		when(itemServiceMock.findByFileLike("OK")).thenReturn(generateItems(null));
	    		
	        mockMvc.perform(get(PATH+"/search/title/OK"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(12)))
	        		.andExpect(jsonPath("$[0].file").value("picture1.jpg"))
	        		.andExpect(jsonPath("$[11].file").value("codevideo5"))
	        		;
	        
	        verify(itemServiceMock).findByFileLike("OK");
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_search_title_noResults() throws Exception {
	    		when(itemServiceMock.findByFileLike("OK")).thenReturn(generateItems(null));
	    		//when(itemServiceMock.findByFileLike("KO")).thenReturn(new ArrayList<>());
	    		
	        mockMvc.perform(get(PATH+"/search/title/KO"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(itemServiceMock).findByFileLike("KO");
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_search_description() throws Exception {
	    	when(itemServiceMock.findByDescriptionLike(Mockito.eq("a description"))).thenReturn(generateItems(null));
    		
	        mockMvc.perform(get(PATH+"/search/description/a description"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(12)))
	        		.andExpect(jsonPath("$[0].file").value("picture1.jpg"))
	        		.andExpect(jsonPath("$[11].file").value("codevideo5"))
	        		;
	        
	        verify(itemServiceMock).findByDescriptionLike("a description");
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_search_description_noResults() throws Exception {
	    		when(itemServiceMock.findByDescriptionLike(Mockito.eq("a description"))).thenReturn(generateItems(null));
	    		//when(itemServiceMock.findByFileLike("another one")).thenReturn(new ArrayList<>());
	    		
	        mockMvc.perform(get(PATH+"/search/description/another one"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(itemServiceMock).findByDescriptionLike("another one");
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    
	    @Test
	    public void testRetrieveAllError() throws Exception{

	         this.mockMvc.perform(get("/api/wall/all")
	                 .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	                 .andExpect(status().isOk())
	                 .andExpect(content().contentType("application/json"));
	    }

	    
	    private List<Item> generateItems(ItemType type) {
		    	items = new ArrayList<Item>();
		    	if(type==null || ItemType.PICTURE==type) {
		    		items.add(new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE));
		    		items.add(new Item("picture2.jpg","/some/local/folder/","Second Picture",ItemType.PICTURE));
		    	}
		    	if(type==null || ItemType.VIDEO==type) {
		    		items.add(new Item("codevideo1","http://youtube.com/some/path/", "Demo video 1",ItemType.VIDEO));
		    	}
		    	if(type==null || ItemType.PICTURE==type) {
		    		items.add(new Item("picture3.jpg","/some/local/folder/",  "Third Picture",ItemType.PICTURE));
		    	}
		    	if(type==null || ItemType.VIDEO==type) {
		    		items.add(new Item("codevideo2","http://youtube.com/some/path/", "Demo video 2",ItemType.VIDEO_YOUTUBE));
		    		items.add(new Item("codevideo3","http://youtube.com/some/path/", "Demo video 3",ItemType.VIDEO_VIMEO));
		    	}
		    	if(type==null || ItemType.PICTURE==type) {
		    		items.add(new Item("picture4.jpg","/some/local/folder/", "Fourth Picture",ItemType.PICTURE));
		    		items.add(new Item("picture5.jpg","/some/local/folder/","Fifth Picture",ItemType.PICTURE));
		    		items.add(new Item("picture6.jpg","/some/local/folder/", "Sixth Picture",ItemType.PICTURE));
		    	}
		    	if(type==null || ItemType.VIDEO==type) {
		    		items.add(new Item("codevideo4","http://youtube.com/some/path/", "Demo video 4",ItemType.VIDEO_YOUTUBE));
		    	}
		    	if(type==null || ItemType.PICTURE==type) {
		    		items.add(new Item("picture7.jpg","/some/local/folder/", "Seventh Picture",ItemType.PICTURE));
		    	}
		    	if(type==null || ItemType.VIDEO==type) {
		    		items.add(new Item("codevideo5","http://youtube.com/some/path/", "Demo video 5",ItemType.VIDEO_YOUTUBE));
		    	}
		    	return items;
    }
	    
}
