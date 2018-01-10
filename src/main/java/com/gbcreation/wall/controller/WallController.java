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

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
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
	

	@RequestMapping(value="/count",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Long> count() {
		log.info("WallController: count items");
		Map<String,Long> count=new HashMap<>();
		count.put("count-all", itemService.countAll());
		count.put("count-pictures", itemService.countPictures());
		count.put("count-videos", itemService.countVideos());
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
	public @ResponseBody Iterable<Item> retrieveAll() {
		log.info("WallController: retrieveAll");
		return itemService.retrieveAllItems();
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
	
	
	//post ou put? au moins POST...
	@RequestMapping(value="/add",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String addItem() {
		log.info("WallController: addItem");
		itemService.addItem(new Item("test",Instant.now().toString(), "test", ItemType.PICTURE));
		return "item added";
	}
	
	//update, delete....
}
