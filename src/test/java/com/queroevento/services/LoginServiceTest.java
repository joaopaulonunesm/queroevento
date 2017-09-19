package com.queroevento.services;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.queroevento.models.Company;
import com.queroevento.models.Login;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class LoginServiceTest {

	@Autowired
	private LoginService loginService;

	@Test
	public void shouldSaveLogin() {

		Login login = factoryLogin("queroevento@hotmail.com", "queroevento", "Quero Evento");

		Assert.assertNotNull(loginService.save(login));
	}

	@Test
	public void shouldDeleteLogin() {

		Login login = factoryLogin("queroevento1@hotmail.com", "queroevento", "Quero Evento");
		loginService.save(login);

		Long id = login.getId();

		loginService.delete(login);

		Assert.assertNull(loginService.findOne(id));
	}

	@Test
	public void shouldAuthenticateLogin() {

		Login login = factoryLogin("queroevento4@hotmail.com", "queroevento", "Quero Evento");
		loginService.save(login);

		loginService.authenticateLogin(login);
		loginService.save(login);

		Assert.assertNotNull(login.getToken());
		Assert.assertNotNull(login.getExpirationTokenDate());
	}

	@Test
	public void shouldFindOneLoginById() {

		Login login = factoryLogin("queroevento2@hotmail.com", "queroevento", "Quero Evento");
		loginService.save(login);

		Assert.assertNotNull(loginService.findOne(login.getId()));
	}

	@Test
	public void shouldFindOneLoginByEmail() {

		Login login = factoryLogin("queroevento3@hotmail.com", "queroevento", "Quero Evento");
		loginService.save(login);

		Assert.assertNotNull(loginService.findByEmail(login.getEmail()));
	}

	@Test
	@DatabaseSetup("/company.xml")
	@DatabaseSetup("/login.xml")
	public void shouldFindOneLoginByToken() {

		Assert.assertNotNull(loginService.findByToken("tokendoqueroevento"));
	}

	private Login factoryLogin(String email, String password, String companyName) {

		Login login = new Login();
		login.setEmail(email);
		login.setPassword(password);
		Company company = new Company();
		company.setName(companyName);
		login.setCompany(company);
		return login;
	}

}
