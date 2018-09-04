package com.gbcreation.wall.repository;

import org.springframework.data.repository.CrudRepository;

import com.gbcreation.wall.model.User;

public interface UserRepository extends  CrudRepository<User, Long>{
	
	User findByLogin(String login);
	
}
