package com.queroevento.controllers;

import java.util.List;

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
import com.queroevento.models.User;
import com.queroevento.services.CategoryService;
import com.queroevento.services.UserService;

@Controller
@RequestMapping(value = "v1")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/categories", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> postCategory(@RequestHeader(value = "Authorization") String token, @RequestBody Category category) {
		
		User user = userService.findByToken(token);
		
		if(user == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(categoryService.save(category), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Category> deleteCategory(@RequestHeader(value = "Authorization") String token, @PathVariable Long id) {
		
		User user = userService.findByToken(token);
		
		if(user == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Category category = categoryService.findOne(id);

		if (category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		categoryService.delete(category);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/categories/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> putCategory(@RequestHeader(value = "Authorization") String token, @RequestBody Category category, @PathVariable Long id) {

		User user = userService.findByToken(token);
		
		if(user == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Category existingCategory = categoryService.findOne(id);

		if (existingCategory == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		category.setId(existingCategory.getId());

		return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);
	}

	@RequestMapping(value = "/categories/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> getOneCategory(@RequestHeader(value = "Authorization") String token, @PathVariable Long id) {

		User user = userService.findByToken(token);
		
		if(user == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Category category = categoryService.findOne(id);

		if (category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(category, HttpStatus.OK);
	}

	@RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Category>> getCategories(@RequestHeader(value = "Authorization") String token) {

		User user = userService.findByToken(token);
		
		if(user == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
	}

}