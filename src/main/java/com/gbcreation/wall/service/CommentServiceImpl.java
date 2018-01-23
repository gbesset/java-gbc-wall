package com.gbcreation.wall.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

	@Resource
	private CommentRepository commentRepository;
	
	@Override
	public long countAll() {
		return commentRepository.count();
	}

	@Override
	public long countByItemId(Item itemId) {
		return commentRepository.countByItemId(itemId);
	}

	@Override
	public long countByItemIdId(long itemIdId) {
		return commentRepository.countByItemIdId(itemIdId);
	}

	@Override
	public Comment findById(Long id) {
		return commentRepository.findOne(id);
	}

	@Override
	public List<Comment> findByItemId(Item itemId) {
		return commentRepository.findByItemIdOrderByCreatedAtDesc(itemId);
	}

	@Override
	public List<Comment> findByItemIdId(Long itemIdId) {
		return commentRepository.findByItemIdIdOrderByCreatedAtDesc(itemIdId);
	}

	@Override
	public List<Comment> findByCommentLike(String comment) {
		return commentRepository.findTop100ByCommentContainingIgnoreCaseOrderByCreatedAtDesc(comment);
	}

	@Override
	public List<Comment> findByAuthorLike(String author) {
		return commentRepository.findTop100ByAuthorContainingIgnoreCaseOrderByCreatedAtDesc(author);
	}

	@Override
	public void addComment(Comment c) {
		commentRepository.save(c);
	}

	@Override
	public Comment updateComment(Comment c) {
		return commentRepository.save(c);
	}

	@Override
	public void deleteComment(Comment c) {
		commentRepository.delete(c);
	}

}
