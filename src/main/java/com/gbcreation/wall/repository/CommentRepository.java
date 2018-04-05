package com.gbcreation.wall.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;

public interface CommentRepository extends CrudRepository<Comment, Long>{

	List<Comment> findByIdOrderByCreatedAtDesc(Long id);
	
	//VIRER !!
	List<Comment> findTop100ByCommentContainingIgnoreCaseOrderByCreatedAtDesc(String comment);
	List<Comment> findTop100ByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(String author);
	
	long countByItemId(Item itemId);
	long countByItemIdId(long itemIdId);
	
	//Attention au by  findAll BY  order....
		Page<Comment> findAllByOrderByCreatedAtDesc(Pageable pageable);
		
	List<Comment> findByItemIdOrderByCreatedAtDesc(Item itemId);
	Page<Comment> findByItemIdIdOrderByCreatedAtDesc(Long itemIdId, Pageable pageable);
	Page<Comment> findByCommentContainingIgnoreCaseOrderByCreatedAtDesc(String comment, Pageable pageable);
	Page<Comment> findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(String author, Pageable pageable);
}
