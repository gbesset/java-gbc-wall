package com.gbcreation.wall.service;

import java.util.List;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;

public interface CommentService {
	long countAll();
	long countByItemId(Item itemId);
	long countByItemIdId(long itemIdId);
	
	List<Comment> findAll();
	Comment findById(Long id);
	List<Comment> findByItemId(Item itemId);
	List<Comment> findByItemIdId(Long itemIdId);
	List<Comment> findByCommentLike(String comment);
	List<Comment> findByAuthorLike(String author);
	
	//Pour l'administration
	void addComment(Comment c);
	Comment updateComment(Comment c);
	void deleteComment(Comment c);
}
