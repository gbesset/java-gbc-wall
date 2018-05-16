package com.gbcreation.wall.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemNotFoundException extends RuntimeException {
	public ItemNotFoundException(long itemId) {
		super("could not find item '" + itemId + "'.");
	}
	
	public ItemNotFoundException(String msg) {
		super(msg + ".");
	}
}
