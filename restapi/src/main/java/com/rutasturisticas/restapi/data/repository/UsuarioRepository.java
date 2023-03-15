package com.rutasturisticas.restapi.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rutasturisticas.restapi.data.entity.UsuarioEntity;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioEntity, String> {

	// DEVUELVE ENTITY POR USERNAME
	UsuarioEntity findByUsername(String username);
}
