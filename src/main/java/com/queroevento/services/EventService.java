package com.queroevento.services;

import static com.queroevento.enums.CatalogStatusEvent.CATALOGING;
import static com.queroevento.enums.CatalogStatusEvent.PUBLISHED;
import static com.queroevento.enums.CatalogStatusEvent.REFUSED;
import static com.queroevento.enums.StatusEvent.ACTIVE;
import static com.queroevento.enums.StatusEvent.CANCELED;
import static com.queroevento.enums.TurbineType.BRONZE;
import static com.queroevento.enums.TurbineType.GOLD;
import static com.queroevento.enums.TurbineType.SILVER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.enums.CatalogStatusEvent;
import com.queroevento.enums.StatusEvent;
import com.queroevento.enums.TurbineType;
import com.queroevento.models.Category;
import com.queroevento.models.Company;
import com.queroevento.models.Event;
import com.queroevento.repositories.EventRepository;
import com.queroevento.utils.Utils;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	public CategoryService categoryService;
	
	@Autowired
	public CompanyService companyService;

	@Autowired
	public Utils utils;

	public Event save(Event event) {
		return eventRepository.save(event);
	}

	public Event getOneById(Long id) throws ServletException {
		
		Event event =  eventRepository.findOne(id);
		
		if (event == null) {

			throw new ServletException("ID Evento não encontrado.");
		}
		
		return event;
	}
	
	public List<Event> getAllEventOrderByDate(Date date,
			CatalogStatusEvent catalogStatusEvent, StatusEvent statusEvent) {

		return eventRepository.findByEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(date, catalogStatusEvent,
				statusEvent);
	}

	public List<Event> getAllPastEventOrderByDate(Date date, CatalogStatusEvent status) {

		return eventRepository.findByEventDateBeforeAndCatalogStatusOrderByEventDateDesc(date, status);
	}

	public List<Event> findByOrderByPeopleEstimateDesc() {

		return eventRepository.findByOrderByPeopleEstimateDesc();
	}

	public List<Event> findByCatalogStatus(CatalogStatusEvent catalogStatus) {

		return eventRepository.findByCatalogStatusOrderByEventDate(catalogStatus);
	}

	public List<Event> findByEventStatus(StatusEvent status) {

		return eventRepository.findByStatusOrderByEventDate(status);
	}

	public List<Event> getEventByKeyword(String keyword) {

		return eventRepository.getEventByKeywordIgnoreCaseOrderByEventDate(keyword);
	}

	public List<Event> findByCompanyId(Long id) {

		return eventRepository.findByCompanyIdOrderByEventDate(id);
	}
	
	public List<Event> findNextEventsByCompanyId(Long id) {

		return eventRepository.findByCompanyIdAndEventDateAfterOrderByEventDate(id, new Date());
	}
	
	public List<Event> findRealizedEventsByCompanyId(Long id) {

		return eventRepository.findByCompanyIdAndEventDateBeforeOrderByEventDate(id, new Date());
	}


	public List<Event> findByTurbineType(
			CatalogStatusEvent catalogStatus, TurbineType turbineType, Date date) {

		List<Event> events = eventRepository.findByCatalogStatusAndTurbineTypeAndEventDateAfterOrderByEventDate(catalogStatus,
				turbineType, date);
		
		Collections.shuffle(events);
		
		return events;
	}

	public List<Event> findEventsByUrlNameCategory(String url,
			Date date, CatalogStatusEvent catalogStatus, StatusEvent status) throws ServletException {
		
		Company company = companyService.findCompanyByUrlName(url);

		return eventRepository.findByCompanyIdAndEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(company.getId(),
				date, catalogStatus, status);
	}



	public Set<Event> findByWord(String word) {

		Set<Event> byTitle = findEventByWord(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byCategory = findEventByCategory(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byKeyword = findEventByKeyword(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byState = findEventByState(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byCity = findEventByCity(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		List<Event> eventsList = new ArrayList<>();
		eventsList.addAll(byTitle);
		eventsList.addAll(byCategory);
		eventsList.addAll(byKeyword);
		eventsList.addAll(byState);
		eventsList.addAll(byCity);

		orderEventsByDate(eventsList);

		Set<Event> events = new LinkedHashSet<>();
		events.addAll(eventsList);

		return events;
	}

	public Event postEvent(Event event, Company company) throws ServletException {

		validateEventFields(event);

		Category category = categoryService.getOneCategoryById(event.getCategory().getId());

		event.setCompany(company);
		event.setCategory(category);
		event.setPeopleEstimate(0);
		event.setCreateEventDate(new Date());
		event.setUrlTitle(utils.stringToUrl(event, event.getTitle()));
		event.setCatalogStatus(CatalogStatusEvent.CATALOGING);
		event.setStatus(StatusEvent.ACTIVE);
		event.setViews(0L);

		return save(event);
	}

	public Event putEvent(Event event, String urlTitle, Company company) throws ServletException {

		validateEventFields(event);

		Category category = categoryService.getOneCategoryById(event.getCategory().getId());

		Event existenceEvent = getOneByUrlTitle(urlTitle);

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
		event.setUrlTitle(utils.stringToUrl(event, event.getTitle()));
		event.setCategory(category);

		Category oldCategory = existenceEvent.getCategory();
		oldCategory.setAmmountEvents(oldCategory.getAmmountEvents() - 1);
		categoryService.save(oldCategory);

		return save(event);
	}

	public Event putEventPeopleEstimate(Long id, Company company) throws ServletException {

		Event event = getOneById(id);

		validateCompany(company, event);

		event.setPeopleEstimate(event.getPeopleEstimate() + 1);

		return save(event);
	}

	public Event putEventStatus(Event event, Long id, Company company) throws ServletException {

		Event existenceEvent = getOneById(id);

		validateCompany(company, existenceEvent);

		StatusEvent statusEvent = event.getStatus();

		if (statusEvent == null || (statusEvent != ACTIVE && statusEvent != CANCELED)) {
			throw new ServletException("Status de Evento informado é inválido.");
		}

		existenceEvent.setStatus(statusEvent);

		return save(existenceEvent);
	}

	public Event putEventStatusCalog(Event event, Long id) throws ServletException {

		Event existenceEvent = getOneById(id);

		CatalogStatusEvent catalogStatusEvent = event.getCatalogStatus();

		if (catalogStatusEvent == null || (catalogStatusEvent != CATALOGING && catalogStatusEvent != PUBLISHED
				&& catalogStatusEvent != REFUSED)) {
			throw new ServletException("Status de Catalogação de Evento informado é inválido.");
		}

		existenceEvent.setCatalogStatus(catalogStatusEvent);

		save(existenceEvent);

		Category category = existenceEvent.getCategory();

		updateAmmountEventsInCategory(category);

		return existenceEvent;
	}

	public Event putEventTurbineType(Event event, Long id, Company company) throws ServletException {

		Event existenceEvent = getOneById(id);

		validateCompany(company, existenceEvent);

		TurbineType turbineType = event.getTurbineType();

		if (turbineType == null || (turbineType != BRONZE && turbineType != SILVER && turbineType != GOLD)) {
			throw new ServletException("Status de Catalogação de Evento informado é inválido.");
		}

		existenceEvent.setTurbineType(turbineType);

		return save(existenceEvent);
	}

	public void deleteEvent(Long id, Company company) throws ServletException {

		Event event = getOneById(id);

		validateCompany(company, event);

		eventRepository.delete(event);
	}

	public Event getEventByUrlTitle(String url) throws ServletException {

		Event event = getOneByUrlTitle(url);

		event.setViews(event.getViews() + 1);
		save(event);
		
		return event;
	}

	public List<Event> getAllEventByCategory(String url) throws ServletException {
		
		Category category = categoryService.getOneCategoryByUrlName(url);

		List<Event> events = findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, category.getId());

		category.setAmmountEvents(events.size());
		categoryService.save(category);
		
		return events;
	}
	

	private List<Event> findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(Date date,
			CatalogStatusEvent catalogStatus, StatusEvent status, Long idCategory) {

		return eventRepository.findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(date,
				catalogStatus, status, idCategory);
	}
	
	private void updateAmmountEventsInCategory(Category category) {

		List<Event> eventsByCategory = findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, category.getId());

		category.setAmmountEvents(eventsByCategory.size());
		
		categoryService.save(category);
	}
	
	private Event getOneByUrlTitle(String url) throws ServletException {
		
		Event event = eventRepository.findByUrlTitle(url);
		
		if (event == null) {

			throw new ServletException("Evento não encontrado.");
		}
		
		return event;
	}
	
	private Set<Event> findEventByWord(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository
				.findByEventDateAfterAndCatalogStatusAndStatusAndTitleIgnoreCaseContainingOrderByEventDate(date,
						catalogEvent, status, word);
	}
	
	private Set<Event> findEventByCity(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository
				.findByEventDateAfterAndCatalogStatusAndStatusAndCityIgnoreCaseContainingOrderByEventDate(
				date, catalogEvent, status, word);
	}
	
	private Set<Event> findEventByCategory(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository
				.findByEventDateAfterAndCatalogStatusAndStatusAndCategoryNameIgnoreCaseContainingOrderByEventDate(date,
						catalogEvent, status, word);
	}
	
	private Set<Event> findEventByKeyword(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository
				.findByEventDateAfterAndCatalogStatusAndStatusAndKeywordIgnoreCaseContainingOrderByEventDate(date,
						catalogEvent, status, word);
	}
	

	private Set<Event> findEventByState(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository
				.findByEventDateAfterAndCatalogStatusAndStatusAndStateIgnoreCaseContainingOrderByEventDate(date,
						catalogEvent, status, word);
	}
	
	private void orderEventsByDate(List<Event> events) {

		Comparator<Event> comparator = new Comparator<Event>() {

			@Override
			public int compare(Event event1, Event event2) {

				if (event1.getEventDate().before(event2.getEventDate())) {
					return -1;
				}

				if (event1.getEventDate().after(event2.getEventDate())) {
					return 1;
				}

				return 0;
			}
		};

		events.sort(comparator);
	}
	
	private void validateEventFields(Event event) throws ServletException {
		
		// Verifica se o titulo é vazio ou nulo
		if (event.getTitle() == null || event.getTitle().isEmpty()) {
			throw new ServletException("O Título do evento é uma informação obrigatória.");
		}

		// Verifica se existe data no evento
		if (event.getEventDate() == null) {
			throw new ServletException("Data é uma informação obrigatória para o evento.");
		}

		// Verifica se a data do evento é antes da data atual
		if (event.getEventDate().before(new Date())) {
			throw new ServletException(
					"A data do evento não pode ser menor do que a data atual. Por favor informe uma data futura.");
		}

		// Verifica se o preço está nulo
		if (event.getPrice() == null || event.getPrice() < 0) {
			throw new ServletException("É necessário informar um preço maior ou igual a zero.");
		}

		// Verifica se a categoria está nula
		if (event.getCategory() == null) {
			throw new ServletException("Categoria é uma informação obrigatória para o evento.");
		}

		// Verifica se o local é vazio ou nulo
		if (event.getLocal().isEmpty() || event.getLocal() == null) {
			throw new ServletException("Local é uma informação obrigatória para o evento.");
		}
	}
	
	private void validateCompany(Company company, Event existenceEvent) throws ServletException {
		if (company != existenceEvent.getCompany()) {
			throw new ServletException("Empresa solicitante é diferente da empresa criadora do Evento.");
		}
	}

}