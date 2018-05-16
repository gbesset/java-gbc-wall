package com.gbcreation.wall.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
import com.gbcreation.wall.service.CommentService;
import com.gbcreation.wall.service.ItemService;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
public class AdminControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;


	@MockBean 
	private ItemService itemServiceMock;

	@MockBean 
	private CommentService commentServiceMock;
	
	private static String PATH = "/api/admin";


	@Test
	public void test_add_item_200() throws Exception {
		Item it = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
		Item itSaved = it;
		itSaved.setId(145l);
		
		when(itemServiceMock.addItem(any(Item.class))).thenReturn(itSaved);
		
		
		mockMvc.perform(post(PATH + "/item/add")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(it)))
			.andExpect(jsonPath("$.id").value(145l))
			.andExpect(jsonPath("$.file").value("picture1.jpg"))
			.andExpect(status().is(200));
		
		 verify(itemServiceMock, times(1)).addItem(any(Item.class));
		 verifyNoMoreInteractions(itemServiceMock);
	}
	
	@Test
	public void test_add_bad_item_400() throws Exception {
		Item it = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
		Item itSaved = it;
		itSaved.setId(145l);
		
		when(itemServiceMock.addItem(any(Item.class))).thenThrow(PSQLException.class);
		
		mockMvc.perform(post(PATH + "/item/add")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(it)))
		.andDo(print())
			.andExpect(status().is(400));
		
		 verify(itemServiceMock, times(1)).addItem(any(Item.class));
		 verifyNoMoreInteractions(itemServiceMock);
	}
	
	@Test
	public void test_update_item_200() throws Exception {
		Item it = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
		it.setId(145l);
		
		when(itemServiceMock.findById(145l)).thenReturn(it);
		when(itemServiceMock.updateItem(any(Item.class))).thenReturn(it);
		
		
		mockMvc.perform(put(PATH + "/item/update")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(it)))
			.andExpect(jsonPath("$.id").value(145l))
			.andExpect(jsonPath("$.file").value("picture1.jpg"))
			.andExpect(status().is(200));
		
		 verify(itemServiceMock, times(1)).findById(145l);
		 verify(itemServiceMock, times(1)).updateItem(any(Item.class));
		 verifyNoMoreInteractions(itemServiceMock);
	}
	
	@Test
	public void test_update_item_method_post_error() throws Exception {
		Item it = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
		it.setId(145l);
		
		mockMvc.perform(post(PATH + "/item/update")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(it)))
			.andExpect(status().is(405));
		
		 verifyNoMoreInteractions(itemServiceMock);
	}
	
	@Test
	public void test_update_item_noId_400() throws Exception {
		Item it = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
		
		mockMvc.perform(put(PATH + "/item/update")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(it)))
			.andExpect(jsonPath("$.message").value("id not described in item."))
			.andExpect(status().is(400));
		
		 verifyNoMoreInteractions(itemServiceMock);
	}
	
	@Test
	public void test_update_item_not_found_400() throws Exception {
		Item it = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
		it.setId(145l);
		
		when(itemServiceMock.findById(145l)).thenReturn(null);
		
		
		mockMvc.perform(put(PATH + "/item/update")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(it)))
		.andDo(print())
			.andExpect(jsonPath("$.message").value("could not find item '145'."))
			.andExpect(status().is(400));
		
		 verify(itemServiceMock, times(1)).findById(145l);
		 verifyNoMoreInteractions(itemServiceMock);
	}
	
	@Test
	public void test_delete_item_200() throws Exception {
		
		Item it = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
		it.setId(145l);
		
		when(itemServiceMock.findById(145l)).thenReturn(it);
		doNothing().when(itemServiceMock).deleteItem(any(Item.class));
		
		
		mockMvc.perform(delete(PATH + "/item/delete/{id}", it.getId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").value("item deleted"))
			.andExpect(status().is(200));
		
		 verify(itemServiceMock, times(1)).findById(145l);
		 verify(itemServiceMock, times(1)).deleteItem(any(Item.class));
		 verifyNoMoreInteractions(itemServiceMock);
	}
	
	@Test
	public void test_delete_item_notFound_400() throws Exception {
		
		when(itemServiceMock.findById(145l)).thenReturn(null);
		
		mockMvc.perform(delete(PATH + "/item/delete/145")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message").value("could not find item '145'."))
			.andExpect(status().is(400));
		
		 verify(itemServiceMock, times(1)).findById(145l);
		 verifyNoMoreInteractions(itemServiceMock);
	}
	


	/* 
	 * 
	 * 		Comments 
	 * 
	 * */
	
	
	@Test
	public void test_add_comment_200() throws Exception {
		Item i= new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
	    i.setId(2l);
		Comment c = new Comment("author 1","this is my comment", i);
		c.setId(21l);
		
		when(itemServiceMock.findById(2l)).thenReturn(i);
		when(commentServiceMock.addComment(any(Comment.class))).thenReturn(c);
		
		mockMvc.perform(post(PATH+"/item/{id}/comment/add", i.getId())
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(c)))
			.andExpect(jsonPath("$.id").value(21l))
			.andExpect(jsonPath("$.author").value("author 1"))
			.andExpect(status().is(200));
		
		 verify(itemServiceMock, times(1)).findById(2l);
		 verify(commentServiceMock, times(1)).addComment(any(Comment.class));
		 verifyNoMoreInteractions(itemServiceMock);
		 verifyNoMoreInteractions(commentServiceMock);
	}
	
	@Test
	public void test_add_bad_comment_400() throws Exception {
		Item i= new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
	    i.setId(2l);
		Comment c = new Comment("author 1","", i);
		c.setId(21l);
		
		when(itemServiceMock.findById(2l)).thenReturn(i);
		when(commentServiceMock.addComment(any(Comment.class))).thenThrow(PSQLException.class);
		
		mockMvc.perform(post(PATH+"/item/{id}/comment/add", i.getId())
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(c)))
			.andExpect(status().is(400));
		
		 verify(itemServiceMock, times(1)).findById(2l);
		 verify(commentServiceMock, times(1)).addComment(any(Comment.class));
		 verifyNoMoreInteractions(itemServiceMock);
		 verifyNoMoreInteractions(commentServiceMock);
	}
	
	@Test
	public void test_add_comment_noItemId_400() throws Exception {
		Item i= new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
	    i.setId(2l);
		Comment c = new Comment("author 1","this is my comment", i);
		
		when(itemServiceMock.findById(2l)).thenReturn(null);
		
		mockMvc.perform(post(PATH+"/item/{id}/comment/add", i.getId())
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(c)))
			.andExpect(status().is(400))
			.andExpect(jsonPath("$.message").value("could not find item '2'."));

		
		 verify(itemServiceMock, times(1)).findById(2l);
		 verifyNoMoreInteractions(itemServiceMock);
		 verifyNoMoreInteractions(commentServiceMock);
	}
	
	@Test
	public void test_update_comment_200() throws Exception {
		Item it= new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
	    it.setId(2l);
		Comment c = new Comment("author 1","this is my comment", it);
		c.setId(21l);
		
		when(commentServiceMock.findById(21l)).thenReturn(c);
		when(commentServiceMock.updateComment(any(Comment.class))).thenReturn(c);
		
		
		mockMvc.perform(put(PATH + "/comment/update")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(c)))
			.andExpect(jsonPath("$.id").value(21l))
			.andExpect(jsonPath("$.author").value("author 1"))
			.andExpect(status().is(200));
		
		 verify(commentServiceMock, times(2)).findById(21l);
		 verify(commentServiceMock, times(1)).updateComment(any(Comment.class));
		 verifyNoMoreInteractions(commentServiceMock);
	}

	@Test
	public void test_update_comment_noId_400() throws Exception {
		Item it= new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
	    it.setId(2l);
		Comment c = new Comment("author 1","this is my comment", it);
		
		
		mockMvc.perform(put(PATH + "/comment/update")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(c)))
			.andExpect(jsonPath("$.message").value("id not described in comment."))
			.andExpect(status().is(400));
		
		 verifyNoMoreInteractions(commentServiceMock);
	}
	
	@Test
	public void test_update_comment_not_found_400() throws Exception {
		Item it= new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
	    it.setId(2l);
		Comment c = new Comment("author 1","this is my comment", it);
		c.setId(21l);
		
		when(commentServiceMock.findById(21l)).thenReturn(null);
		
		mockMvc.perform(put(PATH + "/comment/update")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(c)))
			.andExpect(jsonPath("$.message").value("could not find comment '21'."))
			.andExpect(status().is(400));
		
		 verify(commentServiceMock, times(1)).findById(21l);
		 verifyNoMoreInteractions(commentServiceMock);
	}
	
	@Test
	public void test_delete_comment_200() throws Exception {
		
		Item it= new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
	    it.setId(2l);
		Comment c = new Comment("author 1","this is my comment", it);
		c.setId(21l);
		
		when(commentServiceMock.findById(21l)).thenReturn(c);
		doNothing().when(commentServiceMock).deleteComment(any(Comment.class));
		
		mockMvc.perform(delete(PATH + "/comment/delete/{id}", c.getId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").value("comment deleted"))
			.andExpect(status().is(200));
		
		 verify(commentServiceMock, times(1)).findById(21l);
		 verify(commentServiceMock, times(1)).deleteComment(any(Comment.class));
		 verifyNoMoreInteractions(commentServiceMock);
	}
	
	@Test
	public void test_delete_comment_notFound_400() throws Exception {
		
		when(commentServiceMock.findById(21l)).thenReturn(null);
		
		mockMvc.perform(delete(PATH + "/comment/delete/21")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.message").value("could not find comment '21'."))
			.andExpect(status().is(400));
		
		 verify(commentServiceMock, times(1)).findById(21l);
		 verifyNoMoreInteractions(commentServiceMock);
	}
	
	 /*
     * converts a Java object into JSON representation
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
}
