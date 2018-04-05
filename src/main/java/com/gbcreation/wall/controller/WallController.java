package com.gbcreation.wall.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;
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
	
	@RequestMapping(value="/item/{id}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> retrieveItemById(@PathVariable("id") Long id) {
		log.info("WallController: retrieveItemById {}",id);
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
	
	@RequestMapping(value="/items", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Item> retrieveItems(Pageable pageable)  {
	//public Page<Item> retrieveItems(@RequestParam( "page" ) int page, @RequestParam( "size" ) int size)  {
		
		log.info("WallController: retrieveItems");
			
		//return itemService.retrieveAllItems();
		//return itemService.retrieveItems(new PageRequest(new Integer(page) -1, new Integer(size)));
		return itemService.retrieveItems(pageable);
	}
	
	@RequestMapping(value="/pictures",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Item> retrieveAllPictures() {
		log.info("WallController: retrieveAllPictures");
		
		return itemService.retrieveAllPictures();
	}
	
	@RequestMapping(value="/videos",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Item> retrieveAllVideos() {
		log.info("WallController: retrieveAllVideos");
		
		return itemService.retrieveAllVideos();
	}
	
	@RequestMapping(value="/comments",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Comment> retrieveComments()  {
		log.info("WallController: retrieveComments");
		
		return commentService.findAll();
	}
	
	@RequestMapping(value="/comment/{id}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Comment retrieveCommentById(@PathVariable("id") Long id) {
		log.info("WallController: retrieveCommentById {}",id);
		
		return commentService.findById(id);
	}
	
	@RequestMapping(value="/comments/item/{id}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Comment> retrieveCommentsByItemId(@PathVariable("id") Long id) {
		log.info("WallController: retrieveCommentsByItemId {}",id);
		
		return commentService.findByItemIdId(id);
	}
	
	
	@RequestMapping(value="/search/title/{title}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Item> retrieveItemsWithTitle(@PathVariable("title") String title) {
		log.info("WallController: retrieveItemsWithTitle");
		
		return itemService.findByFileLike(title);
	}
	
	@RequestMapping(value="/search/description/{description}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Item> retrieveItemsWithDescription(@PathVariable("description") String description) {
		log.info("WallController: retrieveItemsWithDescription");
		
		return itemService.findByDescriptionLike(description);
	}
	
	@RequestMapping(value="/search/comment/{comment}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Comment> retrieveCommentsWithComment(@PathVariable("comment") String comment) {
		log.info("WallController: retrieveCommentsWithComment");
		
		return commentService.findByCommentLike(comment);
	}
	
	@RequestMapping(value="/search/author/{author}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Comment> retrieveCommentsWithAuthor(@PathVariable("author") String author) {
		log.info("WallController: retrieveCommentsWithAuthor");
		
		return commentService.findByAuthorLike(author);
	}
	
}
