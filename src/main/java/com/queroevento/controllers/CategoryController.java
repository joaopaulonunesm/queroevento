package com.queroevento.controllers;

import java.util.List;

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

import com.queroevento.models.Category;
import com.queroevento.models.Company;
import com.queroevento.services.CategoryService;
import com.queroevento.services.CompanyService;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CompanyService companyService;

	@RequestMapping(value = "v1/categories", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> postCategory(@RequestHeader(value = "Authorization") String token,
			@RequestBody Category category) throws ServletException {

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!company.getModerator()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(categoryService.findByName(category.getName()) != null ){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		category.setUrlName(categoryService.nameToUrlName(category.getName()));

		return new ResponseEntity<>(categoryService.save(category), HttpStatus.CREATED);
	}

	@RequestMapping(value = "v1/categories/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Category> deleteCategory(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) throws ServletException {

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (company.getModerator() == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Category category = categoryService.findOne(id);

		if (category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		categoryService.delete(category);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "v1/categories/{urlName}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> putCategory(@RequestHeader(value = "Authorization") String token,
			@RequestBody Category category, @PathVariable String urlName) throws ServletException {

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (company.getModerator() == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Category existingCategory = categoryService.findByUrlNameIgnoreCase(urlName);

		if (existingCategory == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		category.setId(existingCategory.getId());

		return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);
	}

	@RequestMapping(value = "/categories/{urlName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> getOneCategoryByUrlName(@PathVariable String urlName) throws ServletException {

		Category category = categoryService.findByUrlNameIgnoreCase(urlName);

		if (category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(category, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/categories/greaterthanzero", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Category>> getCategoriesGreaterThanZero() throws ServletException {
		
		return new ResponseEntity<>(categoryService.findByAmmountEventsGreaterThanOrderByAmmountEventsDesc(0), HttpStatus.OK);
	}

	@RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Category>> getCategories() throws ServletException {

		return new ResponseEntity<>(categoryService.findByOrderByAmmountEventsDesc(), HttpStatus.OK);
	}


}