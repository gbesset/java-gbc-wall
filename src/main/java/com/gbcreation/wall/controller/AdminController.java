package com.gbcreation.wall.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.service.ItemService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

	@Resource
	private ItemService itemService;
	
	@RequestMapping(value="/item/add",method=RequestMethod.POST)
	public @ResponseBody String addItem(@RequestBody Item item) {
		log.info("AdminController: addItem {}", item.getFile());
		itemService.addItem(item);
		return "item added";
	}
	
	@RequestMapping(value="/item/update/{id}",method=RequestMethod.PUT)
	public @ResponseBody String updateItem(@RequestBody Item item, @PathVariable("id") String id) {
		log.info("AdminController: AdminController id {}", id);
		itemService.updateItem(id, item);
		return "item updated";
	}
	
	//update, delete....
		
	@RequestMapping(value="/item/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody String deleteItem(@PathVariable String id) {
		log.info("AdminController: delete item {}",id);
		Item itemFound = itemService.findById(new Long(id));
		if(itemFound!=null) {
			itemService.deleteItem(itemFound);
			return "item deleted";
		}
		else {
			return "item not found";
		}
	}
}
