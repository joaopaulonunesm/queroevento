package com.queroevento.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.queroevento.models.Event;
import com.queroevento.utils.CatalogStatusEvent;
import com.queroevento.utils.StatusEvent;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByCategoryIdOrderByEventDate(Long id);

	List<Event> findByEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(Date date, CatalogStatusEvent catalogStatusEvent,
			StatusEvent statusEvent);

	List<Event> findByOrderByPeopleEstimateDesc();

	Event findByUrlTitle(String url);

	List<Event> findByEventDateBeforeOrderByEventDateDesc(Date date);

	List<Event> findByTurbineTypeIsNotNullOrderByTurbineTypeDesc();

	List<Event> findByCatalogStatusOrderByEventDate(CatalogStatusEvent catalogStatus);

	List<Event> findByStatusOrderByEventDate(StatusEvent status);

	List<Event> getEventByKeywordIgnoreCaseOrderByEventDate(String keyword);

	List<Event> findByUserIdOrderByEventDate(Long id);

	List<Event> findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(Date date,
			CatalogStatusEvent catalogStatus, StatusEvent status, Long idCategory);


}