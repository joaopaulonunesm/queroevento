package com.queroevento.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.Category;
import com.queroevento.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	public void delete(Category category) {
		categoryRepository.delete(category);
	}

	public Category findOne(Long id) {
		return categoryRepository.findOne(id);
	}

	public Category findByUrlNameIgnoreCase(String urlName) {
		return categoryRepository.findByUrlNameIgnoreCase(urlName);
	}

	public List<Category> findByAmmountEventsGreaterThanOrderByAmmountEventsDesc(int greaterThen) {
		return categoryRepository.findByAmmountEventsGreaterThanOrderByAmmountEventsDesc(greaterThen);
	}

	public Category findByName(String name) {
		return categoryRepository.findByName(name);
	}

	public List<Category> findByOrderByAmmountEventsDesc() {
		return categoryRepository.findByOrderByAmmountEventsDesc();
	}

}