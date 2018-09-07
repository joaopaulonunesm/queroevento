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
public class CompanyServiceTest {

	@Autowired
	private LoginService loginService;

	@Autowired
	private CompanyService companyService;

	@Test
	public void shouldSaveChangesCompany() {

		Login login = loginService
				.save(factoryLoginAndCompany("queroevento@hotmail.com", "queroevento", "Quero Evento"));

		Company company = login.getCompany();

		Assert.assertNull(company.getState());
		Assert.assertNull(company.getCity());
		Assert.assertNull(company.getPhoneNumber());
		Assert.assertNull(company.getCellPhoneNumber());
		Assert.assertNull(company.getContactEmail());

		String state = "Paraná";
		String city = "Maringá";
		String phoneNumber = "4430303030";
		String cellPhoneNumber = "44999999999";
		String contactEmail = "joaomarques@queroevento.com";

		company.setState(state);
		company.setCity(city);
		company.setPhoneNumber(phoneNumber);
		company.setCellPhoneNumber(cellPhoneNumber);
		company.setContactEmail(contactEmail);

		companyService.save(company);

		Assert.assertEquals(state, company.getState());
		Assert.assertEquals(city, company.getCity());
		Assert.assertEquals(phoneNumber, company.getPhoneNumber());
		Assert.assertEquals(cellPhoneNumber, company.getCellPhoneNumber());
		Assert.assertEquals(contactEmail, company.getContactEmail());
	}

	@Test
	public void shouldFindOneCompanyByUrlName() {

		Login login = factoryLoginAndCompany("queroevento1@hotmail.com", "queroevento1", "Quero Evento1");
		Company company = login.getCompany();
		company.setUrlName(companyService.nameToUrlName(company.getName()));
		companyService.save(company);
		Assert.assertNotNull(companyService.findByUrlName(company.getUrlName()));
	}

	@Test
	@DatabaseSetup("/company.xml")
	public void shouldFindOneCompanyById() {

		Assert.assertNotNull(companyService.findOne(1L));
	}

	@Test
	@DatabaseSetup("/company.xml")
	public void shouldFindOneCompanyByName() {

		Assert.assertNotNull(companyService.findByNameIgnoreCase("Quero Evento"));
	}

	@Test
	@DatabaseSetup("/company.xml")
	@DatabaseSetup("/login.xml")
	public void shouldFindOneCompanyByTokenLogin() {

		Assert.assertNotNull(companyService.findByToken("Bearer tokendoqueroevento"));
	}

	private Login factoryLoginAndCompany(String email, String password, String companyName) {

		Login login = new Login();
		login.setEmail(email);
		login.setPassword(password);
		Company company = new Company();
		company.setName(companyName);
		login.setCompany(company);
		return login;
	}

}
