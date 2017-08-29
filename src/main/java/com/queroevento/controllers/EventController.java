package com.queroevento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.queroevento.services.EventService;

@Controller
public class EventController {
	
	@Autowired
	private EventService eventService;

}