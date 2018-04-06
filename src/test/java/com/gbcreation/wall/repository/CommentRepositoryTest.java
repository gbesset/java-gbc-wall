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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {
	
	 @Autowired
	  private ItemRepository itemRepository;
	 
	 @Autowired
	  private CommentRepository commentRepository;
	 
	 private Item item1;
	 
	 private Item item2;
	 
	 private Pageable pageable;
	 
	 @Before
	 public void setup() {
		 pageable = new PageRequest(0,20);;
		 
		 commentRepository.deleteAll();
		 
		 item1 = new Item("picture1.jpg","/some/local/folder/","First Picture",ItemType.PICTURE);
		 itemRepository.save(item1);
		 item2 = new Item("picture2.jpg","/some/local/folder/","Another great Picture",ItemType.PICTURE);
		 itemRepository.save(item2);
		 assertEquals(2,itemRepository.count());
	  }

	 
	 @Test
	  public void test_save_and_update_item() {
	    Comment comment = new Comment("Author1", "First Comm !!!", item1);
	    commentRepository.save(comment);
	    assertEquals(1,commentRepository.count());
	    
	    comment.setComment("ok, updated");
	    commentRepository.save(comment);
	    Comment c2 = commentRepository.findOne(comment.getId());
	    assertEquals("ok, updated", c2.getComment());
	    
	  }
	 
	 @Test
	  public void test_crud() {
		//Assert Create
		assertEquals(0, commentRepository.count());
	  	
		Comment commentSaved = commentRepository.save(new Comment("Author #1", "This is a great and long com. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ullamcorper sed metus in gravida. Sed eget purus id nunc aliquet vulputate. Donec nibh mi, consectetur ac nulla sed, ullamcorper pretium elit. Ut vel purus sed sapien molestie elementum eu sed metus. Pellentesque ultricies nibh vel ligula volutpat rutrum. Phasellus semper molestie placerat. Donec quis orci egestas, tempus ipsum vel, congue justo. Aliquam ut ante pretium, aliquam eros id, accumsan mi. Sed justo quam, mattis vel dignissim at, cursus quis tellus. Phasellus elementum orci velit, in dignissim risus porttitor a. Suspendisse varius porta dui at condimentum. Duis vitae nunc vitae est sollicitudin ullamcorper nec at eros. Nulla facilisi. Suspendisse semper libero ac ligula ultricies, sollicitudin feugiat eros rutrum.\n" + 
				"\n" + 
				"Proin fermentum eros sed tristique imperdiet. Ut nec tincidunt dui. Donec tristique nisl posuere auctor tincidunt. Nam nisl nunc, pellentesque lacinia mi nec, tempor vehicula erat. Maecenas vehicula risus in odio mollis, non rutrum velit sagittis. Curabitur imperdiet feugiat ipsum sit amet cursus. Praesent risus felis, tincidunt at purus eu, vulputate sagittis leo. Vivamus faucibus nisi neque, sit amet mollis magna vestibulum non. Nulla ac turpis justo. Fusce sollicitudin ipsum nec sapien molestie, a rhoncus eros pulvinar. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin lobortis auctor turpis, eget commodo est consequat in.\n" + 
				"\n" + 
				"Fusce mi velit, aliquet nec posuere quis, suscipit vitae urna. Ut blandit vulputate semper. Donec sagittis condimentum ullamcorper. Curabitur quis porttitor quam. Aliquam et nisi porta, consectetur ligula vel, euismod nunc. Morbi ac magna tellus. Duis blandit, sem in semper viverra, dui purus semper ligula, eu suscipit orci urna a est. Vivamus vehicula mauris vel ex dignissim, vitae faucibus ligula suscipit. Praesent viverra consectetur pharetra.\n" + 
				"\n" + 
				"Mauris tincidunt massa mauris, malesuada auctor lorem egestas iaculis. Morbi id lorem scelerisque, commodo elit et, dignissim ante. Curabitur tempor nulla in neque fermentum, vel facilisis mauris feugiat. Phasellus vel ex sed sapien dictum tristique. Suspendisse eu venenatis dolor, porta commodo sapien. Vestibulum leo massa, elementum hendrerit imperdiet eget, tincidunt sed ex. Cras erat risus, dignissim in imperdiet sit amet, eleifend eu mi. Quisque a mi in leo pellentesque euismod et ac justo. In hac habitasse platea dictumst. Quisque porttitor cursus leo eget consectetur. Vestibulum vulputate massa a leo molestie, sit amet eleifend diam fringilla.\n" + 
				"\n" + 
				"Aenean convallis, mauris et mattis aliquam, nulla ante venenatis velit, ac facilisis metus massa sed metus. Duis vel lorem auctor orci feugiat fringilla. Fusce ultricies malesuada nulla at accumsan. Quisque quis efficitur sem, quis porttitor tellus. Sed at condimentum nunc. Etiam maximus ultricies quam, ut euismod libero aliquet id. Phasellus accumsan ultrices ultrices. Nulla nisi purus, tempus vitae tortor sed, bibendum gravida magna.\n" + 
				"\n" + 
				"Mauris eu bibendum nulla. Morbi eget tristique nunc, eu gravida tellus. Donec gravida vel mauris vehicula dapibus. Nunc aliquet, ipsum ac pharetra vehicula, nisi erat feugiat libero, at lobortis libero lectus maximus nisl. Etiam finibus magna justo, ac gravida nisl faucibus sit amet. Nam enim quam, laoreet quis urna sed, luctus convallis erat. Fusce ultrices, dolor ut commodo semper, sem nibh blandit.\n" + 
				"\n" + 
				"", item1));
	  	assertEquals(1, commentRepository.count());
	    
	  	assertEquals(true, commentRepository.exists(commentSaved.getId()));
	  	
	    //Assert Find One and All
	  	assertEquals(commentSaved, commentRepository.findOne(commentSaved.getId()));
	  	
	  	Comment commentSaved2 = commentRepository.save(new Comment("Author2", "another comments for the same picture !!!", item1));
	  	assertEquals(2, commentRepository.count());
	  	assertEquals(2, commentRepository.findAll().spliterator().getExactSizeIfKnown());
	  	
	  	//Assert Update
	  	commentSaved2.setComment("This is a new comment for the picture.");
	  	commentRepository.save(commentSaved2);
	  	Comment aCom = commentRepository.findOne(commentSaved2.getId());
	  	assertEquals("This is a new comment for the picture.", aCom.getComment());
	  	 
	  	//Assert Delete
	  	commentRepository.delete(commentSaved);
	  	commentRepository.delete(commentSaved2);
	    assertEquals(0, commentRepository.count());
	  }
	  
	 @Test
	  public void test_count() {
		//Assert Create
		assertEquals(0, commentRepository.count());
	  	
		commentRepository.save(new Comment("Author2", " great picture 1 love it!", item1));
		commentRepository.save(new Comment("Author3", "where is picture 1 taken?", item1));
		commentRepository.save(new Comment("Author1", "I guess in australia", item1));
		commentRepository.save(new Comment("Author1", "picture 2 is great", item2));
	  	
	    assertEquals(4, commentRepository.count());
	    assertEquals(3, commentRepository.countByItemId(item1));
	    assertEquals(3, commentRepository.countByItemIdId(item1.getId()));
	    assertEquals(1, commentRepository.countByItemId(item2));
	    assertEquals(1, commentRepository.countByItemIdId(item2.getId()));
	    
	    commentRepository.deleteAll();
	    
	  }
	 
	  @Test
	  public void test_retrieve_comments_from_itemId() {
		 for(Comment com : createSomeComments()) {
			 commentRepository.save(com);
		 }
		 
		 assertEquals(15,commentRepository.count());
		 
		 List<Comment> results = commentRepository.findByItemIdOrderByCreatedAtDesc(item1);
		 assertEquals(10, results.size());
		 assertEquals(" no ideas comm13.1 love it!", results.get(0).getComment());
		 assertEquals(" country - comm1 love it!", results.get(9).getComment());
		  
		 Item item3 = new Item("new file","a path", "a description", ItemType.PICTURE);
		 itemRepository.save(item3);
		 results = commentRepository.findByItemIdOrderByCreatedAtDesc(item3);
		 assertEquals(0, results.size());
		 
		 results = commentRepository.findByItemIdOrderByCreatedAtDesc(item2);
		 assertEquals(5, results.size());
		 assertEquals(" great comm15.2 love it!", results.get(0).getComment());
		 assertEquals(" POOL - comm7.2 love it!", results.get(4).getComment());
	  }
	  
	  @Test
	  public void test_retrieve_comments_from_itemId_id() {
		 for(Comment com : createSomeComments()) {
			 commentRepository.save(com);
		 }
		 
		 assertEquals(15,commentRepository.count());
		 
		 Page<Comment> resultsPaged = commentRepository.findByItemIdIdOrderByCreatedAtDesc(item1.getId(),pageable);
		 assertEquals(10, resultsPaged.getContent().size());
		 assertEquals(" no ideas comm13.1 love it!", resultsPaged.getContent().get(0).getComment());
		 assertEquals(" country - comm1 love it!", resultsPaged.getContent().get(9).getComment());
		  
		 resultsPaged = commentRepository.findByItemIdIdOrderByCreatedAtDesc(43l,pageable);
		 assertEquals(0, resultsPaged.getContent().size());
		 
		 resultsPaged = commentRepository.findByItemIdIdOrderByCreatedAtDesc(item2.getId(),pageable);
		 assertEquals(5, resultsPaged.getContent().size());
		 assertEquals(" great comm15.2 love it!", resultsPaged.getContent().get(0).getComment());
		 assertEquals(" POOL - comm7.2 love it!", resultsPaged.getContent().get(4).getComment());
		
	  }
	 
	 @Test
	  public void test_filtering_on_comment_field_and_fieldIgnoreCase() {
		 for(Comment com : createSomeComments()) {
			 commentRepository.save(com);
		 }
		 
		 assertEquals(15,commentRepository.count());
		 
		 Page<Comment> resultsPaged = commentRepository.findByCommentContainingIgnoreCaseOrderByCreatedAtDesc("comm7.2", pageable);
		 assertEquals(" POOL - comm7.2 love it!", resultsPaged.getContent().get(0).getComment());
		 
		 resultsPaged = commentRepository.findByCommentContainingIgnoreCaseOrderByCreatedAtDesc("cOMm7.2", pageable);
		 assertEquals(" POOL - comm7.2 love it!", resultsPaged.getContent().get(0).getComment());
		 
		 resultsPaged = commentRepository.findByCommentContainingIgnoreCaseOrderByCreatedAtDesc("country", pageable);
		 assertEquals(3, resultsPaged.getContent().size());
		 assertEquals(" coUNtry - comm6 love it!", resultsPaged.getContent().get(0).getComment());
		 assertEquals(" country - comm3 love it!", resultsPaged.getContent().get(1).getComment());
		 assertEquals(" country - comm1 love it!", resultsPaged.getContent().get(2).getComment());
		 
		 resultsPaged = commentRepository.findByCommentContainingIgnoreCaseOrderByCreatedAtDesc("CounTRy", pageable);
		 assertEquals(3, resultsPaged.getContent().size());
		 assertEquals(" coUNtry - comm6 love it!", resultsPaged.getContent().get(0).getComment());
		 assertEquals(" country - comm3 love it!", resultsPaged.getContent().get(1).getComment());
		 assertEquals(" country - comm1 love it!", resultsPaged.getContent().get(2).getComment());
		 
		 resultsPaged = commentRepository.findByCommentContainingIgnoreCaseOrderByCreatedAtDesc(".2", pageable);
		 assertEquals(5, resultsPaged.getContent().size());
		 assertEquals(" great comm15.2 love it!", resultsPaged.getContent().get(0).getComment());
		 assertEquals(" almost done comm14.2 love it!", resultsPaged.getContent().get(1).getComment());
		 assertEquals(" done comm11.2 love it!", resultsPaged.getContent().get(2).getComment());
		 assertEquals(" ok comm10.2 love it!", resultsPaged.getContent().get(3).getComment());
		 assertEquals(" POOL - comm7.2 love it!", resultsPaged.getContent().get(4).getComment());
	  }
	 
	 @Test
	  public void test_filtering_on_author_field_and_fieldIgnoreCase() {
		 for(Comment com : createSomeComments()) {
			 commentRepository.save(com);
		 }
		 
		 assertEquals(15,commentRepository.count());
		 
		 Page<Comment> resultsPaged = commentRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc("Author3", pageable);
		 assertEquals(2, resultsPaged.getContent().size());
		 assertEquals(" coUNtry - comm6 love it!", resultsPaged.getContent().get(0).getComment());
		 assertEquals(" country - comm3 love it!", resultsPaged.getContent().get(1).getComment());
		 
		 
		 resultsPaged = commentRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc("thor", pageable);
		 assertEquals(15, resultsPaged.getContent().size());
		 
		 resultsPaged = commentRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc("tHOr", pageable);
		 assertEquals(15, resultsPaged.getContent().size());
		 
		 resultsPaged = commentRepository.findByAuthorContainingIgnoreCaseOrderByCreatedAtDesc("2", pageable);
		 assertEquals(4, resultsPaged.getContent().size());
		 assertEquals(" almost done comm14.2 love it!", resultsPaged.getContent().get(0).getComment());
		 assertEquals(" what to say comm12.1 love it!", resultsPaged.getContent().get(1).getComment());
		 assertEquals(" POOL - comm7.2 love it!", resultsPaged.getContent().get(2).getComment());
		 assertEquals(" mountain - comm2 love it!", resultsPaged.getContent().get(3).getComment());
	  }


	private  List<Comment> createSomeComments() {
		 Instant today = Instant.parse("2018-04-10T15:11:00.225Z");
		  
		  List<Comment> comments = new ArrayList<Comment>();
		  comments.add(new Comment("Author1", " country - comm1 love it!", item1));
		  comments.add(new Comment("Author2", " mountain - comm2 love it!", item1));
		  comments.add(new Comment("Author3", " country - comm3 love it!", item1));
		  comments.add(new Comment("Author1", " pool- comm4 love it!", item1));
		  comments.add(new Comment("Author4", " sand and boat  comm5 love it!", item1));
		  comments.add(new Comment("Author3", " coUNtry - comm6 love it!", item1));
		  comments.add(new Comment("Author2", " POOL - comm7.2 love it!", item2));
		  comments.add(new Comment("Author1", " MouNTain comm8.1 love it!", item1));
		  comments.add(new Comment("Author1", " nice comm9.1 love it!", item1));
		  comments.add(new Comment("Author4", " ok comm10.2 love it!", item2));
		  comments.add(new Comment("Author4", " done comm11.2 love it!", item2));
		  comments.add(new Comment("Au2thor", " what to say comm12.1 love it!", item1));
		  comments.add(new Comment("Author1", " no ideas comm13.1 love it!", item1));
		  comments.add(new Comment("2Author", " almost done comm14.2 love it!", item2));
		  comments.add(new Comment("Author1", " great comm15.2 love it!", item2));
		  
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
