package com.gbcreation.wall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.service.WallItem;
import com.gbcreation.wall.service.WallService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

	@RestController
	@RequestMapping("/api/wall")
	@Slf4j
	@Setter
	public class WallController {
		
	@Autowired
	private WallService wallService;
	
	//TODO
	//1 une seule methode avec critères, etc puto que Photo, Videos,...

	@RequestMapping(value="all",method=RequestMethod.GET)
	public @ResponseBody Iterable<Item> retrieveAll() {
		log.info("WallController: retrieveAll");
		return wallService.retrieveAllItems();
	}
	
	@RequestMapping(value="pictures",method=RequestMethod.GET)
	public @ResponseBody List<WallItem> retrieveAllPictures() {
		log.info("WallController: retrieveAllPictures");
		return wallService.retrieveAllPictures();
	}
	
	@RequestMapping(value="videos",method=RequestMethod.GET)
	public @ResponseBody List<WallItem> retrieveAllVideos() {
		log.info("WallController: retrieveAllVideos");
		return wallService.retrieveAllVideos();
	}
	
	
	@RequestMapping(value="add",method=RequestMethod.GET)
	public @ResponseBody String addItem() {
		log.info("WallController: addItem");
		wallService.addItem();
		return "item added";
	}
}
