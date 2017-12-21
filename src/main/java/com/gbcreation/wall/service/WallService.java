package com.gbcreation.wall.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.repository.ItemRepository;

@Service
public class WallService implements IWallService{
	
	@Autowired
	private ItemRepository itemRepository;
	
	//private List<WallItem> items;
	
	/*public WallService() {
		super();
		//items = new ArrayList<WallItem>();
	}*/
	
	public void addItem() {
		Item item = new Item("nomFichier.png","","ceci est la descriptio du fichier");
		item.setType("Picture");
		
		itemRepository.save(item);

	}
	
	public Iterable<Item> retrieveAllItems() {
		return itemRepository.findAll();
	}
	
	public List<WallItem> retrieveAllPictures() {
		return null;
	}
	
	public List<WallItem> retrieveAllVideos() {
		return null;
	}
	
	public List<WallItem> retrieveAllFromTo(Date from, Date to) {
		return null;
	}
	
	
	
}
