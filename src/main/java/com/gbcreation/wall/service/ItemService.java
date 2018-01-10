package com.gbcreation.wall.service;

import java.time.Instant;
import java.util.List;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;

public interface ItemService {
	
	Long countAll();
	Long countPictures();
	Long countVideos();
	
	Item findById(Long id);
	List<Item> findByFile(String title);
	List<Item> findByFileLike(String title);
	List<Item> findByDescriptionLike(String description);
	
	List<Item> retrieveAllItems();
	List<Item> retrieveAllPictures();
	List<Item> retrieveAllVideos();
	
	//Pour l'administration
	void addItem(Item i);
	Item updateItem(Item i);
	void deleteItem(Item i);

	
}
