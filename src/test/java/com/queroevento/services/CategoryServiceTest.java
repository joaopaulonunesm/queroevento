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
import com.queroevento.models.Category;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class CategoryServiceTest {
	
	@Autowired
	private CategoryService categoryService;
	
	@Test
	public void shouldSaveCategory(){
		
		Category category = factoryCategory("Baladas");
		
		Assert.assertNotNull(categoryService.save(category));
	}
	
	@Test
	public void shouldDeleteCategory(){
		
		Category category = factoryCategory("Baladas");
		categoryService.save(category);
		
		Long id = category.getId();
		
		categoryService.delete(category);
		
		Assert.assertNull(categoryService.findOne(id));
	}
	
	@Test
	@DatabaseSetup("/category.xml")
	public void shouldFindOneCategoryById(){
		
		Assert.assertNotNull(categoryService.findOne(1L));
	}
	
	@Test
	@DatabaseSetup("/category.xml")
	public void shouldFindOneCategoryByUrlName(){
		
		Assert.assertNotNull(categoryService.findByUrlNameIgnoreCase("inauguracoes"));
	}
	
	@Test
	@DatabaseSetup("/category.xml")
	public void shouldFindAllCategoriesOrderByAmmountEvent(){
		
		List<Category> categories = categoryService.findByOrderByAmmountEventsDesc();
		
		Assert.assertEquals(3, categories.size());
		Assert.assertEquals("Shows", categories.get(0).getName());
		Assert.assertEquals("Inaugurações", categories.get(1).getName());
		Assert.assertEquals("Palestras", categories.get(2).getName());
	}

	private Category factoryCategory(String name) {
		Category category = new Category();
		category.setName(name);
		category.setUrlName(categoryService.nameToUrlName(name));
		return category;
	}
	
}
