package com.gbcreation.wall.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
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
	public Page<Item> findByFileLike(String file, Pageable pageable) {
		return itemRepository.findByFileContainingIgnoreCaseOrderByCreatedAtDesc(file, pageable);
	}
	
	@Override
	public Page<Item> findByDescriptionLike(String description, Pageable pageable) {
		return itemRepository.findByDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(description, pageable);
	}
	
	@Override
	public Page<Item> retrieveItems(Pageable pageable) {
		return itemRepository.findAllByOrderByCreatedAtDesc(pageable);
	}
	
	@Override
	public Page<Item> retrievePictures(Pageable pageable) {
		return itemRepository.findAllByTypeInOrderByCreatedAtDesc(Arrays.asList(ItemType.PICTURE),pageable);
	}

	@Override
	public Page<Item> retrieveVideos(Pageable pageable) {
		return itemRepository.findAllByTypeInOrderByCreatedAtDesc(Arrays.asList(ItemType.VIDEO,
																			ItemType.VIDEO_VIMEO,
																			ItemType.VIDEO_YOUTUBE)
															  ,pageable);
	}

	@Override
	public Item addItem(Item i) {
		return itemRepository.save(i);
	}

	@Override
	public Item updateItem(Item i) {
		Item itUpdated = itemRepository.save(i);
		itUpdated.setUpdatedAt(new Date());
		return itUpdated;
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
