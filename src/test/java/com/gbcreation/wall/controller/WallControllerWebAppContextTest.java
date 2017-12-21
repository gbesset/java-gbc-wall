package com.gbcreation.wall.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gbcreation.wall.WallApplicationTests;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.service.WallItem;
import com.gbcreation.wall.service.WallService;


//Appproche WebApppContextt (complète)
//Charger le context Spring en entier
//Crée nécessairement tous les beans et dépendances
//Valide le java et la conf

//On a déjà une classe de test qui charge l' applicationContext. Donc on va etendre cette classe

public class WallControllerWebAppContextTest extends WallApplicationTests{

	@Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
   
    @Mock
    private WallService wallService;
	  
	private  List<Item> items;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
          
		  items = new ArrayList<Item>();
		  items.add(new Item("picture1.jpg","/some/local/folder/","First Picture"));
		  items.add(new Item("picture2.jpg","/some/local/folder/","Second Picture"));
		  items.add(new Item("codevideo1","http://youtube.com/some/path/", "Demo video 1"));
		  items.add(new Item("picture3","/some/local/folder/",  "Third Picture"));
		  items.add(new Item("codevideo2","http://youtube.com/some/path/", "Demo video 2"));
		  items.add(new Item("codevideo3","http://youtube.com/some/path/", "Demo video 3"));
		  items.add(new Item("picture4.jpg","/some/local/folder/", "Fourth Picture"));
		  items.add(new Item("picture5.jpg","/some/local/folder/","Fifth Picture"));
		  items.add(new Item("picture6.jpg","/some/local/folder/", "Sixth Picture"));
		  items.add(new Item("codevideo4","http://youtube.com/some/path/", "Demo video 4"));
		  items.add(new Item("picture7.jpg","/some/local/folder/", "Seventh Picture"));
		  items.add(new Item("codevideo5","http://youtube.com/some/path/", "Demo video 5"));
		  when(wallService.retrieveAllItems()).thenReturn(items);
    }
	
    @Test
    public void test_retrieve_all_items_OK() throws Exception {
   
        mockMvc.perform(get("/api/wall/all"))
        		.andDo(print())
        		//MediaType.APPLICATION_JSON_VALUE
        		.andExpect(content().contentType("application/json;charset=UTF-8"))
        		.andExpect(status().isOk())
        		;
    }
}
