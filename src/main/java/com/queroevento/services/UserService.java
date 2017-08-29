package com.queroevento.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.UserAccount;
import com.queroevento.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public UserAccount save(UserAccount user) {
		return userRepository.save(user);
	}

	public void delete(UserAccount user) {
		userRepository.delete(user);
	}

	public UserAccount findOne(Long id) {
		return userRepository.findOne(id);
	}
}