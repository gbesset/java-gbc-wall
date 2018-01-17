package com.gbcreation.wall.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.gbcreation.wall.util.WallUtils;



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
	  	
	  	Item itemSaved2 = itemRepository.save(new Item("codevideo1","http://youtube.com/some/path/", "Demo video 1", ItemType.VIDEO_YOUTUBE));
	  	assertEquals(2, itemRepository.count());
	  	
	  	for(Item it : itemRepository.findAll()){
	  		if(ItemType.PICTURE.equals(it.getType())){
	  			assertEquals(it, itemSaved);
	  		}
	  		else {
	  			assertEquals(it, itemSaved2);
	  		}
	  	}
	  	
	  	//Assert Update
	  	itemSaved.setDescription("This is a description of the item");
	  	itemRepository.save(itemSaved);
	  	Item itemUpdated = itemRepository.findOne(itemSaved.getId());
	  	assertEquals("This is a description of the item", itemUpdated.getDescription());
	  	 
	  	//Assert Delete
	  	itemRepository.delete(itemSaved);
	  	itemRepository.delete(itemSaved2);
	    assertEquals(0, itemRepository.count());
	  }
	  
	 @Test
	  public void test_count() {
		//Assert Create
		assertEquals(0, itemRepository.count());
	  	
	    itemRepository.save(new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE));
	    itemRepository.save(new Item("video 1","http://some/path/","First video",ItemType.VIDEO));
	    itemRepository.save(new Item("video 2","http://some/youtube/path/","second Video",ItemType.VIDEO_YOUTUBE));
	    itemRepository.save(new Item("video 3","http://some/vimeo/path/","third Video",ItemType.VIDEO_VIMEO));
	  	
	    assertEquals(4, itemRepository.count());
	    assertEquals(1, itemRepository.count(ItemFilterSpecifications.isItemPicture()));
	    assertEquals(3, itemRepository.count(ItemFilterSpecifications.isItemVideo()));
	    
	    itemRepository.deleteAll();
	  }
	 
	 @Test
	  public void test_findBy_some_attribute() {
		 for(Item it : createSomeItems()) {
			 itemRepository.save(it);
		 }

		 List<Item> results = itemRepository.findByFileOrderByCreatedAtDesc("codevideo2");
		 assertEquals("codevideo2", results.get(0).getFile());
		 
		 results = itemRepository.findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc("codev");
		 assertEquals(5, results.size());
		 assertEquals("codevideo5", results.get(0).getFile());
		 assertEquals("codevideo4", results.get(1).getFile());
		 assertEquals("codevideo1", results.get(4).getFile());
		 
		 results = itemRepository.findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc("cODev");
		 assertEquals(5, results.size());
		 assertEquals("codevideo5", results.get(0).getFile());
		 assertEquals("codevideo4", results.get(1).getFile());
		 assertEquals("codevideo1", results.get(4).getFile());
		 
		 results = itemRepository.findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc(".png");
		 assertEquals(2, results.size());
		 assertEquals("picture5.png", results.get(0).getFile());
		 assertEquals("picture2.png", results.get(1).getFile());
		 
		 results = itemRepository.findByFileMyRqt("codevideo2");
		 assertEquals("codevideo2", results.get(0).getFile());
	 }
	 
	 @Test
	  public void test_filtering_on_description_field_and_fieldIgnoreCase() {
		 for(Item it : createSomeItems()) {
			 itemRepository.save(it);
		 }
		 
		 List<Item> results = itemRepository.findAll(ItemFilterSpecifications.descriptionLike("Fourth Picture"));
		 assertEquals("Fourth Picture", results.get(0).getDescription());
		 
		 results = itemRepository.findAll(ItemFilterSpecifications.descriptionLike("fOurTH pictuRe"));
		 assertEquals("Fourth Picture", results.get(0).getDescription());
		 
		 results = itemRepository.findAll(ItemFilterSpecifications.descriptionLike("Seventh"));
		 assertEquals("Seventh Picture", results.get(0).getDescription());
		 
		 results = itemRepository.findAll(ItemFilterSpecifications.descriptionLike("sevEnth"));
		 assertEquals("Seventh Picture", results.get(0).getDescription());
		 
		 results = itemRepository.findAll(ItemFilterSpecifications.descriptionLike("Picture"));
		 assertEquals(7, results.size());
		 
		 results = itemRepository.findAll(ItemFilterSpecifications.descriptionLike("picTUre"));
		 assertEquals(7, results.size());
		 
		 results = itemRepository.findAll(ItemFilterSpecifications.descriptionLike("th"));
		 assertEquals(5, results.size());
		 assertEquals("Third Picture", results.get(0).getDescription());
		 assertEquals("Fourth Picture", results.get(1).getDescription());
		 
		 results = itemRepository.findAll(ItemFilterSpecifications.descriptionLike("TH"));
		 assertEquals(5, results.size());
		 assertEquals("Third Picture", results.get(0).getDescription());
		 assertEquals("Fourth Picture", results.get(1).getDescription());
		 
	  }
	 
	 @Test
	  public void test_filtering_on_type() {
		 for(Item it : createSomeItems()) {
			 itemRepository.save(it);
		 }
		  assertEquals(12, iterableSize(itemRepository.findAll()));
		  assertEquals(7, itemRepository.findAll(ItemFilterSpecifications.isItemPicture()).size());
		  assertEquals(5, itemRepository.findAll(ItemFilterSpecifications.isItemVideo()).size());
	  }
	 
	 
	 @Test
	  public void test_filtering_on_creation_date() {
		 for(Item it : createSomeItems()) {
			 itemRepository.save(it);
		 }

		 LocalDate today = LocalDate.of(2018,4,10);
		 LocalDate lastYearJanuaryFirst = LocalDate.of(2017, 1, 1);
		 LocalDate currentYearJanuaryFirst = LocalDate.of(2018, 1, 1);
		 LocalDate currentYearSomeDaysBefore = LocalDate.of(2018, 4, 1);
		 
		 assertEquals(12, iterableSize(itemRepository.findAll()));

		 //Année précédente
		 assertEquals(4, itemRepository.findAll(ItemFilterSpecifications.periodFromTo(convertLocalDateToDate(lastYearJanuaryFirst),convertLocalDateToDate(currentYearJanuaryFirst))).size());
		 //Cette année
		 assertEquals(8, itemRepository.findAll(ItemFilterSpecifications.periodFromTo(convertLocalDateToDate(currentYearJanuaryFirst),convertLocalDateToDate(today))).size());
		 //Entré début année et 1er Avril
		 assertEquals(3, itemRepository.findAll(ItemFilterSpecifications.periodFromTo(convertLocalDateToDate(currentYearJanuaryFirst),convertLocalDateToDate(currentYearSomeDaysBefore))).size());
		 //entre 1er avvriil et aujourdhui
		 assertEquals(5, itemRepository.findAll(ItemFilterSpecifications.periodFromTo(convertLocalDateToDate(currentYearSomeDaysBefore),convertLocalDateToDate(today))).size());
		 //le 1er avril entre le 1er et le 2
		 assertEquals(1, itemRepository.findAll(ItemFilterSpecifications.periodFromTo(convertLocalDateToDate(currentYearSomeDaysBefore),convertLocalDateToDate(LocalDate.of(2018, 4, 2)))).size());
	  }
	 
	 @Test
	  public void test_filtering_on_nb_like() {
		 fail("not yet implemented");
	  }
	 
	 @Test
	  public void test_find_items_order_by_creation_date() {
		 for(Item it : createSomeItems()) {
			 itemRepository.save(it);
		 }
		 
		 List<Item> results = itemRepository.findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc("e");
		 assertEquals(12, results.size());
		 assertEquals("2018-04-05T15:11:00.225Z", Instant.ofEpochMilli(results.get(0).getCreatedAt().getTime()).toString());
		 assertEquals("2017-01-10T15:11:00.225Z", Instant.ofEpochMilli(results.get(11).getCreatedAt().getTime()).toString());
		 
		 results = WallUtils.convertIterableToList(itemRepository.findAll(new Sort(Sort.Direction.DESC,"createdAt")));
		 assertEquals(12, results.size());
		 assertEquals("2018-04-05T15:11:00.225Z", Instant.ofEpochMilli(results.get(0).getCreatedAt().getTime()).toString());
		 assertEquals("2018-04-04T15:11:00.225Z", Instant.ofEpochMilli(results.get(1).getCreatedAt().getTime()).toString());
		 assertEquals("2017-02-10T15:11:00.225Z", Instant.ofEpochMilli(results.get(10).getCreatedAt().getTime()).toString());
		 assertEquals("2017-01-10T15:11:00.225Z", Instant.ofEpochMilli(results.get(11).getCreatedAt().getTime()).toString());
		 
	  }
	 
	 @Test
	  public void test_limmit_100_item_max() {
		 for(int i=0; i<=200;i++) {
			Item it = new Item("file-"+i, "/some/path/"+i, "d-"+i, ItemType.PICTURE);
			itemRepository.save(it);
		 }
		 List<Item> results = itemRepository.findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc("file");
		 assertEquals(100, results.size());
		 assertEquals("file-200", results.get(0).getFile());
		 assertEquals("file-101", results.get(99).getFile());
		 
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
		    assertEquals("picture2.png", itemFound.get(0).getFile());
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
	  public void test_paginate_with_type_filtering() {
		  for(Item it : createSomeItems()) {
				 itemRepository.save(it);
			 }
		    assertEquals(12, itemRepository.count());
		    
		    //find by description
		    Sort sort = new Sort(new Sort.Order(Direction.DESC, "file"));
		    Pageable pageable = new PageRequest(0, 5, sort);

		    //find by images by description
		    //todo
		    fail("not yet implemented");
			
		    //find all images
		    // 2ère page de résultats ; 3 résultats max par page.
		    Page<Item> allItemPaginated = itemRepository.findAll(ItemFilterSpecifications.isItemPicture(),new PageRequest(2, 3));
		 
		    assertEquals(2, allItemPaginated.getNumber());
		    assertEquals(3, allItemPaginated.getSize()); 				// la taille de la pagination
		    assertEquals(7, allItemPaginated.getTotalElements()); 	//nb total d'éléments récupérables
		    assertEquals(3, allItemPaginated.getTotalPages()); 		// nombre de pages total
		    assertTrue(allItemPaginated.hasContent());
		    
		    //find all videos
		    // 2ère page de résultats ; 3 résultats max par page.
		    allItemPaginated = itemRepository.findAll(ItemFilterSpecifications.isItemVideo(),new PageRequest(2, 3));
		 
		    assertEquals(2, allItemPaginated.getNumber());
		    assertEquals(2, allItemPaginated.getSize()); 				// la taille de la pagination
		    assertEquals(5, allItemPaginated.getTotalElements()); 	//nb total d'éléments récupérables
		    assertEquals(2, allItemPaginated.getTotalPages()); 		// nombre de pages total
		    assertTrue(allItemPaginated.hasContent());
	  }
	  
	  public int iterableSize(Iterable<Item> it){
		  if (it instanceof Collection) {
	            return ((Collection<?>) it).size();
	        }
	        int counter = 0;
	        for (Object i : it) {
	            counter++;
	        }
	        return counter;
	  }
	  
		 public Date convertLocalDateToDate(LocalDate localDate) {
			 return 	 Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		 }
		 
	  //TODO remplacer par une BDD et liquibase ?
	  private List<Item> createSomeItems() {
		  Instant today = Instant.parse("2018-04-10T15:11:00.225Z");
		  
		  List<Item> items = new ArrayList<Item>();
		  items.add(new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE));
		  items.add(new Item("picture2.png","/some/local/folder/","Second Picture",ItemType.PICTURE));
		  items.add(new Item("codevideo1","http://youtube.com/some/path/", "Demo video 1",ItemType.VIDEO_YOUTUBE));
		  items.add(new Item("picture3","/some/local/folder/",  "Third Picture",ItemType.PICTURE));
		  items.add(new Item("codevideo2","http://youtube.com/some/path/", "Demo video 2",ItemType.VIDEO_YOUTUBE));
		  items.add(new Item("codevideo3","http://youtube.com/some/path/", "Demo video 3",ItemType.VIDEO_YOUTUBE));
		  items.add(new Item("picture4.jpg","/some/local/folder/", "Fourth Picture",ItemType.PICTURE));
		  items.add(new Item("picture5.png","/some/local/folder/","Fifth Picture",ItemType.PICTURE));
		  items.add(new Item("picture6.jpg","/some/local/folder/", "Sixth Picture",ItemType.PICTURE));
		  items.add(new Item("codevideo4","http://youtube.com/some/path/", "Demo video 4",ItemType.VIDEO_YOUTUBE));
		  items.add(new Item("picture7.jpg","/some/local/folder/", "Seventh Picture",ItemType.PICTURE));
		  items.add(new Item("codevideo5","http://youtube.com/some/path/", "Demo video 5",ItemType.VIDEO_YOUTUBE));
		  
		  //Last Year
		  items.get(0).setCreatedAt(Date.from(Instant.parse("2017-01-10T15:11:00.225Z")));
		  items.get(1).setCreatedAt(Date.from(Instant.parse("2017-02-10T15:11:00.225Z")));
		  items.get(2).setCreatedAt(Date.from(Instant.parse("2017-08-21T15:11:00.225Z")));
		  items.get(3).setCreatedAt(Date.from(Instant.parse("2017-12-29T15:11:00.225Z")));
		  //1janvier --> date fixe (2018-04-01)
		  items.get(4).setCreatedAt(Date.from(Instant.parse("2018-01-25T15:11:00.225Z")));
		  items.get(5).setCreatedAt(Date.from(Instant.parse("2018-02-10T15:11:00.225Z")));
		  items.get(6).setCreatedAt(Date.from(Instant.parse("2018-03-10T15:11:00.225Z")));
		  
		  //date fixe (2018-04-01) today (2018-04-10)
		  items.get(7).setCreatedAt(Date.from(Instant.parse("2018-04-01T15:11:00.225Z")));
		  items.get(8).setCreatedAt(Date.from(Instant.parse("2018-04-02T15:11:00.225Z")));
		  items.get(9).setCreatedAt(Date.from(Instant.parse("2018-04-03T15:11:00.225Z")));
		  items.get(10).setCreatedAt(Date.from(Instant.parse("2018-04-04T15:11:00.225Z")));
		  items.get(11).setCreatedAt(Date.from(Instant.parse("2018-04-05T15:11:00.225Z")));
		  return items;
	  }
	  
	  //toodo tri par date 
	 
}
