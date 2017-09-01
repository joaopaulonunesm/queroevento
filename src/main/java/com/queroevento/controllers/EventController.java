package com.queroevento.controllers;

import java.util.Date;
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
import com.queroevento.models.Event;
import com.queroevento.models.User;
import com.queroevento.services.CategoryService;
import com.queroevento.services.EventService;
import com.queroevento.services.UserService;

@Controller
@RequestMapping(value = "v1")
public class EventController {

	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/events", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> postEvent(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event) {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (event.getTitle() == null || event.getTitle().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (event.getEventDate() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (event.getPrice() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (event.getCategory() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Category category = categoryService.findOne(event.getCategory().getId());

		if (category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		event.setUser(user);
		event.setCategory(category);
		event.setPeopleEstimate(0);
		event.setCreateEventDate(new Date());
		event.setUrlTitle(eventService.titleToUrlTitle(event.getTitle()));

		return new ResponseEntity<>(eventService.save(event), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/events/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEvent(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable Long id) {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (event.getUser() != null && user.getId() != event.getUser().getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (user.getId() != existenceEvent.getUser().getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		event.setId(existenceEvent.getId());
		event.setCreateEventDate(existenceEvent.getCreateEventDate());
		event.setPeopleEstimate(existenceEvent.getPeopleEstimate());
		event.setCatalogStatus(existenceEvent.getCatalogStatus());
		event.setUser(user);
		event.setCatalogStatus(existenceEvent.getCatalogStatus());

		eventService.save(event);

		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/{id}/estimate", method = RequestMethod.PUT)
	public ResponseEntity<Event> putEventPeopleEstimate(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (user != existenceEvent.getUser()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		existenceEvent.setPeopleEstimate(existenceEvent.getPeopleEstimate() + 1);

		eventService.save(existenceEvent);

		return new ResponseEntity<>(existenceEvent, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/{id}")
	public ResponseEntity<Event> deleteEvent(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event event = eventService.findOne(id);

		if (event == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (user != event.getUser()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		eventService.delete(event);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/events/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> getOneEvent(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event event = eventService.findOne(id);

		if (event == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventOrderByDate(@RequestHeader(value = "Authorization") String token) {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(eventService.findAllOrderByEventDate(), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/category/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventByCategory(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Category category = categoryService.findOne(id);

		if (category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(eventService.findByCategoryIdOrderByEventDate(category.getId()), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/estimate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventOrderByConfirmedPresenceDesc(
			@RequestHeader(value = "Authorization") String token) {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(eventService.findByOrderByPeopleEstimateDesc(), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/urltitle/{url}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> getEventByUrlTitle(@RequestHeader(value = "Authorization") String token,
			@PathVariable String url) {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event event = eventService.findByUrlTitle(url);

		if (event == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(event, HttpStatus.OK);
	}
}