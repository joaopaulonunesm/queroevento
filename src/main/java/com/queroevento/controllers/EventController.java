package com.queroevento.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import com.queroevento.models.Event;
import com.queroevento.models.User;
import com.queroevento.services.CategoryService;
import com.queroevento.services.EventService;
import com.queroevento.services.UserService;
import com.queroevento.utils.CatalogStatusEvent;
import com.queroevento.utils.StatusEvent;
import com.queroevento.utils.TurbineType;

@Controller
public class EventController {

	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "v1/events", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> postEvent(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event) throws ServletException {

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

		category.setAmmountEvents(category.getAmmountEvents() + 1);

		if (event.getEventDate().before(new Date())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		event.setUser(user);
		event.setCategory(category);
		event.setPeopleEstimate(0);
		event.setCreateEventDate(new Date());
		event.setUrlTitle(eventService.titleToUrlTitle(event));
		event.setCatalogStatus(CatalogStatusEvent.CATALOGING);
		event.setStatus(StatusEvent.ACTIVE);

		return new ResponseEntity<>(eventService.save(event), HttpStatus.CREATED);
	}

	@RequestMapping(value = "v1/events/{urlTitle}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEvent(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable String urlTitle) throws ServletException {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findByUrlTitle(urlTitle);

		if (event.getUser() != null && user.getId() != event.getUser().getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (user.getId() != existenceEvent.getUser().getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (event.getEventDate().before(new Date())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		event.setId(existenceEvent.getId());
		event.setCreateEventDate(existenceEvent.getCreateEventDate());
		event.setPeopleEstimate(existenceEvent.getPeopleEstimate());
		event.setCatalogStatus(existenceEvent.getCatalogStatus());
		event.setUser(user);
		event.setCatalogStatus(CatalogStatusEvent.CATALOGING);
		event.setUrlTitle(eventService.titleToUrlTitle(event));

		eventService.save(event);

		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/estimate", method = RequestMethod.PUT)
	public ResponseEntity<Event> putEventPeopleEstimate(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) throws ServletException {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (existenceEvent == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (user != existenceEvent.getUser()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		existenceEvent.setPeopleEstimate(existenceEvent.getPeopleEstimate() + 1);

		eventService.save(existenceEvent);

		return new ResponseEntity<>(existenceEvent, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/status", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEventStatus(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable Long id) throws ServletException {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (existenceEvent == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (user != existenceEvent.getUser()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (event.getStatus() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		existenceEvent.setStatus(event.getStatus());

		eventService.save(existenceEvent);

		return new ResponseEntity<>(existenceEvent, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/status/catalog", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEventStatusCalog(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable Long id) throws ServletException {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (existenceEvent == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!user.getModerator()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (event.getCatalogStatus() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		existenceEvent.setCatalogStatus(event.getCatalogStatus());

		eventService.save(existenceEvent);

		return new ResponseEntity<>(existenceEvent, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/turbine", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEventTurbineType(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable Long id) throws ServletException {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (existenceEvent == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Implementar regras para nao permitir turbinar se usuário não pagou

		if (event.getTurbineType() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		existenceEvent.setTurbineType(event.getTurbineType());

		eventService.save(existenceEvent);

		return new ResponseEntity<>(existenceEvent, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Event> deleteEvent(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) throws ServletException {

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

	@RequestMapping(value = "v1/events/status/catalog/pending", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByCatalogStatusPending(
			@RequestHeader(value = "Authorization") String token) throws ServletException {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!user.getModerator()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Event> events = eventService.findByCatalogStatusOrderByEventDate(CatalogStatusEvent.CATALOGING);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/status/catalog/refused", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByCatalogStatusRefused(
			@RequestHeader(value = "Authorization") String token) throws ServletException {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!user.getModerator()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Event> events = eventService.findByCatalogStatusOrderByEventDate(CatalogStatusEvent.REFUSED);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/status/canceled", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByStatus(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!user.getModerator()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Event> events = eventService.findByStatusOrderByEventDate(StatusEvent.CANCELED);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByUser(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		User user = userService.findByToken(token);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Event> events = eventService.findByUserId(user.getId());

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	// Publicos

	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventOrderByDate() throws ServletException {

		return new ResponseEntity<>(eventService.findByEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/search/{word}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Event>> getAllEventsByWord(@PathVariable String word) throws ServletException {
		
		Set<Event> byTitle = eventService.findByEventDateAfterAndCatalogStatusAndStatusAndTitleIgnoreCaseContainingOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byCategory = eventService
				.findByEventDateAfterAndCatalogStatusAndStatusAndCategoryNameIgnoreCaseContainingOrderByEventDate(new Date(),
						CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byKeyword = eventService.findByEventDateAfterAndCatalogStatusAndStatusAndKeywordIgnoreCaseContainingOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);
		
		Set<Event> byState = eventService.findByEventDateAfterAndCatalogStatusAndStatusAndStateIgnoreCaseContainingOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);
		
		Set<Event> byCity = eventService.findByEventDateAfterAndCatalogStatusAndStatusAndCityIgnoreCaseContainingOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		List<Event> eventsList = new ArrayList<>();
		eventsList.addAll(byTitle);
		eventsList.addAll(byCategory);
		eventsList.addAll(byKeyword);
		eventsList.addAll(byState);
		eventsList.addAll(byCity);

		eventService.orderByEventDate(eventsList);

		Set<Event> events =  new LinkedHashSet<>();
		
		events.addAll(eventsList);
		
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/keyword/{keyword}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByKeywordIgnoreCase(String keyword) throws ServletException {

		List<Event> events = eventService.getEventByKeywordIgnoreCaseOrderByEventDate(keyword);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/urltitle/{url}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> getEventByUrlTitle(@PathVariable String url) throws ServletException {

		Event event = eventService.findByUrlTitle(url);

		if (event == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> getOneEvent(@PathVariable Long id) throws ServletException {

		Event event = eventService.findOne(id);

		if (event == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/past", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllPastEventOrderByDate() throws ServletException {

		return new ResponseEntity<>(eventService.findByEventDateBeforeAndCatalogStatusOrderByEventDateDesc(new Date(),
				CatalogStatusEvent.PUBLISHED), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/category/{url}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventByCategory(@PathVariable String url) throws ServletException {

		Category category = categoryService.findByUrlNameIgnoreCase(url);

		return new ResponseEntity<>(
				eventService.findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(new Date(),
						CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, category.getId()),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/events/estimate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventOrderByConfirmedPresenceDesc() throws ServletException {

		return new ResponseEntity<>(eventService.findByOrderByPeopleEstimateDesc(), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/type/turbine/gold", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getByTurbineTypeGold() throws ServletException {

		List<Event> events = eventService.findByCatalogStatusAndTurbineTypeAndEventDateAfterOrderByEventDate(
				CatalogStatusEvent.PUBLISHED, TurbineType.GOLD, new Date());

		Collections.shuffle(events);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/type/turbine/silver", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getByTurbineTypeSilver() throws ServletException {

		List<Event> events = eventService.findByCatalogStatusAndTurbineTypeAndEventDateAfterOrderByEventDate(
				CatalogStatusEvent.PUBLISHED, TurbineType.SILVER, new Date());

		List<Event> eventsGold = eventService.findByCatalogStatusAndTurbineTypeAndEventDateAfterOrderByEventDate(
				CatalogStatusEvent.PUBLISHED, TurbineType.GOLD, new Date());

		events.addAll(eventsGold);

		Collections.shuffle(events);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/type/turbine/bronze", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getByTurbineTypeBronze() throws ServletException {

		List<Event> events = eventService.findByCatalogStatusAndTurbineTypeAndEventDateAfterOrderByEventDate(
				CatalogStatusEvent.PUBLISHED, TurbineType.BRONZE, new Date());

		Collections.shuffle(events);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

}