package com.queroevento.services;

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
import com.queroevento.models.Event;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class EventServiceTest {
	
	@Autowired
	private EventService eventService;
	
	@Test
	@DatabaseSetup("/company.xml")
	@DatabaseSetup("/category.xml")
	@DatabaseSetup("/event.xml")
	public void shouldFindEventsByCompany(){
		List<Event> events = eventService.findByCompanyId(1L);
		Assert.assertEquals(2, events.size());
	}

}
