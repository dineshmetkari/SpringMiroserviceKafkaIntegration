package com.stackroute.messaging.service;

import java.util.List;

import com.stackroute.messaging.model.User;


/**
 * UserService
 *
 * @author Dinesh Metkari (dineshmetkari@gmail.com)
 */
public interface UserService {
	
	User findById(long id);
	
	User findByName(String name);
	
	void saveUser(User user);
	
	void updateUser(User user);
	
	void deleteUserById(long id);

	List<User> findAllUsers(); 
	
	void deleteAllUsers();
	
	public boolean isUserExist(User user);
	
}
