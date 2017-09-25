package com.queroevento.controllers;

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
import com.queroevento.models.Company;
import com.queroevento.services.CategoryService;
import com.queroevento.services.EventService;
import com.queroevento.services.CompanyService;
import com.queroevento.utils.CatalogStatusEvent;
import com.queroevento.utils.StatusEvent;
import com.queroevento.utils.TurbineType;

@Controller
public class EventController {

	@Autowired
	private EventService eventService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "v1/events", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> postEvent(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event) throws ServletException {

		Company company = companyService.findByToken(token);

		validateEventFields(event, company);

		Category category = categoryService.findOne(event.getCategory().getId());

		if (category == null) {
			throw new ServletException("Categoria não existente. Por favor informe uma categoria existente.");
		}

		event.setCompany(company);
		event.setCategory(category);
		event.setPeopleEstimate(0);
		event.setCreateEventDate(new Date());
		event.setUrlTitle(eventService.titleToUrlTitle(event));
		event.setCatalogStatus(CatalogStatusEvent.CATALOGING);
		event.setStatus(StatusEvent.ACTIVE);
		event.setViews(0L);

		eventService.save(event);

		return new ResponseEntity<>(event, HttpStatus.CREATED);
	}

	@RequestMapping(value = "v1/events/{urlTitle}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEvent(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable String urlTitle) throws ServletException {

		Company company = companyService.findByToken(token);

		validateEventFields(event, company);

		Category category = categoryService.findOne(event.getCategory().getId());

		// Verifica se a categoria informada existe
		if (category == null) {
			throw new ServletException("Categoria não existente. Por favor informe uma categoria existente.");
		}

		Event existenceEvent = eventService.findByUrlTitle(urlTitle);

		// Verifica se o evento existe
		if (existenceEvent == null) {
			throw new ServletException("Evento não existente.");
		}

		// Verifica se company que esta alterando é a mesma do evento alterado
		if (event.getCompany() != null && company.getId() != event.getCompany().getId()) {
			throw new ServletException("Sua empresa é diferente da empresa que esta informando no evento.");
		}

		// Verifica se a company que esta alterando é diferente da company do
		// evento já existente
		if (company.getId() != existenceEvent.getCompany().getId()) {
			throw new ServletException("Sua empresa é diferente da empresa que criou esse evento.");
		}

		event.setId(existenceEvent.getId());
		event.setCreateEventDate(existenceEvent.getCreateEventDate());
		event.setPeopleEstimate(existenceEvent.getPeopleEstimate());
		event.setCatalogStatus(existenceEvent.getCatalogStatus());
		event.setCompany(company);
		event.setCatalogStatus(CatalogStatusEvent.CATALOGING);
		event.setUrlTitle(eventService.titleToUrlTitle(event));
		event.setCategory(category);

		Category oldCategory = existenceEvent.getCategory();
		oldCategory.setAmmountEvents(oldCategory.getAmmountEvents() - 1);
		categoryService.save(oldCategory);

		return new ResponseEntity<>(eventService.save(event), HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/estimate", method = RequestMethod.PUT)
	public ResponseEntity<Event> putEventPeopleEstimate(@RequestHeader(value = "Authorization") String token,
			@PathVariable Long id) throws ServletException {

		Company comapany = companyService.findByToken(token);

		if (comapany == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (existenceEvent == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (comapany != existenceEvent.getCompany()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		existenceEvent.setPeopleEstimate(existenceEvent.getPeopleEstimate() + 1);

		eventService.save(existenceEvent);

		return new ResponseEntity<>(existenceEvent, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/status", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEventStatus(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable Long id) throws ServletException {

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (existenceEvent == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (company != existenceEvent.getCompany()) {
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

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (existenceEvent == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!company.getModerator()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (event.getCatalogStatus() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		existenceEvent.setCatalogStatus(event.getCatalogStatus());

		eventService.save(existenceEvent);

		Category category = existenceEvent.getCategory();

		eventService.refreshAmmountEventsInCategory(category);
		categoryService.save(category);

		return new ResponseEntity<>(existenceEvent, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/{id}/turbine", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> putEventTurbineType(@RequestHeader(value = "Authorization") String token,
			@RequestBody Event event, @PathVariable Long id) throws ServletException {

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event existenceEvent = eventService.findOne(id);

		if (existenceEvent == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (company != existenceEvent.getCompany()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Event event = eventService.findOne(id);

		if (event == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (company != event.getCompany()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		eventService.delete(event);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/status/catalog/pending", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByCatalogStatusPending(
			@RequestHeader(value = "Authorization") String token) throws ServletException {

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!company.getModerator()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Event> events = eventService.findByCatalogStatusOrderByEventDate(CatalogStatusEvent.CATALOGING);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/status/catalog/refused", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByCatalogStatusRefused(
			@RequestHeader(value = "Authorization") String token) throws ServletException {

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!company.getModerator()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Event> events = eventService.findByCatalogStatusOrderByEventDate(CatalogStatusEvent.REFUSED);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/status/canceled", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByStatus(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!company.getModerator()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Event> events = eventService.findByStatusOrderByEventDate(StatusEvent.CANCELED);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "v1/events/company", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventByCompany(@RequestHeader(value = "Authorization") String token)
			throws ServletException {

		Company company = companyService.findByToken(token);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Event> events = eventService.findByCompanyId(company.getId());

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	// Publicos

	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventOrderByDate() throws ServletException {

		return new ResponseEntity<>(eventService.findByEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE), HttpStatus.OK);
	}

	@RequestMapping(value = "/events/company/{url}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEventsCompanyByUrl(@PathVariable String url) throws ServletException {

		Company company = companyService.findByNameUrl(url);

		if (company == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Event> events = eventService.findByCompanyIdAndEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(
				company.getId(), new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/search/{word}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Event>> getAllEventsByWord(@PathVariable String word) throws ServletException {

		List<Event> eventsList = eventService.findWordInTitleAndCategoryAndKeywordAndStateAndCity(word);

		Set<Event> events = new LinkedHashSet<>();

		events.addAll(eventsList);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	// Verificar necessidade de filtrar por Data, Status de catalogação e Status
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

		event.setViews(event.getViews() + 1);
		eventService.save(event);

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

		List<Event> events = eventService.findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, category.getId());

		category.setAmmountEvents(events.size());
		categoryService.save(category);

		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	// Verificar necessidade de filtrar por Data, Status de catalogação e Status
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

	private void validateEventFields(Event event, Company company) throws ServletException {

		// Verifica se a company existe pelo token informado
		if (company == null) {
			throw new ServletException("Empresa não existente.");
		}

		// Verifica se o titulo é vazio ou nulo
		if (event.getTitle() == null || event.getTitle().isEmpty()) {
			throw new ServletException("O título não pode ser nulo ou vazio. Por favor informe o título.");
		}

		// Verifica se existe data no evento
		if (event.getEventDate() == null) {
			throw new ServletException("Por favor informe uma data para o evento.");
		}

		// Verifica se a data do evento é antes da data atual
		if (event.getEventDate().before(new Date())) {
			throw new ServletException(
					"A data não pode ser menor do que a data atual. Por favor informe uma data futura.");
		}

		// Verifica se o preço está nulo
		if (event.getPrice() == null) {
			throw new ServletException("Por favor informe um preço que seja maior que zero.");
		}

		// Verifica se a categoria está nula
		if (event.getCategory() == null) {
			throw new ServletException("Por favor informe uma categoria.");
		}

		// Verifica se o local é vazio ou nulo
		if (event.getLocal().isEmpty() || event.getLocal() == null) {
			throw new ServletException("Por favor informe o local do evento.");
		}
	}

}