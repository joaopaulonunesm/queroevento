package com.queroevento.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.queroevento.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByUrlNameIgnoreCase(String urlName);

	List<Category> findByOrderByAmmountEventsDesc();

}