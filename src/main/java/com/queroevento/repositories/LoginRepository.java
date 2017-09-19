package com.queroevento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.queroevento.models.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

	Login findByEmail(String email);

	Login findByToken(String token);

}