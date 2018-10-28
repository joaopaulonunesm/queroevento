package com.queroevento.controllers;

import java.util.Date;
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

import com.queroevento.enums.CatalogStatusEvent;
import com.queroevento.enums.StatusEvent;
import com.queroevento.enums.TurbineType;
import com.queroevento.models.Company;
import com.queroevento.models.Event;
import com.queroevento.services.CompanyService;
import com.queroevento.services.EventService;

@Controller
public class EventController {

	@Autowired
	private EventService eventService;
	
	@Autowired
	private CompanyService companyService;

	@RequestMapping(value = "v1/events", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> postEvent(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event) throws ServletException {

		Company company = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(eventService.postEvent(event, company), HttpStatus.CREATED);
	}

	@RequestMapping(value = "v1/events/{urlTitle}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEvent(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable String urlTitle) throws ServletException {

		Company company = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(eventService.putEvent(event, urlTitle, company), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/estimate", method = RequestMethod.PUT)
	public ResponseEntity<Event> putEventPeopleEstimate(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) throws ServletException {

		Company company = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(eventService.putEventPeopleEstimate(id, company), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/status", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEventStatus(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable Long id) throws ServletException {

		Company company = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(eventService.putEventStatus(event, id, company), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/status/catalog", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEventStatusCalog(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable Long id) throws ServletException {

		companyService.validateCompanyModeratorByToken(token);

		return new ResponseEntity<>(eventService.putEventStatusCalog(event, id), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/turbine", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEventTurbineType(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable Long id) throws ServletException {

		Company company = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(eventService.putEventTurbineType(event, id, company), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Event> deleteEvent(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) throws ServletException {

		Company company = companyService.validateCompanyByToken(token);

		eventService.deleteEvent(id, company);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/status/catalog/pending", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByCatalogStatusPending(
			@RequestHeader(value = "Authorization") String token) throws ServletException {

		companyService.validateCompanyModeratorByToken(token);

		return new ResponseEntity<>(eventService.findByCatalogStatus(CatalogStatusEvent.CATALOGING), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/status/catalog/refused", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByCatalogStatusRefused(
			@RequestHeader(value = "Authorization") String token) throws ServletException {

		companyService.validateCompanyModeratorByToken(token);

		return new ResponseEntity<>(eventService.findByCatalogStatus(CatalogStatusEvent.REFUSED), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/status/canceled", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByStatus(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		companyService.validateCompanyModeratorByToken(token);

		return new ResponseEntity<>(eventService.findByEventStatus(StatusEvent.CANCELED), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/company", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByCompany(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		Company company = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(eventService.findByCompanyId(company.getId()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "v1/events/company/next", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getNextEventsByCompany(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		Company company = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(eventService.findNextEventsByCompanyId(company.getId()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "v1/events/company/realized", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getRealizedEventsByCompany(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		Company company = companyService.validateCompanyByToken(token);

		return new ResponseEntity<>(eventService.findRealizedEventsByCompanyId(company.getId()), HttpStatus.OK);
	}

	// Publicos

	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventOrderByDate() {

		return new ResponseEntity<>(eventService.getAllEventOrderByDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/company/{url}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventsCompanyByUrl(@PathVariable String url) throws ServletException {

		return new ResponseEntity<>(eventService.findEventsByUrlNameCategory(
				url, new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/search/{word}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Event>> getAllEventsByWord(@PathVariable String word) {

		return new ResponseEntity<>(eventService.findByWord(word), HttpStatus.OK);
	}

	// Verificar necessidade de filtrar por Data, Status de catalogação e Status
	@RequestMapping(value = "/events/keyword/{keyword}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByKeywordIgnoreCase(String keyword) {

		return new ResponseEntity<>(eventService.getEventByKeyword(keyword), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/urltitle/{url}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> getEventByUrlTitle(@PathVariable String url) throws ServletException {

		return new ResponseEntity<>(eventService.getEventByUrlTitle(url), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> getOneEvent(@PathVariable Long id) throws ServletException {

		return new ResponseEntity<>(eventService.getOneById(id), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/past", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllPastEventOrderByDate() {

		return new ResponseEntity<>(eventService.getAllPastEventOrderByDate(new Date(),
				CatalogStatusEvent.PUBLISHED), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/category/{url}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventByCategory(@PathVariable String url) throws ServletException {

		return new ResponseEntity<>(eventService.getAllEventByCategory(url), HttpStatus.OK);
	}

	// Verificar necessidade de filtrar por Data, Status de catalogação e Status
	@RequestMapping(value = "/events/estimate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventOrderByConfirmedPresenceDesc() {

		return new ResponseEntity<>(eventService.findByOrderByPeopleEstimateDesc(), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/turbine/gold", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getByTurbineTypeGold() {

		return new ResponseEntity<>(eventService.findByTurbineType(
				CatalogStatusEvent.PUBLISHED, TurbineType.GOLD, new Date()), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/turbine/silver", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getByTurbineTypeSilver() {

		return new ResponseEntity<>(eventService.findByTurbineType(
				CatalogStatusEvent.PUBLISHED, TurbineType.SILVER, new Date()), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/turbine/bronze", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getByTurbineTypeBronze() {

		return new ResponseEntity<>(eventService.findByTurbineType(
				CatalogStatusEvent.PUBLISHED, TurbineType.BRONZE, new Date()), HttpStatus.OK);
	}

}