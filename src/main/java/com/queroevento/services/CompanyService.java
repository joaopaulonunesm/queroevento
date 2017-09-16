package com.queroevento.services;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.Login;
import com.queroevento.models.Company;
import com.queroevento.repositories.CompanyRepository;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private LoginService companyService;

	public Company save(Company user) {
		return companyRepository.save(user);
	}

	public Company findOne(Long id) {
		return companyRepository.findOne(id);
	}

	public Company findByNameUrl(String url) {
		return companyRepository.findByNameUrl(url);
	}

	public Company findByNameIgnoreCase(String name) {
		return companyRepository.findByNameIgnoreCase(name);
	}

	public Company findByToken(String token) throws ServletException {

		String formattedToken = token.substring(7);

		Login login = companyService.findByToken(formattedToken);

		if (login == null) {
			throw new ServletException("Token não encontrado.");
		}

		return login.getCompany();
	}

	public String nameToUrlName(String name) {

		String urlName = name.replaceAll(" ", "-").replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e")
				.replaceAll("[îìíï]", "i").replaceAll("[õôòóö]", "o").replaceAll("[ûúùü]", "u")
				.replaceAll("[ÃÂÀÁÄ]", "A").replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I")
				.replaceAll("[ÕÔÒÓÖ]", "O").replaceAll("[ÛÙÚÜ]", "U").replace('ç', 'c').replace('Ç', 'C')
				.replace('ñ', 'n').replace('Ñ', 'N');

		return urlName.toLowerCase();
	}

}