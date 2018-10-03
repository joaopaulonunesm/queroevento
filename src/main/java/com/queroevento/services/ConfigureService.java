package com.queroevento.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.queroevento.utils.Utils;

@Component
public class ConfigureService {

	@Autowired
	public Utils utils;
	
	@Autowired
	public CompanyService companyService;
	
	@Autowired
	public CategoryService categoryService;
	
}
