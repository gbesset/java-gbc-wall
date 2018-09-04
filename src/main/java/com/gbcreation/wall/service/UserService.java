package com.gbcreation.wall.service;

import com.gbcreation.wall.model.User;

public interface UserService {
	
	long countAll();
	User findById(Long id);
	User findByLogin(String login);
	
	User login(String login, String password);
	
	
	//Pour l'administration
	User addUser(User u);
	User updateUser(User u);
	void deleteUser(User u);
}
