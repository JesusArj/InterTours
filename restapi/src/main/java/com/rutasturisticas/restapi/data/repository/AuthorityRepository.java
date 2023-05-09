package com.rutasturisticas.restapi.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rutasturisticas.restapi.data.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}