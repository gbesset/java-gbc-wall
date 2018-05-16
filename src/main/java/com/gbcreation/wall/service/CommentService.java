package com.gbcreation.wall.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;

public interface CommentService {
	long countAll();
	long countByItemId(Item itemId);
	long countByItemIdId(long itemIdId);
	
	Page<Comment> retrieveComments(Pageable pageable);
	Comment findById(Long id);
	List<Comment> findByItemId(Item itemId);
	Page<Comment> findByItemIdId(Long itemIdId, Pageable pageable);
	Page<Comment> findByCommentLike(String comment, Pageable pageable);
	Page<Comment> findByAuthorLike(String author, Pageable pageable);
	
	//Pour l'administration
	Comment addComment(Comment c);
	Comment updateComment(Comment c);
	void deleteComment(Comment c);
}
