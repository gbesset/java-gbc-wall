package com.gbcreation.wall.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gbcreation.wall.controller.exception.ItemNotFoundException;
import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.service.CommentService;
import com.gbcreation.wall.service.ItemService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/wall")
@CrossOrigin(origins = {"http://localhost:4200", "http://k.g.gbcreation.fr"})
@Slf4j
@Setter
public class WallController {
		
	@Resource
	private ItemService itemService;
	
	@Resource
	private CommentService commentService;

	
	@RequestMapping(value="/login",method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,String> login(@RequestBody String user) {
		log.info("WallController: login -->{}",user);
		Map<String,String> response = new HashMap<>();
		
		//En attendant mise en place spring security et JWT
		if("gbe@fr".equals(user)) {
			response.put("avaibility", Instant.now().toString());
			response.put("status", "connected");
		}
		else {
			response.put("msg", "user not found");
		}
		return response;
	}
	
	
	@RequestMapping(value="/count",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Long> count() {
		log.info("WallController: count items");
		Map<String,Long> count=new HashMap<>();
		count.put("all", itemService.countAll());
		count.put("pictures", itemService.countPictures());
		count.put("videos", itemService.countVideos());
		count.put("comments", commentService.countAll());
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
		
		log.info("WallController: retrieveItems");
			
		return itemService.retrieveItems(pageable);
	}
	
	@RequestMapping(value="/pictures",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Item> retrieveAllPictures(Pageable pageable) {
		log.info("WallController: retrieveAllPictures");
		
		return itemService.retrievePictures(pageable);
	}
	
	@RequestMapping(value="/videos",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Item> retrieveAllVideos(Pageable pageable) {
		log.info("WallController: retrieveAllVideos");
		
		return itemService.retrieveVideos(pageable);
	}
	
	@RequestMapping(value="/comments",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Comment> retrieveComments(Pageable pageable)  {
		log.info("WallController: retrieveComments");
		
		return commentService.retrieveComments(pageable);
	}
	
	@RequestMapping(value="/comment/{id}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Comment retrieveCommentById(@PathVariable("id") Long id) {
		log.info("WallController: retrieveCommentById {}",id);
		
		return commentService.findById(id);
	}
	
	@RequestMapping(value="/comments/item/{id}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Comment> retrieveCommentsByItemId(@PathVariable("id") Long id, Pageable pageable) {
		log.info("WallController: retrieveCommentsByItemId {}",id);
		
		return commentService.findByItemIdId(id,pageable);
	}
	
	@RequestMapping(value="/item/{itemId}/comment/add",method=RequestMethod.POST, consumes= { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE } , produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Comment> addComment(@PathVariable String itemId, @RequestBody Comment comment) {
		log.info("WallController: addComment on item {} from author {}", itemId, comment.getAuthor());
		
		Item itemFound = this.itemService.findById(new Long(itemId));
		if(itemFound == null) {
			return new ResponseEntity(new ItemNotFoundException(new Long(itemId)),HttpStatus.BAD_REQUEST);
		}
		
		comment.setItemId(itemFound);
		
		return new ResponseEntity<Comment>(commentService.addComment(comment),HttpStatus.OK);
	}
	
	@RequestMapping(value="/search/title/{title}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Item> retrieveItemsWithTitle(@PathVariable("title") String title, Pageable pageable) {
		log.info("WallController: retrieveItemsWithTitle");
		
		return itemService.findByFileLike(title, pageable);
	}
	
	@RequestMapping(value="/search/description/{description}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Item> retrieveItemsWithDescription(@PathVariable("description") String description, Pageable pageable) {
		log.info("WallController: retrieveItemsWithDescription");
		
		return itemService.findByDescriptionLike(description, pageable);
	}
	
	@RequestMapping(value="/search/comment/{comment}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Comment> retrieveCommentsWithComment(@PathVariable("comment") String comment, Pageable pageable) {
		log.info("WallController: retrieveCommentsWithComment");
		
		return commentService.findByCommentLike(comment, pageable);
	}
	
	@RequestMapping(value="/search/author/{author}",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Comment> retrieveCommentsWithAuthor(@PathVariable("author") String author, Pageable pageable) {
		log.info("WallController: retrieveCommentsWithAuthor");
		
		return commentService.findByAuthorLike(author, pageable);
	}
	
}
