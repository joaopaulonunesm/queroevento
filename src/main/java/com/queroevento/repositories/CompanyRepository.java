package com.queroevento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.queroevento.models.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	Company findByUrlName(String urlName);

	Company findByNameIgnoreCase(String name);

}