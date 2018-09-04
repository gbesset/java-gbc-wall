package com.gbcreation.wall.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gbcreation.wall.model.User;
import com.gbcreation.wall.model.UserRole;
import com.gbcreation.wall.repository.UserRepository;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
	
	@TestConfiguration
    static class UserServiceTestContextConfiguration {
        @Bean
        public UserService UserService() {
            return new UserServiceImpl();
        }
    }
 
    @Autowired
    private UserService userService;
 
    @MockBean
    private UserRepository userRepository;
    
	private List<User> users;
	
    @Before
    public void setUp() {
	    	users = generateUsers();
    }


	@Test
    public void test_count_all() {
    		when(userRepository.count()).thenReturn(12L);
	   
    		Long result = userService.countAll();

	    	assertEquals(new Long(12),result);

	    	verify(userRepository).count();
	    	verifyNoMoreInteractions(userRepository);
    }
	
	@Test
	public void test_findBy_Login() {
		
		User User = new User(33l,"john.doe","totoPws", UserRole.ADMIN, true, null, null);

		when(userRepository.findOne(2L)).thenReturn(User);
		User result = userService.findById(2L);

		assertEquals(User,result);

		verify(userRepository).findOne(2L);
		verifyNoMoreInteractions(userRepository);
	}

	
	@Test
	public void test_login_OK_password_OK() {
		
		User user = new User(33l,"john.doe","totoPws", UserRole.ADMIN, true, null, null);
		when(userRepository.findByLogin("john.doe")).thenReturn(user);
		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		
		User result = userService.login("john.doe", "totoPws");
		
		assertNotNull(result);
		verify(userRepository).findByLogin("john.doe");
		verify(userRepository).save(user);
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void test_login_OK_password_KO() {
		
		User user = new User(33l,"john.doe","totoPws", UserRole.ADMIN, true, null, null);
		when(userRepository.findByLogin("john.doe")).thenReturn(user);
		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		
		User result = userService.login("john.doe", "badPwd");
		
		assertNull(result);
		verify(userRepository).findByLogin("john.doe");
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void test_login_KO() {
		
		User user = new User(33l,"john.doe","totoPws", UserRole.ADMIN, true, null, null);
		when(userRepository.findOne(2L)).thenReturn(null);
		
		User result = userService.login("john.doe", "password");
		
		assertNull(result);
		verify(userRepository).findByLogin("john.doe");
		verifyNoMoreInteractions(userRepository);
			
	}
	
	@Test
	public void test_add_User() {
		User u = new User("John Doe", "myPWD");
	    userService.addUser(u);

	    	verify(userRepository).save(u);
	    	verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void test_update_User() {
		User u = new User("John Doe", "myPwd");
		User uUpdated = new User("John Doe", "new Pwd");
		
		when(userRepository.save(u)).thenReturn(uUpdated);
		assertEquals(u.getPassword(),"myPwd");
		
		User result= userService.updateUser(u);

		assertEquals(result,uUpdated);
		assertEquals(result.getPassword(),"new Pwd");
    		verify(userRepository).save(u);
    		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void test_delete_User() {
		User c = new User("John Doe", "pwd1");
	    userService.deleteUser(c);

	    	verify(userRepository).delete(c);
	    	verifyNoMoreInteractions(userRepository);
	}
    
    
    private List<User> generateUsers() {
		users = new ArrayList<User>();
		users.add(new User("John Doe", "pwd1"));
		users.add(new User("Jane Doe", "myPwd"));
		users.add(new User("Theodore Handle", "wow"));
		users.add(new User("Guy Mann", "blabla"));
		users.add(new User("Eleanor.Fant", "maybe22"));
		
		return users;
	}
}
