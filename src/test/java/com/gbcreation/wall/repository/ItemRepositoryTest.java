package com.gbcreation.wall.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;



@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemRepositoryTest {

	 @Autowired
	  private ItemRepository itemRepository;
	 
	 @Before
	 public void setup() {
		 itemRepository.deleteAll();
	  }

	 @Test
	  public void test_save_and_update_item() {
	    Item item = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
	    itemRepository.save(item);
	    assertEquals(1,itemRepository.count());
	    
	    item.setFile("new name of file");
	    itemRepository.save(item);
	    assertEquals("new name of file",itemRepository.findAll().iterator().next().getFile());
	  }

	 @Test
	  public void test_crud() {
		//Assert Create
		assertEquals(0, itemRepository.count());
	  	
	    Item itemSaved = itemRepository.save(new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE));
	  	assertEquals(1, itemRepository.count());
	    
	  	assertEquals(true, itemRepository.exists(itemSaved.getId()));
	  	
	    //Assert Find One and All
	  	assertEquals(itemSaved, itemRepository.findOne(itemSaved.getId()));
	  	
	  	Item itemSaved2 = itemRepository.save(new Item("codevideo1","http://youtube.com/some/path/", "Demo video 1",ItemType.VIDEO_YOUTUBE));
	  	assertEquals(2, itemRepository.count());
	  	
	  	for(Item it : itemRepository.findAll()){
	  		Item t = (it.getType()==ItemType.PICTURE) ? itemSaved : itemSaved2;
	  		assertEquals(t.getId(),it.getId());
	  	}
	  	
	  	//Assert Update
	  	itemSaved.setDescription("This is a description of the item");
	  	itemRepository.save(itemSaved);
	  	Item itemUpdated = itemRepository.findById(itemSaved.getId());
	  	assertEquals("This is a description of the item", itemUpdated.getDescription());
	  	 
	  	//Assert Delete
	  	itemRepository.delete(itemSaved);
	  	itemRepository.delete(itemSaved2);
	    assertEquals(0, itemRepository.count());
	  }
	  
	 @Test
	  public void test_findBy_some_attribute() {
		 for(Item it : createSomeItems()) {
			 itemRepository.save(it);
		 }

		 List<Item> results = itemRepository.findByFile("codevideo2");
		 assertEquals("codevideo2", results.get(0).getFile());
		 
		 results = itemRepository.findByFileMyRqt("codevideo2");
		 assertEquals("codevideo2", results.get(0).getFile());
		 
		 results = itemRepository.findByDescriptionContaining("Fourth Picture");
		 assertEquals("picture4.jpg", results.get(0).getFile());
		 
		 results = itemRepository.findByDescriptionContaining("Seventh");
		 assertEquals("picture7.jpg", results.get(0).getFile());
		 
		 results = itemRepository.findByDescriptionContaining("Picture");
		 assertEquals(7, results.size());
	  }
	 
	  @Test
	  public void test_paginate() {
		  for(Item it : createSomeItems()) {
				 itemRepository.save(it);
			 }
		    assertEquals(12, itemRepository.count());
		    
		    
		    //find by description
		    Sort sort = new Sort(new Sort.Order(Direction.DESC, "file"));
		    Pageable pageable = new PageRequest(0, 5, sort);

		    List<Item> itemFound = itemRepository.findByDescriptionContaining("Picture", pageable);
		    assertEquals(5, itemFound.size());
		    assertEquals("picture7.jpg", itemFound.get(0).getFile());
		    assertEquals("picture3", itemFound.get(4).getFile());

		    pageable = new PageRequest(1, 5, sort);
		    itemFound = itemRepository.findByDescriptionContaining("Picture", pageable);
		    assertEquals(2, itemFound.size());
		    assertEquals("picture2.jpg", itemFound.get(0).getFile());
		    assertEquals("picture1.jpg", itemFound.get(1).getFile());
			 
			
		    //find all
		    // 2ère page de résultats ; 3 résultats max par page.
		    Page<Item> allItemPaginated = itemRepository.findAll(new PageRequest(2, 3));
		 
		    assertEquals(2, allItemPaginated.getNumber());
		    assertEquals(3, allItemPaginated.getSize()); // la taille de la pagination
		    assertEquals(12, allItemPaginated.getTotalElements()); //nb total d'éléments récupérables
		    assertEquals(4, allItemPaginated.getTotalPages()); // nombre de pages total
		    assertTrue(allItemPaginated.hasContent());
	  }
	  
	  @Test
	  public void testFilter() {
		  fail("not yet implemented");
	  }
	  
	  
	  //TODO remplacer par une BDD et liquibase ?
	  private List<Item> createSomeItems() {
		  List<Item> items = new ArrayList<Item>();
		  items.add(new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE));
		  items.add(new Item("picture2.jpg","/some/local/folder/","Second Picture",ItemType.PICTURE));
		  items.add(new Item("codevideo1","http://youtube.com/some/path/", "Demo video 1",ItemType.VIDEO_YOUTUBE));
		  items.add(new Item("picture3","/some/local/folder/",  "Third Picture",ItemType.PICTURE));
		  items.add(new Item("codevideo2","http://youtube.com/some/path/", "Demo video 2",ItemType.VIDEO_YOUTUBE));
		  items.add(new Item("codevideo3","http://youtube.com/some/path/", "Demo video 3",ItemType.VIDEO_YOUTUBE));
		  items.add(new Item("picture4.jpg","/some/local/folder/", "Fourth Picture",ItemType.PICTURE));
		  items.add(new Item("picture5.jpg","/some/local/folder/","Fifth Picture",ItemType.PICTURE));
		  items.add(new Item("picture6.jpg","/some/local/folder/", "Sixth Picture",ItemType.PICTURE));
		  items.add(new Item("codevideo4","http://youtube.com/some/path/", "Demo video 4",ItemType.VIDEO_YOUTUBE));
		  items.add(new Item("picture7.jpg","/some/local/folder/", "Seventh Picture",ItemType.PICTURE));
		  items.add(new Item("codevideo5","http://youtube.com/some/path/", "Demo video 5",ItemType.VIDEO_YOUTUBE));
		  return items;
	  }
}
