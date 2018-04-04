package com.gbcreation.wall.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
import com.gbcreation.wall.service.CommentService;
import com.gbcreation.wall.service.ItemService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

	@RestController
	@RequestMapping("/api/wall")
	@Slf4j
	@Setter
	public class WallController {
		
	@Resource
	private ItemService itemService;
	
	@Resource
	private CommentService commentService;

	@RequestMapping(value="/count",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Long> count() {
		log.info("WallController: count items");
		Map<String,Long> count=new HashMap<>();
		count.put("count-all", itemService.countAll());
		count.put("count-pictures", itemService.countPictures());
		count.put("count-videos", itemService.countVideos());
		count.put("count-comments", commentService.countAll());
		return count;
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> findById(@PathVariable("id") Long id) {
		log.info("WallController: findById {}",id);
		Map<String, Object> result = new HashMap<String,Object>();
		
		Item itemFound = itemService.findById(id);
		
		if(itemFound != null) {
			result.put("prev", "todo (id item)");
			result.put("item", itemFound);
			result.put("next", "todo (id item)");
		} else {
			result.put("prev", "");
			result.put("item", "no item found");
			result.put("next", "");
		}
		return result;
	}
	
	@RequestMapping(value="/all",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String retrieveAll() throws JsonProcessingException {
		log.info("WallController: retrieveAll");
		
		//Pb commentaire - item -> Filter non export Json. (Itrabe<Item> disparait pour String et on utilise ObjectMapper
		//Voir si OK ou si mieux....
		
		 ObjectMapper objectMapper = new ObjectMapper();
		 FilterProvider filters = new SimpleFilterProvider().addFilter("ItemFilter", SimpleBeanPropertyFilter.serializeAllExcept("itemId"));
		 // and then serialize using that filter provider:
		 return objectMapper.writer(filters).writeValueAsString(itemService.retrieveAllItems());
		
	}
	
	@RequestMapping(value="/pictures",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String retrieveAllPictures() throws JsonProcessingException {
		log.info("WallController: retrieveAllPictures");
		
		 ObjectMapper objectMapper = new ObjectMapper();
		 FilterProvider filters = new SimpleFilterProvider().addFilter("ItemFilter", SimpleBeanPropertyFilter.serializeAllExcept("itemId"));
		 // and then serialize using that filter provider:
		 return objectMapper.writer(filters).writeValueAsString(itemService.retrieveAllPictures());
	}
	
	@RequestMapping(value="/videos",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String retrieveAllVideos() throws JsonProcessingException {
		log.info("WallController: retrieveAllVideos");
		
		 ObjectMapper objectMapper = new ObjectMapper();
		 FilterProvider filters = new SimpleFilterProvider().addFilter("ItemFilter", SimpleBeanPropertyFilter.serializeAllExcept("itemId"));
		 // and then serialize using that filter provider:
		 return objectMapper.writer(filters).writeValueAsString(itemService.retrieveAllVideos());
	}
	
	@RequestMapping(value="/search/title/{title}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String retrieveItemsWithTitle(@PathVariable("title") String title) throws JsonProcessingException {
		log.info("WallController: retrieveItemsWithTitle");
		
		 ObjectMapper objectMapper = new ObjectMapper();
		 FilterProvider filters = new SimpleFilterProvider().addFilter("ItemFilter", SimpleBeanPropertyFilter.serializeAllExcept("itemId"));
		 // and then serialize using that filter provider:
		 return objectMapper.writer(filters).writeValueAsString(itemService.findByFileLike(title));
	}
	
	@RequestMapping(value="/search/description/{description}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String retrieveItemsWithDescription(@PathVariable("description") String description) throws JsonProcessingException {
		log.info("WallController: retrieveItemsWithDescription");
		
		ObjectMapper objectMapper = new ObjectMapper();
		 FilterProvider filters = new SimpleFilterProvider().addFilter("ItemFilter", SimpleBeanPropertyFilter.serializeAllExcept("itemId"));
		 // and then serialize using that filter provider:
		 return objectMapper.writer(filters).writeValueAsString(itemService.findByDescriptionLike(description));
	}
	
	@RequestMapping(value="/search/comment/{comment}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String retrieveCommentsWithComment(@PathVariable("comment") String comment) throws JsonProcessingException {
		log.info("WallController: retrieveCommentsWithComment");
		
		ObjectMapper objectMapper = new ObjectMapper();
		 FilterProvider filters = new SimpleFilterProvider().addFilter("ItemFilter", SimpleBeanPropertyFilter.serializeAllExcept("itemId"));
		 // and then serialize using that filter provider:
		 return objectMapper.writer(filters).writeValueAsString(commentService.findByCommentLike(comment));
	}
	
	@RequestMapping(value="/search/author/{author}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String retrieveCommentsWithAuthor(@PathVariable("author") String author) throws JsonProcessingException {
		log.info("WallController: retrieveCommentsWithAuthor");
		
		ObjectMapper objectMapper = new ObjectMapper();
		 FilterProvider filters = new SimpleFilterProvider().addFilter("ItemFilter", SimpleBeanPropertyFilter.serializeAllExcept("itemId"));
		 // and then serialize using that filter provider:
		 return objectMapper.writer(filters).writeValueAsString(commentService.findByAuthorLike(author));
	}
	
	
	//post ou put? au moins POST...
	/*@RequestMapping(value="/add",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String addItem() {
		log.info("WallController: addItem");
		itemService.addItem(new Item("test",Instant.now().toString(), "test", ItemType.PICTURE));
		return "item added";
	}*/
	
	//update, delete....
}
