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
	List<Item> findByFileLike(String title);
	List<Item> findByDescriptionLike(String description);
	
	Page<Item> retrieveItems(Pageable pageable);
	List<Item> retrieveAllItems();
	List<Item> retrieveAllPictures();
	List<Item> retrieveAllVideos();

	
	//TODO avec pageable (page=x?nbelem=y
	//voir meme sort = https://stackoverflow.com/questions/43028457/spring-jpa-specification-with-sort
	//List<Item> retrieveAllItems(Page);
	//List<Item> retrieveAllPictures();
	//List<Item> retrieveAllVideos();
	
	//Pour l'administration
	void addItem(Item i);
	Item updateItem(Item i);
	void deleteItem(Item i);
	
	void updateItem(String id, Item item);

	
}
