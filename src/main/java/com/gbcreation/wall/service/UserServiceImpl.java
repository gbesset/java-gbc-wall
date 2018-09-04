package com.gbcreation.wall.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gbcreation.wall.model.User;
import com.gbcreation.wall.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Resource
	private UserRepository userRepository;
	
	@Override
	public long countAll() {
		return userRepository.count();
	}

	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User findByLogin(String login) {
		return userRepository.findByLogin(login);
	}

	@Override
	public User addUser(User u) {
		return userRepository.save(u);
	}

	@Override
	public User updateUser(User u) {
		return userRepository.save(u);
	}

	@Override
	public void deleteUser(User u) {
		 userRepository.delete(u);
		
	}

	@Override
	public User login(String login, String password) {
		User u = userRepository.findByLogin(login);
		if(u != null) {
			if(u.getPassword().equals(password)) {
				u.setLastConnection(new Date());
				this.updateUser(u);
				return u;
			}
		}
		return null;
	}

}
