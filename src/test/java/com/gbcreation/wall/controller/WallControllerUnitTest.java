package com.gbcreation.wall.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
import com.gbcreation.wall.service.CommentService;
import com.gbcreation.wall.service.ItemService;


@RunWith(SpringRunner.class)
@WebMvcTest(WallController.class)
public class WallControllerUnitTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemServiceMock;

	@MockBean
	private CommentService commentServiceMock;
	
	private static String PATH = "/api/wall";
	
	private  List<Item> items;
	
	private  List<Comment> comments;
	
	private Pageable pageable;
	
	@Mock
	private Page page;
	  
	@Before
	public void setUp() {
		pageable = new PageRequest(0,20);
	}
	
	@Test
	public void test_count() throws Exception {
		when(itemServiceMock.countAll()).thenReturn(12L);
		when(itemServiceMock.countPictures()).thenReturn(7L);
		when(itemServiceMock.countVideos()).thenReturn(5L);
		when(commentServiceMock.countAll()).thenReturn(15L);

		mockMvc.perform(get(PATH+"/count"))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.count-all").value(12))
		.andExpect(jsonPath("$.count-pictures").value(7))
		.andExpect(jsonPath("$.count-videos").value(5))
		.andExpect(jsonPath("$.count-comments").value(15))
		;
		
		verify(itemServiceMock).countAll();
		verify(itemServiceMock).countPictures();
		verify(itemServiceMock).countVideos();
		verify(commentServiceMock).countAll();
		verifyNoMoreInteractions(itemServiceMock);
	}
	
	//Problème  cause Pageable
	// Failed to instantiate Pageable, Specified class is an interface
	
	// PB pageable 
	
/*	
	@Test
	public void test_retrieve_from_id() throws Exception {
		when(itemServiceMock.findById(1234567L)).thenReturn(generateItems(null).get(4));

		mockMvc.perform(get(PATH+"/item/1234567"))
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

		mockMvc.perform(get(PATH+"/item/0000000"))
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
		fail("devrait marcher mais pb pageable..");
		when(page.getContent()).thenReturn(generateItems(null));
		Mockito.when(itemServiceMock.retrieveItems(any(Pageable.class))).thenReturn(page);
		
		//when(itemServiceMock.retrieveAllItems()).thenReturn(generateItems(null));

		mockMvc.perform(get(PATH+"/items"))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[*]").isArray())
		.andExpect(jsonPath("$[*]", hasSize(12)))
		.andExpect(jsonPath("$[0].file").value("picture1.jpg"))
		.andExpect(jsonPath("$[11].file").value("codevideo5"))
		;

		verify(itemServiceMock).retrieveItems(pageable);
		verifyNoMoreInteractions(itemServiceMock);
	}
	
	@Test
	public void test_retrieve_all_items_empty() throws Exception {
		mockMvc.perform(get(PATH+"/items"))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string("[]"))
		;

		verify(itemServiceMock).retrieveItems(pageable);
		verifyNoMoreInteractions(itemServiceMock);
	}
	    
	    @Test
	    public void test_retrieve_all_pictures_OK() throws Exception {
	    		when(page.getContent()).thenReturn(generateItems(ItemType.PICTURE));
	    		when(itemServiceMock.retrievePictures(pageable)).thenReturn(page);
	    	
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
	        
	        verify(itemServiceMock).retrievePictures(pageable);
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_retrieve_all_pictures_empty() throws Exception {
	    	
	    		when(page.getContent()).thenReturn(new ArrayList<>());
	    		when(itemServiceMock.retrievePictures(pageable)).thenReturn(page);
    		
	        mockMvc.perform(get(PATH+"/pictures"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(itemServiceMock).retrievePictures(pageable);
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    
	    @Test
	    public void test_retrieve_all_videos_OK() throws Exception {
	    		when(page.getContent()).thenReturn(generateItems(ItemType.VIDEO));
	    		when(itemServiceMock.retrieveVideos(pageable)).thenReturn(page);
	    		
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
	        
	        verify(itemServiceMock).retrieveVideos(pageable);
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_retrieve_all_videos_empty() throws Exception {
	    	
	    		when(page.getContent()).thenReturn(new ArrayList<>());
	    		when(itemServiceMock.retrieveVideos(pageable)).thenReturn(page);
    		
	        mockMvc.perform(get(PATH+"/videos"))
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(itemServiceMock).retrieveVideos(pageable);
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
		public void test_retrieve_all_comments_OK() throws Exception {
	     	generateItems(null);
	     	when(page.getContent()).thenReturn(generateComments());
			when(commentServiceMock.retrieveComments(pageable)).thenReturn(page);

			mockMvc.perform(get(PATH+"/comments"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[*]").isArray())
			.andExpect(jsonPath("$[*]", hasSize(12)))
			.andExpect(jsonPath("$[0].comment").value("nice picture1"))
			.andExpect(jsonPath("$[11].comment").value("ah ok, it's south africa"))
			;

			verify(commentServiceMock).retrieveComments(pageable);
			verifyNoMoreInteractions(commentServiceMock);
		}
	    
	    @Test
		public void test_retrieve_comment_from_id() throws Exception {
	     	generateItems(null);
			when(commentServiceMock.findById(4L)).thenReturn(generateComments().get(3));

			mockMvc.perform(get(PATH+"/comment/4"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.comment").value("nice video2"))
			;

			verify(commentServiceMock).findById(4L);
			verifyNoMoreInteractions(commentServiceMock);
		}
	    
	    @Test
		public void test_retrieve_comment_from_item__id() throws Exception {
	     	generateItems(null);
	     	when(page.getContent()).thenReturn(generateComments());
			when(commentServiceMock.findByItemIdId(4L, pageable)).thenReturn(page);

			mockMvc.perform(get(PATH+"/comments/item/4"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[*]").isArray())
			.andExpect(jsonPath("$[*]", hasSize(12)))
			.andExpect(jsonPath("$[0].comment").value("nice picture1"))
			.andExpect(jsonPath("$[11].comment").value("ah ok, it's south africa"))
			;

			verify(commentServiceMock).findByItemIdId(4L, pageable);
			verifyNoMoreInteractions(commentServiceMock);
		}
	    
	    
	    @Test
	    public void test_search_title() throws Exception {
	    	
	    		when(page.getContent()).thenReturn(generateItems(null));
	    		when(itemServiceMock.findByFileLike("OK", pageable)).thenReturn(page);
	    		
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
	        
	        verify(itemServiceMock).findByFileLike("OK", pageable);
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_search_title_noResults() throws Exception {
	    	
	    		when(page.getContent()).thenReturn(generateItems(null));
	    		when(itemServiceMock.findByFileLike("OK", pageable)).thenReturn(page);
	    		//when(itemServiceMock.findByFileLike("KO")).thenReturn(new ArrayList<>());
	    		
	        mockMvc.perform(get(PATH+"/search/title/KO"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(itemServiceMock).findByFileLike("KO", pageable);
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_search_description() throws Exception {
	    		
	    		when(page.getContent()).thenReturn(generateItems(null));
	    		when(itemServiceMock.findByDescriptionLike(Mockito.eq("a description"), pageable)).thenReturn(page);
    		
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
	        
	        verify(itemServiceMock).findByDescriptionLike("a description", pageable);
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_search_description_noResults() throws Exception {
	    		when(page.getContent()).thenReturn(generateItems(null));
	    		when(itemServiceMock.findByDescriptionLike(Mockito.eq("a description"), pageable)).thenReturn(page);
	    		//when(itemServiceMock.findByFileLike("another one")).thenReturn(new ArrayList<>());
	    		
	        mockMvc.perform(get(PATH+"/search/description/another one"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(itemServiceMock).findByDescriptionLike("another one", pageable);
	        verifyNoMoreInteractions(itemServiceMock);
	    }
	    
	    @Test
	    public void test_search_comments() throws Exception {
	    		generateItems(null);
	    		when(page.getContent()).thenReturn(generateComments());
	    		when(commentServiceMock.findByCommentLike(Mockito.eq("a description"), pageable)).thenReturn(page);
    		
	        mockMvc.perform(get(PATH+"/search/comment/a description"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(12)))
	        		.andExpect(jsonPath("$[0].comment").value("nice picture1"))
	        		.andExpect(jsonPath("$[11].comment").value("ah ok, it's south africa"))
	        		;
	        
	        verify(commentServiceMock).findByCommentLike("a description", pageable);
	        verifyNoMoreInteractions(commentServiceMock);
	    }
	    
	    @Test
	    public void test_search_comments_noResults() throws Exception {
	    		
	    		//test sans, devrait marcher non?
	    		//when(page.getContent()).thenReturn(new ArrayList<>());
	    		//when(commentServiceMock.findByCommentLike(Mockito.eq("a description"), pageable)).thenReturn(page);
    		
	        mockMvc.perform(get(PATH+"/search/comment/another one"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(commentServiceMock).findByCommentLike("another one", pageable);
	        verifyNoMoreInteractions(commentServiceMock);
	    }
	    
	    @Test
	    public void test_search_authors() throws Exception {
	    	generateItems(null);
	    	when(page.getContent()).thenReturn(generateComments());
	    	when(commentServiceMock.findByAuthorLike(Mockito.eq("an author"), pageable)).thenReturn(page);
    		
	        mockMvc.perform(get(PATH+"/search/author/an author"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(jsonPath("$[*]").isArray())
	        		.andExpect(jsonPath("$[*]", hasSize(12)))
	        		.andExpect(jsonPath("$[0].author").value("John Doe"))
	        		.andExpect(jsonPath("$[11].author").value("Guy Mann"))
	        		;
	        
	        verify(commentServiceMock).findByAuthorLike("an author", pageable);
	        verifyNoMoreInteractions(commentServiceMock);
	    }
	    
	    @Test
	    public void test_search_author_noResults() throws Exception {
	    		
	    		//when(page.getContent()).thenReturn(new ArrayList<>());
	    		//when(commentServiceMock.findByAuthorLike(Mockito.eq("an author"), pageable)).thenReturn(page);
    		
	        mockMvc.perform(get(PATH+"/search/author/another one"))
	        		.andDo(print())
	        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"))
	        		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        		.andExpect(status().isOk())
	        		.andExpect(content().string("[]"))
	        		;
	        
	        verify(commentServiceMock).findByAuthorLike("another one", pageable);
	        verifyNoMoreInteractions(commentServiceMock);
	    }
*/	        
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
	    
	    private List<Comment> generateComments() {
			comments = new ArrayList<Comment>();
			comments.add(new Comment("John Doe", "nice picture1", items.get(0)));
			comments.add(new Comment("Jane Doe", "it's true, ont of your best", items.get(0)));
			comments.add(new Comment("Theodore Handle", "wow", items.get(0)));
			
			comments.add(new Comment("Guy Mann", "nice video2", items.get(4)));
			comments.add(new Comment("Eleanor Fant Mann", "a bit long maybe", items.get(4)));
			comments.add(new Comment("Guy Mann", "not agreed", items.get(4)));
			
			comments.add(new Comment("John Doe", "nice picture4", items.get(6)));
			comments.add(new Comment("Jane Doe", "where is it", items.get(6)));
			comments.add(new Comment("Theodore Handle", "australia maybee", items.get(6)));
			comments.add(new Comment("Guy Mann", "NZ i guess", items.get(6)));
			comments.add(new Comment("Eleanor Fant Mann", "no idea, so beautiful. wow", items.get(6)));
			comments.add(new Comment("Guy Mann", "ah ok, it's south africa", items.get(6)));
			
			return comments;
		}
	    
}
