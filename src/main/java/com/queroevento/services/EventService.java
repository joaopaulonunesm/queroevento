package com.queroevento.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.enums.CatalogStatusEvent;
import com.queroevento.enums.StatusEvent;
import com.queroevento.enums.TurbineType;
import com.queroevento.models.Category;
import com.queroevento.models.Event;
import com.queroevento.repositories.EventRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;

	public Event save(Event event) {
		return eventRepository.save(event);
	}

	public void delete(Event event) {
		eventRepository.delete(event);
	}

	public Event findOne(Long id) {
		return eventRepository.findOne(id);
	}

	public List<Event> findByEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(Date date,
			CatalogStatusEvent catalogStatusEvent, StatusEvent statusEvent) {

		return eventRepository.findByEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(date, catalogStatusEvent,
				statusEvent);
	}

	public List<Event> findByEventDateBeforeAndCatalogStatusOrderByEventDateDesc(Date date, CatalogStatusEvent status) {

		return eventRepository.findByEventDateBeforeAndCatalogStatusOrderByEventDateDesc(date, status);
	}

	public List<Event> findByCategoryIdOrderByEventDate(Long id) {

		return eventRepository.findByCategoryIdOrderByEventDate(id);
	}

	public List<Event> findByOrderByPeopleEstimateDesc() {

		return eventRepository.findByOrderByPeopleEstimateDesc();
	}

	public Event findByUrlTitle(String url) {

		return eventRepository.findByUrlTitle(url);
	}

	public List<Event> findByCatalogStatusOrderByEventDate(CatalogStatusEvent catalogStatus) {

		return eventRepository.findByCatalogStatusOrderByEventDate(catalogStatus);
	}

	public List<Event> findByStatusOrderByEventDate(StatusEvent status) {

		return eventRepository.findByStatusOrderByEventDate(status);
	}

	public List<Event> getEventByKeywordIgnoreCaseOrderByEventDate(String keyword) {

		return eventRepository.getEventByKeywordIgnoreCaseOrderByEventDate(keyword);
	}

	public List<Event> findByCompanyId(Long id) {

		return eventRepository.findByCompanyIdOrderByEventDate(id);
	}

	public List<Event> findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(Date date,
			CatalogStatusEvent catalogStatus, StatusEvent status, Long idCategory) {

		return eventRepository.findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(date,
				catalogStatus, status, idCategory);
	}

	public List<Event> findByCatalogStatusAndTurbineTypeAndEventDateAfterOrderByEventDate(
			CatalogStatusEvent catalogStatus, TurbineType turbineType, Date date) {

		return eventRepository.findByCatalogStatusAndTurbineTypeAndEventDateAfterOrderByEventDate(catalogStatus,
				turbineType, date);
	}

	public Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndTitleIgnoreCaseContainingOrderByEventDate(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository
				.findByEventDateAfterAndCatalogStatusAndStatusAndTitleIgnoreCaseContainingOrderByEventDate(date,
						catalogEvent, status, word);
	}

	public Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndCategoryNameIgnoreCaseContainingOrderByEventDate(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository
				.findByEventDateAfterAndCatalogStatusAndStatusAndCategoryNameIgnoreCaseContainingOrderByEventDate(date,
						catalogEvent, status, word);
	}

	public Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndKeywordIgnoreCaseContainingOrderByEventDate(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository
				.findByEventDateAfterAndCatalogStatusAndStatusAndKeywordIgnoreCaseContainingOrderByEventDate(date,
						catalogEvent, status, word);
	}

	public Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndStateIgnoreCaseContainingOrderByEventDate(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository
				.findByEventDateAfterAndCatalogStatusAndStatusAndStateIgnoreCaseContainingOrderByEventDate(date,
						catalogEvent, status, word);
	}

	public Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndCityIgnoreCaseContainingOrderByEventDate(
			Date date, CatalogStatusEvent catalogEvent, StatusEvent status, String word) {

		return eventRepository.findByEventDateAfterAndCatalogStatusAndStatusAndCityIgnoreCaseContainingOrderByEventDate(
				date, catalogEvent, status, word);
	}

	public List<Event> findByCompanyIdAndEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(Long idCompany,
			Date date, CatalogStatusEvent catalogStatus, StatusEvent status) {

		return eventRepository.findByCompanyIdAndEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(idCompany,
				date, catalogStatus, status);
	}

	public void updateAmmountEventsInCategory(Category category) {

		List<Event> eventsByCategory = findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, category.getId());

		category.setAmmountEvents(eventsByCategory.size());
	}

	public List<Event> findByWord(String word) {

		Set<Event> byTitle = findByEventDateAfterAndCatalogStatusAndStatusAndTitleIgnoreCaseContainingOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byCategory = findByEventDateAfterAndCatalogStatusAndStatusAndCategoryNameIgnoreCaseContainingOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byKeyword = findByEventDateAfterAndCatalogStatusAndStatusAndKeywordIgnoreCaseContainingOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byState = findByEventDateAfterAndCatalogStatusAndStatusAndStateIgnoreCaseContainingOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		Set<Event> byCity = findByEventDateAfterAndCatalogStatusAndStatusAndCityIgnoreCaseContainingOrderByEventDate(
				new Date(), CatalogStatusEvent.PUBLISHED, StatusEvent.ACTIVE, word);

		List<Event> eventsList = new ArrayList<>();
		eventsList.addAll(byTitle);
		eventsList.addAll(byCategory);
		eventsList.addAll(byKeyword);
		eventsList.addAll(byState);
		eventsList.addAll(byCity);

		orderEventsByDate(eventsList);

		return eventsList;
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

	public void validateEventFields(Event event) throws ServletException {
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