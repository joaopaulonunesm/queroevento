package com.queroevento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.queroevento.models.Login;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

	Optional<Login> findByEmail(String email);

	Optional<Login> findByToken(String token);

}