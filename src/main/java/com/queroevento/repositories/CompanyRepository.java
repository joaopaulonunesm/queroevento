package com.queroevento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.queroevento.models.Company;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findByUrlName(String urlName);

	Optional<Company> findByNameIgnoreCase(String name);

}