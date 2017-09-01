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
	
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Category findByNameIgnoreCase(String name) {
		return categoryRepository.findByNameIgnoreCase(name);
	}

}