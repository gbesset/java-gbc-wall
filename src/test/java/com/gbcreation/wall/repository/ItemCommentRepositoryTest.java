package com.gbcreation.wall.repository;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;
import com.gbcreation.wall.util.WallUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemCommentRepositoryTest {
	
	 @Autowired
	  private ItemRepository itemRepository;
	 
	 @Autowired
	  private CommentRepository commentRepository;
	 
	 private Item item1;
	 
	 private Item item2;
	 
	 private Item item3;
	 
	 @Before
	 public void setup() {
		 commentRepository.deleteAll();
		 
		 item1 = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
		 itemRepository.save(item1);
		 item2 = new Item("picture2.jpg","/some/local/folder/","Nice travel.. hope being there",ItemType.PICTURE);
		 itemRepository.save(item2);
		 item3 = new Item("picture3.jpg","/some/local/folder/","Another great Picture",ItemType.PICTURE);
		 itemRepository.save(item3);
		 assertEquals(3,itemRepository.count());
		 
		 for(Comment com : createSomeComments()) {
			 commentRepository.save(com);
		 }
	  }

	 
	 @Test
	  public void test_retrieve_items_with_comments() {
		 List<Item> items = WallUtils.convertIterableToList(itemRepository.findAll());
		 assertEquals(3,items.spliterator().getExactSizeIfKnown());
		 
		 assertEquals("picture1.jpg", items.get(0).getFile());
		 assertEquals(10, commentRepository.countByItemId(items.get(0)));
		 assertEquals(" no ideas comm13.1 love it!", commentRepository.findByItemIdOrderByCreatedAtDesc(items.get(0)).get(0).getComment());
		 assertEquals(" country - comm1 love it!", commentRepository.findByItemIdOrderByCreatedAtDesc(items.get(0)).get(9).getComment());
		 
		 assertEquals("picture2.jpg", items.get(1).getFile());
		 assertEquals(0, commentRepository.countByItemId(items.get(1)));
		 
		 assertEquals("picture3.jpg", items.get(2).getFile());
		 assertEquals(5, commentRepository.countByItemId(items.get(2)));
		 assertEquals(" great comm15.2 love it!",commentRepository.findByItemIdOrderByCreatedAtDesc(items.get(2)).get(0).getComment());
		 assertEquals(" POOL - comm7.2 love it!",commentRepository.findByItemIdOrderByCreatedAtDesc(items.get(2)).get(4).getComment());
	  }


	private  List<Comment> createSomeComments() {
		  
		  List<Comment> comments = new ArrayList<Comment>();
		  comments.add(new Comment("Author1", " country - comm1 love it!", item1));
		  comments.add(new Comment("Author2", " mountain - comm2 love it!", item1));
		  comments.add(new Comment("Author3", " country - comm3 love it!", item1));
		  comments.add(new Comment("Author1", " pool- comm4 love it!", item1));
		  comments.add(new Comment("Author4", " sand and boat  comm5 love it!", item1));
		  comments.add(new Comment("Author3", " coUNtry - comm6 love it!", item1));
		  comments.add(new Comment("Author2", " POOL - comm7.2 love it!", item3));
		  comments.add(new Comment("Author1", " MouNTain comm8.1 love it!", item1));
		  comments.add(new Comment("Author1", " nice comm9.1 love it!", item1));
		  comments.add(new Comment("Author4", " ok comm10.2 love it!", item3));
		  comments.add(new Comment("Author4", " done comm11.2 love it!", item3));
		  comments.add(new Comment("Au2thor", " what to say comm12.1 love it!", item1));
		  comments.add(new Comment("Author1", " no ideas comm13.1 love it!", item1));
		  comments.add(new Comment("2Author", " almost done comm14.2 love it!", item3));
		  comments.add(new Comment("Author1", " great comm15.2 love it!", item3));
		  
		  //Last Year
		  comments.get(0).setCreatedAt(Date.from(Instant.parse("2017-01-10T15:11:00.225Z")));
		  comments.get(1).setCreatedAt(Date.from(Instant.parse("2017-02-10T15:11:00.225Z")));
		  comments.get(2).setCreatedAt(Date.from(Instant.parse("2017-08-21T15:11:00.225Z")));
		  comments.get(3).setCreatedAt(Date.from(Instant.parse("2017-12-29T15:11:00.225Z")));
		  //1janvier --> date fixe (2018-04-01)
		  comments.get(4).setCreatedAt(Date.from(Instant.parse("2018-01-25T15:11:00.225Z")));
		  comments.get(5).setCreatedAt(Date.from(Instant.parse("2018-02-10T15:11:00.225Z")));
		  comments.get(6).setCreatedAt(Date.from(Instant.parse("2018-03-10T15:11:00.225Z")));
		  
		  //date fixe (2018-04-01) today (2018-04-10)
		  comments.get(7).setCreatedAt(Date.from(Instant.parse("2018-04-01T15:11:00.225Z")));
		  comments.get(8).setCreatedAt(Date.from(Instant.parse("2018-04-02T15:11:00.225Z")));
		  comments.get(9).setCreatedAt(Date.from(Instant.parse("2018-04-03T15:11:00.225Z")));
		  comments.get(10).setCreatedAt(Date.from(Instant.parse("2018-04-04T15:11:00.225Z")));
		  comments.get(11).setCreatedAt(Date.from(Instant.parse("2018-04-05T15:11:00.225Z")));
		  comments.get(12).setCreatedAt(Date.from(Instant.parse("2018-04-06T15:11:00.225Z")));
		  comments.get(13).setCreatedAt(Date.from(Instant.parse("2018-04-07T15:11:00.225Z")));
		  comments.get(14).setCreatedAt(Date.from(Instant.parse("2018-04-08T15:11:00.225Z")));
		  
		  return comments; 
	}
}
