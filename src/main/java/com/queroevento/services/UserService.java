package com.queroevento.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.User;
import com.queroevento.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}

	public void delete(User user) {
		userRepository.delete(user);
	}

	public User findOne(Long id) {
		return userRepository.findOne(id);
	}
}