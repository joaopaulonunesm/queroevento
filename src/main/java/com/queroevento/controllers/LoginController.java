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

import com.queroevento.models.Company;
import com.queroevento.models.Login;
import com.queroevento.services.ConfigureService;
import com.queroevento.services.LoginService;

@Controller
public class LoginController extends ConfigureService {

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/logins", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Login> postLogin(@RequestBody Login login) {

		Company company = login.getCompany();

		if (login.getEmail() == null || login.getPassword() == null || company.getName() == null
				|| company.getName().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (loginService.findByEmail(login.getEmail()) != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (companyService.findByNameIgnoreCase(company.getName()) != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		login.setCreateDate(new Date());
		login.setActive(true);

		company.setUrlName(utils.stringToUrl(company.getName()));

		return new ResponseEntity<>(loginService.save(login), HttpStatus.CREATED);
	}

	@RequestMapping(value = "v1/logins/password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Login> putLoginPassword(@RequestHeader(value = "Authorization") String token,
			@RequestBody Login login) throws ServletException {

		Login existenceLogin = loginService.validateLogin(token);

		if (login.getPassword() == null || login.getPassword().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		existenceLogin.setPassword(login.getPassword());

		return new ResponseEntity<>(loginService.save(existenceLogin), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/logins/active", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Login> putLoginActive(@RequestHeader(value = "Authorization") String token,
			@RequestBody Login login) throws ServletException {

		Login existenceLogin = loginService.validateLogin(token);

		if (login.getActive() != null && login.getActive() != existenceLogin.getActive()) {
			existenceLogin.setActive(login.getActive());
		}

		return new ResponseEntity<>(loginService.save(existenceLogin), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/logins", method = RequestMethod.DELETE)
	public ResponseEntity<Login> deleteLogin(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		Login existenceLogin = loginService.validateLogin(token);

		loginService.delete(existenceLogin);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/logins/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Login> authenticatedLogin(@RequestBody Login login) throws ServletException {

		if (login.getEmail() == null || login.getPassword() == null) {
			throw new ServletException("Username e Senha é obrigatório.");
		}

		Login existenceLogin = loginService.findByEmail(login.getEmail());

		if (existenceLogin == null) {
			throw new ServletException("Empresa não encontrada.");
		}

		if (!login.getPassword().equals(existenceLogin.getPassword())) {
			throw new ServletException("Username ou Password inválido.");
		}

		return new ResponseEntity<>(loginService.authenticate(existenceLogin), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/logins", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Login> getOneEvent(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		Login login = loginService.validateLogin(token);

		if (login == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(login, HttpStatus.OK);
	}

}