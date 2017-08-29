package com.queroevento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.queroevento.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	
}