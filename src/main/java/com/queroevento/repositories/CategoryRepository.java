package com.queroevento.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.queroevento.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByUrlNameIgnoreCase(String urlName);

	List<Category> findByAmmountEventsGreaterThanOrderByAmmountEventsDesc(int greaterThen);

	Optional<Category> findByName(String name);

	List<Category> findByOrderByAmmountEventsDesc();

}