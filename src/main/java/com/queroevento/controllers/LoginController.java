package com.queroevento.controllers;

import java.util.Date;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.queroevento.models.Login;
import com.queroevento.services.LoginService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/logins", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Login> postLogin(@RequestBody Login login) {

		if (login.getEmail() == null || login.getPassword() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (loginService.findByEmail(login.getEmail()) != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		login.setCreateDate(new Date());
		login.setActive(true);

		return new ResponseEntity<>(loginService.save(login), HttpStatus.CREATED);
	}

	@RequestMapping(value = "v1/logins/password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Login> putLoginPassword(@RequestHeader(value = "Authorization") String token,
			@RequestBody Login login) {

		Login existenceLogin = loginService.findByToken(token);

		if (existenceLogin == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (login.getPassword() == null || login.getPassword().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		existenceLogin.setPassword(login.getPassword());

		return new ResponseEntity<>(loginService.save(existenceLogin), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/logins/active", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Login> putLoginActive(@RequestHeader(value = "Authorization") String token,
			@RequestBody Login login) {

		Login existenceLogin = loginService.findByToken(token);

		if (existenceLogin == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (login.getActive() != null && login.getActive() != existenceLogin.getActive()) {
			existenceLogin.setActive(login.getActive());
		}

		return new ResponseEntity<>(loginService.save(existenceLogin), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/logins", method = RequestMethod.DELETE)
	public ResponseEntity<Login> deleteLogin(@RequestHeader(value = "Authorization") String token) {

		Login existenceLogin = loginService.findByToken(token);

		if (existenceLogin == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		loginService.delete(existenceLogin);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/logins/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Login> authenticatedLogin(@RequestBody Login login) throws ServletException {

		Login existenceLogin = loginService.validateLogin(login);

		if (existenceLogin.getExpirationTokenDate() != null
				&& existenceLogin.getExpirationTokenDate().after(new Date())) {

			return new ResponseEntity<>(existenceLogin, HttpStatus.OK);

		} else if (existenceLogin.getExpirationTokenDate() == null
				|| existenceLogin.getExpirationTokenDate().before(new Date())) {

			Date expirationDate = new Date(System.currentTimeMillis() + 240 * 60 * 1000);

			String token = Jwts.builder().setSubject(existenceLogin.getEmail())
					.signWith(SignatureAlgorithm.HS512, "autenticando").setExpiration(expirationDate).compact();

			existenceLogin.setToken(token);
			existenceLogin.setExpirationTokenDate(expirationDate);
		}

		return new ResponseEntity<>(loginService.save(existenceLogin), HttpStatus.OK);
	}

}