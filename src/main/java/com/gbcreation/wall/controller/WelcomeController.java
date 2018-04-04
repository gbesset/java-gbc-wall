package com.gbcreation.wall.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/")
@Slf4j
public class WelcomeController {


	@RequestMapping(value="/",method=RequestMethod.GET)
	public @ResponseBody String welcome() {
		return "gbcreation - wall backend - Welcome";
	}
	
}
