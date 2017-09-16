package com.queroevento.services;

import javax.servlet.ServletException;

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

	public Login findByEmail(String email) {
		return loginRepository.findByEmailIgnoreCase(email);
	}
	
	public Login findByToken(String token){
		return loginRepository.findByToken(token);
	}

	public Login validateLogin(Login login) throws ServletException {

		if (login.getEmail() == null || login.getPassword() == null) {
			throw new ServletException("Username e Senha é obrigatório.");
		}

		Login existenceLogin = loginRepository.findByEmailIgnoreCase(login.getEmail());

		if (existenceLogin == null) {
			throw new ServletException("Empresa não encontrada.");
		}

		if (!login.getPassword().equals(existenceLogin.getPassword())) {
			throw new ServletException("Username ou Password inválido.");
		}

		return existenceLogin;
	}

}