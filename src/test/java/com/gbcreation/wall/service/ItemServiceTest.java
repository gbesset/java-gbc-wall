package com.gbcreation.wall.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
import com.gbcreation.wall.repository.ItemFilterSpecifications;
import com.gbcreation.wall.repository.ItemRepository;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PrepareForTest( ItemFilterSpecifications.class)
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
		
		private Pageable pageable;
		
		@Mock
		Page page;

		@Mock
		Page pageP;
		
		@Mock
		Page pageV;
		
	    @Before
	    public void setUp() {
		    	items = generateItems();
		    	itemsP = items.stream().filter(i-> i.getType().equals(ItemType.PICTURE)).collect(Collectors.toList());
		    	itemsV = items.stream().filter(i-> i.getType().equals(ItemType.VIDEO) ||i.getType().equals(ItemType.VIDEO_YOUTUBE) || i.getType().equals(ItemType.VIDEO_VIMEO)).collect(Collectors.toList());
		    	
		    	pageable = new PageRequest(0,20);
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
	    	
	    		// Methodes static -> powerMockito
	    		Specification<Item> specPicture = ItemFilterSpecifications.isItemPicture();
			Specification<Item> specVideo = ItemFilterSpecifications.isItemVideo();	
			PowerMockito.mockStatic(ItemFilterSpecifications.class);
			PowerMockito.when(ItemFilterSpecifications.isItemPicture()).thenReturn(specPicture);
			PowerMockito.when(ItemFilterSpecifications.isItemVideo()).thenReturn(specVideo);
		
	     	when(itemRepository.count()).thenReturn(12L);
	     	when(itemRepository.count(Mockito.eq(specPicture))).thenReturn(7L);
	     	when(itemRepository.count(Mockito.eq(specVideo))).thenReturn(5L);
		   
		    	Long result = itemService.countPictures();
	
		    	assertEquals(new Long(7),result);
	
		    	verify(itemRepository).count(Mockito.eq(specPicture));
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_count_videos() {
	    	
	    		// Methodes static -> powerMockito
			Specification<Item> specPicture = ItemFilterSpecifications.isItemPicture();
			Specification<Item> specVideo = ItemFilterSpecifications.isItemVideo();	
			
			PowerMockito.mockStatic(ItemFilterSpecifications.class);
			PowerMockito.when(ItemFilterSpecifications.isItemPicture()).thenReturn(specPicture);
			PowerMockito.when(ItemFilterSpecifications.isItemVideo()).thenReturn(specVideo);
		
	     	when(itemRepository.count()).thenReturn(12L);
	     	when(itemRepository.count(Mockito.eq(specPicture))).thenReturn(7L);
	     	when(itemRepository.count(Mockito.eq(specVideo))).thenReturn(5L);
	     	
	     	
	     	Long result = itemService.countVideos();

	     	assertEquals(new Long(5),result);
	     	
	     	verify(itemRepository).count(Mockito.eq(specVideo));
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

	    		when(page.getContent()).thenReturn(Arrays.asList(item));
	    		when(itemRepository.findByFileContainingIgnoreCaseOrderByCreatedAtDesc("picture", pageable)).thenReturn(page);
		    	Page<Item> resultPaged = itemService.findByFileLike("picture", pageable);
	
		    	assertEquals(1,resultPaged.getContent().size());
		    	assertEquals(item,resultPaged.getContent().get(0));
	
		    	verify(itemRepository).findByFileContainingIgnoreCaseOrderByCreatedAtDesc("picture", pageable);
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_findBy_title_like_noResults() {
	    		when(page.getContent()).thenReturn(new ArrayList<>());
	    		when(itemRepository.findByFileContainingIgnoreCaseOrderByCreatedAtDesc("picture", pageable)).thenReturn(page);
		    	Page<Item> resultPaged = itemService.findByFileLike("picture", pageable);
	
		    	assertEquals(0,resultPaged.getContent().size());
	
		    	verify(itemRepository).findByFileContainingIgnoreCaseOrderByCreatedAtDesc("picture", pageable);
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_findBy_description_like() {
	    		Item item = new Item("test-picture-001", "/some/local/path/", "this is a beautiful picture",ItemType.PICTURE);

	    		when(page.getContent()).thenReturn(Arrays.asList(item));
	    		when(itemRepository.findByDescriptionContainingIgnoreCaseOrderByCreatedAtDesc("beautiful", pageable)).thenReturn(page);
		    	Page<Item> resultPaged = itemService.findByDescriptionLike("beautiful", pageable);
	
		    	assertEquals(1,resultPaged.getContent().size());
		    	assertEquals(item,resultPaged.getContent().get(0));
	
		    	verify(itemRepository, times(1)).findByDescriptionContainingIgnoreCaseOrderByCreatedAtDesc("beautiful", pageable);
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_findBy_description_like_noResults() {

	    		when(page.getContent()).thenReturn(new ArrayList<>());
	    		when(itemRepository.findByDescriptionContainingIgnoreCaseOrderByCreatedAtDesc("beautiful", pageable)).thenReturn(page);
		    	
	    		Page<Item> resultPaged = itemService.findByDescriptionLike("beautiful", pageable);
	
		    	assertEquals(0,resultPaged.getContent().size());
		    	verify(itemRepository, times(1)).findByDescriptionContainingIgnoreCaseOrderByCreatedAtDesc("beautiful", pageable);
		    	verifyNoMoreInteractions(itemRepository);
	    }
	    
	    @Test
	    public void test_retrieve_all_items() {
	    	 
	    	 	when(page.getContent()).thenReturn(items);
	    	 	when(pageP.getContent()).thenReturn(itemsP);
	    	 	when(pageV.getContent()).thenReturn(itemsV);
		    	when(itemRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(page);
		    	when(itemRepository.findAllByTypeInOrderByCreatedAtDesc(any(List.class), any(Pageable.class))).thenReturn(pageP);
		    	when(itemRepository.findAllByTypeInOrderByCreatedAtDesc(any(List.class), any(Pageable.class))).thenReturn(pageV);
	    	
		    	Page<Item> resultPaged = itemService.retrieveItems(pageable);
	
		    	assertEquals(resultPaged.getContent().size(),items.size());
	
		    	verify(itemRepository).findAllByOrderByCreatedAtDesc(pageable);
		    	verifyNoMoreInteractions(itemRepository);
	     }
	    
	    @Test
	    public void test_retrieve_all_pictures() {
    					
		    	when(page.getContent()).thenReturn(items);
	    	 	when(pageP.getContent()).thenReturn(itemsP);
	    	 	when(pageV.getContent()).thenReturn(itemsV);
		    	when(itemRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(page);
		    	when(itemRepository.findAllByTypeInOrderByCreatedAtDesc(Mockito.eq(Arrays.asList(ItemType.PICTURE)), any(Pageable.class))).thenReturn(pageP);
		    	when(itemRepository.findAllByTypeInOrderByCreatedAtDesc(Mockito.eq(Arrays.asList(ItemType.VIDEO_YOUTUBE)), any(Pageable.class))).thenReturn(pageV);
		    	
		    	Page<Item> resultPaged = itemService.retrievePictures(pageable);
		    	assertEquals(resultPaged.getContent().size(),7);
		    	assertEquals(resultPaged.getContent().size(),itemsP.size());
		    	assertEquals(resultPaged.getContent().get(0).getType(),ItemType.PICTURE);

		    	verify(itemRepository).findAllByTypeInOrderByCreatedAtDesc(Arrays.asList(ItemType.PICTURE), pageable);
		    	verifyNoMoreInteractions(itemRepository);
	     }
	    
	    @Test
	    public void test_retrieve_all_videos() {
	      	when(page.getContent()).thenReturn(items);
	    	 	when(pageP.getContent()).thenReturn(itemsP);
	    	 	when(pageV.getContent()).thenReturn(itemsV);
		    	when(itemRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(page);
		    	when(itemRepository.findAllByTypeInOrderByCreatedAtDesc(Mockito.eq(Arrays.asList(ItemType.PICTURE)), any(Pageable.class))).thenReturn(pageP);
		    	when(itemRepository.findAllByTypeInOrderByCreatedAtDesc(Mockito.eq(Arrays.asList(ItemType.VIDEO, ItemType.VIDEO_VIMEO, ItemType.VIDEO_YOUTUBE)), any(Pageable.class))).thenReturn(pageV);
	    	
		    	Page<Item> resultPaged = itemService.retrieveVideos(pageable);
	
		    	assertEquals(resultPaged.getContent().size(),5);
		    assertEquals(resultPaged.getContent().size(),itemsV.size());
		    	assertEquals(resultPaged.getContent().get(0).getType(),ItemType.VIDEO);
		    	assertEquals(resultPaged.getContent().get(1).getType(),ItemType.VIDEO_YOUTUBE);
		    	assertEquals(resultPaged.getContent().get(2).getType(),ItemType.VIDEO_VIMEO);
	
		    	verify(itemRepository).findAllByTypeInOrderByCreatedAtDesc(Arrays.asList(ItemType.VIDEO, ItemType.VIDEO_VIMEO, ItemType.VIDEO_YOUTUBE), pageable);
		    	verifyNoMoreInteractions(itemRepository);
	     }
	    
	    
	    //@Test
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
