package com.queroevento.controllers;

import com.queroevento.models.Category;
import com.queroevento.services.CategoryService;
import com.queroevento.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CompanyService companyService;

    @RequestMapping(value = "v1/categories", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> postCategory(@RequestHeader(value = "Authorization") String token,
                                                 @RequestBody Category category) throws ServletException {
        companyService.validateCompanyModeratorByToken(token);
        return new ResponseEntity<>(categoryService.postCategory(category), HttpStatus.CREATED);
    }

    @RequestMapping(value = "v1/categories/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Category> deleteCategory(@RequestHeader(value = "Authorization") String token,
                                                   @PathVariable Long id) throws ServletException {
        companyService.validateCompanyModeratorByToken(token);
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "v1/categories/{urlName}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> putCategory(@RequestHeader(value = "Authorization") String token,
                                                @RequestBody Category category, @PathVariable String urlName) throws ServletException {
        companyService.validateCompanyModeratorByToken(token);
        return new ResponseEntity<>(categoryService.putCategory(category, urlName), HttpStatus.OK);
    }

    @RequestMapping(value = "/categories/{urlName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getOneCategoryByUrlName(@PathVariable String urlName) throws ServletException {
        return new ResponseEntity<>(categoryService.getOneCategoryByUrlName(urlName), HttpStatus.OK);
    }

    @RequestMapping(value = "/categories/greaterthanzero", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> getCategoriesGreaterThanZero() {
        return new ResponseEntity<>(categoryService.findByAmmountEventsGreaterThanOrderByAmmountEventsDesc(0), HttpStatus.OK);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> getCategories() {
        return new ResponseEntity<>(categoryService.findByOrderByAmmountEventsDesc(), HttpStatus.OK);
    }
}