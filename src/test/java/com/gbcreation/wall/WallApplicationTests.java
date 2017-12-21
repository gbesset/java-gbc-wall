package com.gbcreation.wall;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.gbcreation.wall.controller.AdminController;
import com.gbcreation.wall.controller.HomeController;
import com.gbcreation.wall.controller.WallController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WallApplicationTests {

	@Autowired
    private ApplicationContext ctx;
	
	@Test
	public void contextLoads() throws Exception {
	}
	
	@Test
	public void should_be_runnable() throws Exception {
		assertNotNull(ctx.getBean(WallController.class));
		assertNotNull(ctx.getBean(HomeController.class));
		assertNotNull(ctx.getBean(AdminController.class));
	}
}

//REMINDER
//The @SpringBootTest annotation tells Spring Boot to go and look for a main configuration class  (one with @SpringBootApplication for instance), 
//and use that to start a Spring application context.