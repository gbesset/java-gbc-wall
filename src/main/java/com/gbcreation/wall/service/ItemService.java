package com.gbcreation.wall.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gbcreation.wall.model.Item;

public interface ItemService {
	
	Long countAll();
	Long countPictures();
	Long countVideos();
	
	Item findById(Long id);
	List<Item> findByFile(String title);
	Page<Item> findByFileLike(String title, Pageable pageable);
	Page<Item> findByDescriptionLike(String description, Pageable pageable);
	
	Page<Item> retrieveItems(Pageable pageable);
	Page<Item> retrievePictures(Pageable pageable);
	Page<Item> retrieveVideos(Pageable pageable);

	
	
	//Pour l'administration
	Item addItem(Item i);
	Item updateItem(Item i);
	void deleteItem(Item i);
	
	void updateItem(String id, Item item);

	
}
