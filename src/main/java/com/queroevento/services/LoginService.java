package com.queroevento.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.Login;
import com.queroevento.repositories.LoginRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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

	public Login authenticateLogin(Login login) {

		if (login.getExpirationTokenDate() != null && login.getExpirationTokenDate().after(new Date())) {

			return login;

		} else if (login.getExpirationTokenDate() == null || login.getExpirationTokenDate().before(new Date())) {

			Date expirationDate = new Date(System.currentTimeMillis() + 240 * 60 * 1000);

			String token = Jwts.builder().setSubject(login.getEmail())
					.signWith(SignatureAlgorithm.HS512, "autenticando").setExpiration(expirationDate).compact();

			login.setToken(token);
			login.setExpirationTokenDate(expirationDate);
		}
		return loginRepository.save(login);
	}

	public Login findOne(Long id) {
		return loginRepository.findOne(id);
	}

	public Login findByEmail(String email) {
		return loginRepository.findByEmail(email);
	}

	public Login findByToken(String token) {
		return loginRepository.findByToken(token);
	}

}