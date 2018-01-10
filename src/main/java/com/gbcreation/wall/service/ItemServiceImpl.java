package com.gbcreation.wall.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.assertj.core.util.Lists;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
import com.gbcreation.wall.repository.ItemRepository;
import com.gbcreation.wall.util.WallUtils;
import com.gbcreation.wall.repository.ItemFilterSpecifications;

@Service
public class ItemServiceImpl implements ItemService{

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
		return itemRepository.findByFile(file);
	}

	@Override
	public List<Item> findByFileLike(String file) {
		return itemRepository.findByFileContaining(file);
	}
	
	@Override
	public List<Item> findByDescriptionLike(String description) {
		return itemRepository.findAll(ItemFilterSpecifications.descriptionLike(description));
	}
	
	@Override
	public List<Item> retrieveAllItems() {
		return WallUtils.convertIterableToList(itemRepository.findAll());
	}

	@Override
	public List<Item> retrieveAllPictures() {
		return WallUtils.convertIterableToList(itemRepository.findAll(ItemFilterSpecifications.isItemPicture()));
	}

	@Override
	public List<Item> retrieveAllVideos() {
		return WallUtils.convertIterableToList(itemRepository.findAll(ItemFilterSpecifications.isItemVideo()));
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



	
}
