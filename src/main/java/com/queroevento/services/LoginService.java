package com.queroevento.services;

import java.util.Date;

import javax.servlet.ServletException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.Company;
import com.queroevento.models.Login;
import com.queroevento.repositories.LoginRepository;
import com.queroevento.utils.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final LoginRepository loginRepository;
	private final CompanyService companyService;
	private final Utils utils;

	public Login save(Login login) {
		return loginRepository.save(login);
	}

	public void delete(Login login) {
		loginRepository.delete(login);
	}

	public Login authenticate(Login login) throws ServletException {
		if (login.getEmail() == null || login.getPassword() == null) {
			throw new ServletException("Informe seu Email e Senha.");
		}

		Login existenceLogin = getOneByEmail(login.getEmail());

		if (!login.getPassword().equals(existenceLogin.getPassword())) {
			throw new ServletException("Email ou Senha inválido, favor tente novamente.");
		}
		
		validateExpirationTokenDate(existenceLogin);
		
		return existenceLogin;
	}
	
	public Login validateLogin(String token) throws ServletException  {
		Login login = findByToken(token);

		if (login == null) {
			throw new ServletException("Login não encontrado.");
		}
		
		validateExpirationTokenDate(login);

		return login;
	}

	public Login postLogin(Login login) throws ServletException {
		Company company = login.getCompany();

		validateLogin(login, company);

		login.setCreateDate(new Date());
		login.setActive(true);

		company.setUrlName(utils.stringToUrl(company.getName()));
		
		return save(login);
	}

	public Login putLoginPassword(Login login, Login existenceLogin) throws ServletException {

		if (login.getPassword() == null || login.getPassword().isEmpty()) {
			throw new ServletException("É necessário informar uma senha.");
		}

		existenceLogin.setPassword(login.getPassword());
		
		return save(existenceLogin);
	}

	public Login putLoginActive(Login login, Login existenceLogin) throws ServletException {
		if(login.getActive() == null) {
			throw new ServletException("Informe se o login esta Ativo.");
		}
		
		existenceLogin.setActive(login.getActive());
		
		return save(existenceLogin);
	}
	
	private Login findByEmail(String email) {
		return loginRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Erro ao buscar Login por Email. Email: " + email));
	}
	
	private Login findByToken(String token) {
		return loginRepository.findByToken(token.substring(7)).orElseThrow(() -> new RuntimeException("Erro ao buscar Login por Token. Token: " + token));
	}

	private Login getOneByEmail(String email) throws ServletException {
		Login login = findByEmail(email);
		
		if(login == null) {
			throw new ServletException("Email não encontrado.");
		}
		
		return login;
	}
	
	private void validateLogin(Login login, Company company) throws ServletException {
		if (login.getEmail() == null || login.getPassword() == null || company.getName() == null
				|| company.getName().isEmpty()) {
			throw new ServletException("Email, Senha e Nome da Empresa são informações obrigatórias.");
		}

		if (findByEmail(login.getEmail()) != null) {
			throw new ServletException("Email já cadastrado no Quero Evento!");
		}

		if (companyService.findByNameIgnoreCase(company.getName()) != null) {
			throw new ServletException("Já existe uma empresa cadastrada com o nome informado.");
		}
	}

	private void validateExpirationTokenDate(Login login) {
		if (login.getExpirationTokenDate() == null || login.getExpirationTokenDate().before(new Date())) {

			Date expirationDate = new Date(System.currentTimeMillis() + 240 * 60 * 1000);

			String token = Jwts.builder().setSubject(login.getEmail())
					.signWith(SignatureAlgorithm.HS512, "autenticando").setExpiration(expirationDate).compact();

			login.setToken(token);
			login.setExpirationTokenDate(expirationDate);
			
			loginRepository.save(login);
		}
	}
}