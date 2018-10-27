package com.queroevento.services;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.Company;
import com.queroevento.models.Login;
import com.queroevento.repositories.CompanyRepository;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private LoginService loginService;
	
	public Company save(Company user) {
		return companyRepository.save(user);
	}

	public Company findByNameIgnoreCase(String name) {
		return companyRepository.findByNameIgnoreCase(name);
	}
	
	public Company validateCompanyByToken(String token) throws ServletException {
		
		Login login = loginService.validateLogin(token);
		
		return login.getCompany();
	}
	
	public Company validateCompanyModeratorByToken(String token) throws ServletException {
		
		Company company = validateCompanyByToken(token);
		
		if(!company.getModerator()) {
			throw new ServletException("Acesso negado! Entre em contato com o moderador do Quero Evento.");
		}
		
		return company;
	}
	
	public Company putCompany(Company company, Company existenceCompany) {

		company.setId(existenceCompany.getId());
		
		return save(company);
	}

	public Company putCompanyModerator(Company company) {

		company.setModerator(!company.getModerator());
		
		return save(company);
	}
	
	public Company findCompanyByUrlName(String urlName) throws ServletException {
		
		Company company = companyRepository.findByUrlName(urlName);

		if (company == null) {
			throw new ServletException("Empresa não encontrada pelo Nome URL.");
		}
		
		return company;
	}
	
}