package com.queroevento.controllers;

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

import com.queroevento.models.User;
import com.queroevento.services.UserService;

@Controller
@RequestMapping(value = "v1")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/users", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> putUser(@RequestHeader(value = "Authorization") String token, @RequestBody User user, @PathVariable Long id){
		
		User existenceUser = userService.findByToken(token);
		
		if(existenceUser == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		user.setId(id);
		
		return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
	}
	
	
}