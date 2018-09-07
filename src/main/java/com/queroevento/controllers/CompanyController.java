package com.queroevento.controllers;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.queroevento.models.Company;
import com.queroevento.services.CompanyService;

@Controller
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@RequestMapping(value = "/v1/companies", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Company> putCompany(@RequestHeader(value = "Authorization") String token,
			@RequestBody Company company) throws ServletException {

		Company existenceCompany = companyService.findByToken(token);

		if (existenceCompany == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		company.setId(existenceCompany.getId());

		return new ResponseEntity<>(companyService.save(company), HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/companies/moderator", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Company> putCompanyModerator(@RequestHeader(value = "Authorization") String token,
			@RequestBody Company company) throws ServletException {

		Company existenceCompany = companyService.findByToken(token);

		if (existenceCompany == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		existenceCompany.setModerator(company.getModerator());

		return new ResponseEntity<>(companyService.save(existenceCompany), HttpStatus.OK);
	}

	@RequestMapping(value = "/companies/{urlName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Company> findByUrlName(@PathVariable String urlName) {

		Company company = companyService.findByUrlName(urlName);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(company, HttpStatus.OK);
	}

}