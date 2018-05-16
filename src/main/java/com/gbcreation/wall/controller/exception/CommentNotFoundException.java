package com.gbcreation.wall.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommentNotFoundException extends RuntimeException {
	public CommentNotFoundException(long commentId) {
		super("could not find comment '" + commentId + "'.");
	}
	public CommentNotFoundException(String msg) {
		super(msg + ".");
	}
}
