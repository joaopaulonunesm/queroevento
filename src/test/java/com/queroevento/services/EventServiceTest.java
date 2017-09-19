package com.queroevento.services;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.queroevento.models.Category;
import com.queroevento.models.Company;
import com.queroevento.models.Event;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class EventServiceTest {

	@Autowired
	private EventService eventService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CompanyService companyService;

	@Test
	public void shouldSaveEvent() {

		Event event = factoryEvent("Inauguração", "Avenida são paulo", 20.00, new Date());

		Assert.assertNotNull(eventService.save(event));
	}

	@Test
	public void shoulDeleteEvent() {

		Event event = factoryEvent("Inauguração", "Avenida são paulo", 20.00, new Date());

		eventService.save(event);
		Long id = event.getId();

		eventService.delete(event);

		Assert.assertNull(categoryService.findOne(id));
	}
	
	@Test
	@DatabaseSetup("/event.xml")
	public void shoulFindOneEventById() {

		Assert.assertNotNull(eventService.findOne(1L));
	}

	@Test
	@DatabaseSetup("/company.xml")
	@DatabaseSetup("/category.xml")
	@DatabaseSetup("/event.xml")
	public void shouldFindEventsByCompany() {
		
		List<Event> events = eventService.findByCompanyId(1L);

		Assert.assertEquals(2, events.size());
	}

	private Event factoryEvent(String title, String local, double price, Date date) {

		Company company = new Company();
		company.setName("Quero Evento");
		companyService.save(company);
		
		Category category = new Category();
		category.setName("Test");
		categoryService.save(category);

		Event event = new Event();
		event.setTitle(title);
		event.setCompany(company);
		event.setCategory(category);
		event.setLocal(local);
		event.setPrice(price);
		event.setEventDate(date);
		return event;
	}
}
