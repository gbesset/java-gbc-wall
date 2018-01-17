package com.gbcreation.wall.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
import com.gbcreation.wall.repository.ItemFilterSpecifications;
import com.gbcreation.wall.repository.ItemRepository;

@RunWith(SpringRunner.class)
public class ItemServiceTest {

	 @TestConfiguration
	    static class ItemServiceTestContextConfiguration {
	  
	        @Bean
	        public ItemService itemService() {
	            return new ItemServiceImpl();
	        }
	    }
	 
	    @Autowired
	    private ItemService itemService;
	 
	    @MockBean
	    private ItemRepository itemRepository;
	    
		private List<Item> items;

		private List<Item> itemsP;

		private List<Item> itemsV;
	    
	    @Before
	    public void setUp() {
		    	items = generateItems();
		    	itemsP = items.stream().filter(i-> i.getType().equals(ItemType.PICTURE)).collect(Collectors.toList());
		    	itemsV = items.stream().filter(i-> i.getType().equals(ItemType.VIDEO) ||i.getType().equals(ItemType.VIDEO_YOUTUBE) || i.getType().equals(ItemType.VIDEO_VIMEO)).collect(Collectors.toList());
	    }

	    
	    @Test
	    public void test_count_all() {
	    		when(itemRepository.count()).thenReturn(12L);
		   
	    		Long result = itemService.countAll();
	
		    	assertEquals(new Long(12),result);
	
		    	verify(itemRepository).count();
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_count_pictures() {
	    	
	    		given(itemRepository.count()).willReturn(12L);
	    		given(itemRepository.count(Mockito.eq(ItemFilterSpecifications.isItemPicture()))).willReturn(7L);
	    		given(itemRepository.count(Mockito.eq(ItemFilterSpecifications.isItemVideo()))).willReturn(5L);
		    	
		    	//when(itemRepository.count()).thenReturn(12L);
		    	//when(itemRepository.count(Mockito.eq(ItemFilterSpecifications.isItemPicture()))).thenReturn(7L);
		    	//when(itemRepository.count(Mockito.eq(ItemFilterSpecifications.isItemVideo()))).thenReturn(5L);
		   
		    	Long result = itemService.countPictures();
	
		    	//Pb static method.... a mocker ou ...?
		    	//fail("!! je n'arrive pas a mock les Specifications du repository.. (mes filtres isVideo, isPhoto, date)");
		    	assertEquals(new Long(7),result);
	
		    	verify(itemRepository).count(ItemFilterSpecifications.isItemPicture());
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_count_videos() {
	     	when(itemRepository.count()).thenReturn(12L);
	     	when(itemRepository.count(ItemFilterSpecifications.isItemPicture())).thenReturn(7L);
	     	when(itemRepository.count(Mockito.eq(ItemFilterSpecifications.isItemVideo()))).thenReturn(5L);
	     	
	     	
	     	Long result = itemService.countVideos();

	     	fail("!! je n'arrive pas a mock les Specifications du repository.. (mes filtres isVideo, isPhoto, date)");
	     	assertEquals(new Long(5),result);
	     	
	     	verify(itemRepository).count(ItemFilterSpecifications.isItemVideo());
	     	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_findBy_id() {
	    		Item item = new Item("test-picture-001", "/some/local/path/", "this is a beautiful picture",ItemType.PICTURE);

	    		when(itemRepository.findOne(2L)).thenReturn(item);
		    	Item result = itemService.findById(2L);
	
		    	assertEquals(item,result);
	
		    	verify(itemRepository).findOne(2L);
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_findBy_title() {
	    		Item item = new Item("test-picture-001", "/some/local/path/", "this is a beautiful picture",ItemType.PICTURE);

	    		when(itemRepository.findByFileOrderByCreatedAtDesc("test-picture-001")).thenReturn(Arrays.asList(item));
		    	List<Item> result = itemService.findByFile("test-picture-001");
	
		    	assertEquals(1,result.size());
		    	assertEquals(item,result.get(0));
	
		    	verify(itemRepository).findByFileOrderByCreatedAtDesc("test-picture-001");
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_findBy_title_like() {
	    		Item item = new Item("test-picture-001", "/some/local/path/", "this is a beautiful picture",ItemType.PICTURE);

	    		when(itemRepository.findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc("picture")).thenReturn(Arrays.asList(item));
		    	List<Item> result = itemService.findByFileLike("picture");
	
		    	assertEquals(1,result.size());
		    	assertEquals(item,result.get(0));
	
		    	verify(itemRepository).findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc("picture");
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_findBy_title_like_noResults() {
	    		when(itemRepository.findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc(Mockito.anyString())).thenReturn(new ArrayList<>());
		    	List<Item> result = itemService.findByFileLike("picture");
	
		    	assertEquals(0,result.size());
	
		    	verify(itemRepository).findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc("picture");
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_findBy_description_like() {
	    		Item item = new Item("test-picture-001", "/some/local/path/", "this is a beautiful picture",ItemType.PICTURE);

	    		when(itemRepository.findAll(ItemFilterSpecifications.descriptionLike("beautiful"))).thenReturn(Arrays.asList(item));
		    	List<Item> result = itemService.findByFileLike("beautiful");
	
		    	fail("!! je n'arrive pas a mock les Specifications du repository.. (mes filtres isVideo, isPhoto, date)");
		    	assertEquals(1,result.size());
		    	assertEquals(item,result.get(0));
	
		    	verify(itemRepository).findAll(ItemFilterSpecifications.descriptionLike("beautiful"));
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_findBy_description_like_noResults() {

	    		when(itemRepository.findAll(ItemFilterSpecifications.descriptionLike("beautiful"))).thenReturn(new ArrayList<>());
		    	
	    		List<Item> result = itemService.findByFileLike("beautiful");
	
		    	assertEquals(0,result.size());
		    	fail("!! je n'arrive pas a mock les Specifications du repository.. (mes filtres isVideo, isPhoto, date)");
		    	verify(itemRepository).findAll(ItemFilterSpecifications.descriptionLike("beautiful"));
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_retrieve_all_items() {
	    	 	ArgumentCaptor<Sort> sortArgument = ArgumentCaptor.forClass(Sort.class);
	    	 
		    	when(itemRepository.findAll(sortArgument.capture())).thenReturn(items);
		    	when(itemRepository.findAll(ItemFilterSpecifications.isItemPicture(),sortArgument.capture())).thenReturn(itemsP);
		    	when(itemRepository.findAll(ItemFilterSpecifications.isItemVideo(),sortArgument.capture())).thenReturn(itemsV);
	    	
		    	List<Item> result = itemService.retrieveAllItems();
	
		    	assertEquals(result.size(),items.size());
	
		    	verify(itemRepository).findAll(sortArgument.capture());
		    	verifyNoMoreInteractions(itemRepository);
	     }
	    
	    @Test
	    public void test_retrieve_all_pictures() {
	    		when(itemRepository.findAll()).thenReturn(items);
		    	when(itemRepository.findAll(ItemFilterSpecifications.isItemPicture())).thenReturn(itemsP);
		    	when(itemRepository.findAll(ItemFilterSpecifications.isItemVideo())).thenReturn(itemsV);
		    	
		    	List<Item> result = itemService.retrieveAllPictures();
	
		    	fail("!! je n'arrive pas a mock les Specifications du repository.. (mes filtres isVideo, isPhoto, date)");
		    	assertEquals(result.size(),7);
		    	assertEquals(result.size(),itemsP.size());
		    	assertEquals(result.get(0).getType(),ItemType.PICTURE);
	
		    	verify(itemRepository).findAll();
		    	verifyNoMoreInteractions(itemRepository);
	     }
	    
	    @Test
	    public void test_retrieve_all_videos() {
		    	when(itemRepository.findAll()).thenReturn(items);
		    	when(itemRepository.findAll(ItemFilterSpecifications.isItemPicture())).thenReturn(itemsP);
		    	when(itemRepository.findAll(ItemFilterSpecifications.isItemVideo())).thenReturn(itemsV);
	    	
		    	List<Item> result = itemService.retrieveAllVideos();
	
		    	fail("!! je n'arrive pas a mock les Specifications du repository.. (mes filtres isVideo, isPhoto, date)");
		    	assertEquals(result.size(),5);
		    assertEquals(result.size(),itemsV.size());
		    	assertEquals(result.get(0).getType(),ItemType.VIDEO);
		    	assertEquals(result.get(1).getType(),ItemType.VIDEO_YOUTUBE);
		    	assertEquals(result.get(2).getType(),ItemType.VIDEO_VIMEO);
	
		    	verify(itemRepository).findAll();
		    	verifyNoMoreInteractions(itemRepository);
	     }
	    
	    
	    @Test
	    public void test_retrieve_all_from_to() {
			fail("Not yet implemented. to do once filer mocked....");
		}

	    @Test
	    public void test_add_item() {
	    		Item item = new Item("test-picture-001", "/some/local/path/", "this is a beautiful picture",ItemType.PICTURE);	
		    itemService.addItem(item);
	
		    	verify(itemRepository).save(item);
		    	verifyNoMoreInteractions(itemRepository);
	    }

	    @Test
	    public void test_update_item() {
	    		Item item = new Item("test-picture-001", "/some/local/path/", "this is a beautiful picture",ItemType.PICTURE);
	    		Item itemUpdated = new Item("test-picture-001", "/some/local/path/", "this is a beautiful picture that has been modified",ItemType.PICTURE);
	    		Date modifiedDate = new Date();
	    		itemUpdated.setUpdatedAt(modifiedDate);
	    		
	    		when(itemRepository.save(item)).thenReturn(itemUpdated);
	    		Item result= itemService.updateItem(item);
	
	    		assertEquals(result,itemUpdated);
	    		assertEquals(item.getUpdatedAt(),null);
	    		assertEquals(result.getUpdatedAt(),modifiedDate);
		    	verify(itemRepository).save(item);
		    	verifyNoMoreInteractions(itemRepository);
	    }

	    @Test
	    public void test_delete_item() {
	    		Item item = new Item("test-picture-001", "/some/local/path/", "this is a beautiful picture",ItemType.PICTURE);	
		    itemService.deleteItem(item);
	
		    	verify(itemRepository).delete(item);
		    	verifyNoMoreInteractions(itemRepository);
	    }		    	
	    
	    private List<Item> generateItems() {
		    	items = new ArrayList<Item>();
		    	items.add(new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE));
		    	items.add(new Item("picture2.jpg","/some/local/folder/","Second Picture",ItemType.PICTURE));
		    	items.add(new Item("codevideo1","http://youtube.com/some/path/", "Demo video 1",ItemType.VIDEO));
		    	items.add(new Item("picture3.jpg","/some/local/folder/",  "Third Picture",ItemType.PICTURE));
		    	items.add(new Item("codevideo2","http://youtube.com/some/path/", "Demo video 2",ItemType.VIDEO_YOUTUBE));
		    	items.add(new Item("codevideo3","http://youtube.com/some/path/", "Demo video 3",ItemType.VIDEO_VIMEO));
		    	items.add(new Item("picture4.jpg","/some/local/folder/", "Fourth Picture",ItemType.PICTURE));
		    	items.add(new Item("picture5.jpg","/some/local/folder/","Fifth Picture",ItemType.PICTURE));
		    	items.add(new Item("picture6.jpg","/some/local/folder/", "Sixth Picture",ItemType.PICTURE));
		    	items.add(new Item("codevideo4","http://youtube.com/some/path/", "Demo video 4",ItemType.VIDEO_YOUTUBE));
		    	items.add(new Item("picture7.jpg","/some/local/folder/", "Seventh Picture",ItemType.PICTURE));
		    	items.add(new Item("codevideo5","http://youtube.com/some/path/", "Demo video 5",ItemType.VIDEO_YOUTUBE));
		    	return items;
	    }
	    
}
