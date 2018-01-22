package com.gbcreation.wall.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;

public interface CommentRepository extends CrudRepository<Comment, Long>{

	List<Comment> findByIdOrderByCreatedAtDesc(Long id);
	List<Comment> findTop100ByCommentContainingIgnoreCaseOrderByCreatedAtDesc(String comment);
	List<Comment> findTop100ByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(String author);
	
	long countByItemId(Item itemId);
	List<Comment> findByItemIdOrderByCreatedAtDesc(Item itemId);
	List<Comment> findByItemIdIdOrderByCreatedAtDesc(Long itemId);
}
