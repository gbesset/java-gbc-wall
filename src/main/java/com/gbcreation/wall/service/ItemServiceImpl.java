package com.gbcreation.wall.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.repository.ItemFilterSpecifications;
import com.gbcreation.wall.repository.ItemRepository;
import com.gbcreation.wall.util.WallUtils;

@Service
public class ItemServiceImpl implements ItemService{
	
	@Value("${wall.item.page.size:15}")
	private int pageNbItem;

	@Resource
	private ItemRepository itemRepository;

	
	@Override
	public Long countAll() {
			return itemRepository.count();
	}
	@Override
	public Long countPictures() {
			return itemRepository.count(ItemFilterSpecifications.isItemPicture());
	}
	@Override
	public Long countVideos() {
		return itemRepository.count(ItemFilterSpecifications.isItemVideo());
	}
	
	
	@Override
	public Item findById(Long id) {
		return itemRepository.findOne(id);
	}

	@Override
	public List<Item> findByFile(String file) {
		return itemRepository.findByFileOrderByCreatedAtDesc(file);
	}

	@Override
	public List<Item> findByFileLike(String file) {
		return itemRepository.findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc(file);
	}
	
	@Override
	public List<Item> findByDescriptionLike(String description) {
		return itemRepository.findAll(ItemFilterSpecifications.descriptionLike(description), new Sort(Sort.Direction.DESC,"createdAt"));
	}
	
	@Override
	public List<Item> retrieveAllItems() {
		return WallUtils.convertIterableToList(itemRepository.findAll(new Sort(Sort.Direction.DESC,"createdAt")));
	}

	@Override
	public List<Item> retrieveAllPictures() {
		return WallUtils.convertIterableToList(itemRepository.findAll(ItemFilterSpecifications.isItemPicture(),new Sort(Sort.Direction.DESC,"createdAt")));
	}

	@Override
	public List<Item> retrieveAllVideos() {
		return WallUtils.convertIterableToList(itemRepository.findAll(ItemFilterSpecifications.isItemVideo(),new Sort(Sort.Direction.DESC,"createdAt")));
	}

	@Override
	public void addItem(Item i) {
		itemRepository.save(i);
	}

	@Override
	public Item updateItem(Item i) {
		return itemRepository.save(i);
	}

	@Override
	public void deleteItem(Item i) {
		itemRepository.delete(i);
	}
	@Override
	public void updateItem(String id, Item item) {
		//TODO.........; 
		Long idItem = new Long(id);
		if(itemRepository.exists(idItem)) {
			Item foundItemm = itemRepository.findOne(idItem);
			
			itemRepository.save(item);
		}
	}
	
}
