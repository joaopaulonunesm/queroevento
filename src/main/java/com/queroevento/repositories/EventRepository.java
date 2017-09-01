package com.queroevento.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.queroevento.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByCategoryIdOrderByEventDate(Long id);

	List<Event> findByOrderByEventDate();

	List<Event> findByOrderByPeopleEstimateDesc();

	Event findByUrlTitle(String url);

}