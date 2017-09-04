package com.queroevento.services;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.Login;
import com.queroevento.models.User;
import com.queroevento.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginService loginService;

	public User save(User user) {
		return userRepository.save(user);
	}

	public User findOne(Long id) {
		return userRepository.findOne(id);
	}

	public User findByToken(String token) throws ServletException {

		String formattedToken = token.substring(7);

		Login login = loginService.findByToken(formattedToken);

		if (login == null) {
			throw new ServletException("Token não existente.");
		}

		return login.getUser();
	}
}