package com.queroevento.services;

import java.util.List;

import javax.servlet.ServletException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.Category;
import com.queroevento.repositories.CategoryRepository;
import com.queroevento.utils.Utils;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final Utils utils;

	public Category save(Category category) {
		return categoryRepository.save(category);
	}
	
	public Category postCategory(Category category) throws ServletException {

		if (categoryRepository.findByName(category.getName()) != null) {
			throw new ServletException("Já existe uma Categoria com o nome informado.");
		}

		category.setUrlName(utils.stringToUrl(category.getName()));

		return save(category);
	}

	public void deleteCategory(Long id) throws ServletException {

		categoryRepository.delete(getOneCategoryById(id));
	}

	public Category putCategory(Category category, String urlName) throws ServletException {

		Category existingCategory = getOneCategoryByUrlName(urlName);

		category.setId(existingCategory.getId());

		return save(category);
	}

	public Category getOneCategoryByUrlName(String urlName) throws ServletException {
		return categoryRepository.findByUrlNameIgnoreCase(urlName).orElseThrow(() -> new ServletException("Nome URL da Categoria não encontrada."));
	}
	
	public Category getOneCategoryById(Long id) throws ServletException {
		return categoryRepository.findById(id).orElseThrow(() -> new ServletException("ID da Categoria informada não encontrada."));
	}

	public List<Category> findByAmmountEventsGreaterThanOrderByAmmountEventsDesc(int greaterThen) {
		return categoryRepository.findByAmmountEventsGreaterThanOrderByAmmountEventsDesc(greaterThen);
	}

	public List<Category> findByOrderByAmmountEventsDesc() {
		return categoryRepository.findByOrderByAmmountEventsDesc();
	}

}
