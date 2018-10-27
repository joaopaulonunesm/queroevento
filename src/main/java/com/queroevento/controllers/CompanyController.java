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
	public CompanyService companyService;
	
	@RequestMapping(value = "/v1/companies", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Company> putCompany(@RequestHeader(value = "Authorization") String token,
			@RequestBody Company company) throws ServletException {

		Company existenceCompany = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(companyService.putCompany(company, existenceCompany), HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/companies/moderator", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Company> putCompanyModerator(@RequestHeader(value = "Authorization") String token,
			@RequestBody Company company) throws ServletException {

		Company existenceCompany = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(companyService.putCompanyModerator(existenceCompany), HttpStatus.OK);
	}

	@RequestMapping(value = "/companies/{urlName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Company> findCompany(@PathVariable String urlName) throws ServletException {

		return new ResponseEntity<>(companyService.findCompanyByUrlName(urlName), HttpStatus.OK);
	}
	
}