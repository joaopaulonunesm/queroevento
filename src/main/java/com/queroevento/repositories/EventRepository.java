package com.queroevento.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.queroevento.enums.CatalogStatusEvent;
import com.queroevento.enums.StatusEvent;
import com.queroevento.enums.TurbineType;
import com.queroevento.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByCategoryIdOrderByEventDate(Long id);

	List<Event> findByEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(Date date, CatalogStatusEvent catalogStatusEvent,
			StatusEvent statusEvent);

	List<Event> findByOrderByPeopleEstimateDesc();

	Optional<Event> findByUrlTitle(String url);

	List<Event> findByEventDateBeforeAndCatalogStatusOrderByEventDateDesc(Date date, CatalogStatusEvent status);

	List<Event> findByCatalogStatusOrderByEventDate(CatalogStatusEvent catalogStatus);

	List<Event> findByStatusOrderByEventDate(StatusEvent status);

	List<Event> getEventByKeywordIgnoreCaseOrderByEventDate(String keyword);

	List<Event> findByCompanyIdOrderByEventDate(Long id);

	List<Event> findByEventDateAfterAndCatalogStatusAndStatusAndCategoryIdOrderByEventDate(Date date,
			CatalogStatusEvent catalogStatus, StatusEvent status, Long idCategory);

	List<Event> findByCatalogStatusAndTurbineTypeAndEventDateAfterOrderByEventDate(CatalogStatusEvent catalogStatus, TurbineType turbineType, Date date);

	Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndTitleIgnoreCaseContainingOrderByEventDate(Date date,
			CatalogStatusEvent catalogEvent, StatusEvent status, String word);

	Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndCategoryNameIgnoreCaseContainingOrderByEventDate(Date date,
			CatalogStatusEvent catalogEvent, StatusEvent status, String word);

	Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndKeywordIgnoreCaseContainingOrderByEventDate(Date date,
			CatalogStatusEvent catalogEvent, StatusEvent status, String word);

	Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndStateIgnoreCaseContainingOrderByEventDate(Date date,
			CatalogStatusEvent catalogEvent, StatusEvent status, String word);

	Set<Event> findByEventDateAfterAndCatalogStatusAndStatusAndCityIgnoreCaseContainingOrderByEventDate(Date date,
			CatalogStatusEvent catalogEvent, StatusEvent status, String word);

	List<Event> findByCompanyIdAndEventDateAfterAndCatalogStatusAndStatusOrderByEventDate(Long idCompany, Date date,
			CatalogStatusEvent catalogStatus, StatusEvent status);

	List<Event> findByCompanyIdAndEventDateAfterOrderByEventDate(Long id, Date date);

	List<Event> findByCompanyIdAndEventDateBeforeOrderByEventDate(Long id, Date date);


}