package com.queroevento.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.Login;
import com.queroevento.repositories.LoginRepository;

@Service
public class LoginService {

	@Autowired
	private LoginRepository loginRepository;

	public Login save(Login login) {
		return loginRepository.save(login);
	}

	public void delete(Login login) {
		loginRepository.delete(login);
	}

	public Login findOne(Long id) {
		return loginRepository.findOne(id);
	}

}