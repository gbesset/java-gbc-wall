package com.gbcreation.wall.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gbcreation.wall.model.Carousel;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/home")
@CrossOrigin(origins = {"${settings.cors_origin}"})
@Slf4j
public class HomeController {

	
	@Value("${home.carousel.path}")
	private String path;

	@Value("#{'${home.carousel.imgs}'.split(',')}")
	private Set<String> imgs;
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody String home() {
		return "Hello on Welcome Page";
	}
	
	@RequestMapping(value="/carousel", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Carousel retrieveCarousel()  {
		
		log.info("HomeController: retrieveCarousel");
			
		Carousel response = new Carousel(path, imgs);
		
		return response;
	}
	
}
