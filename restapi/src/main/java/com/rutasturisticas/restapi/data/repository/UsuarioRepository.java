package com.rutasturisticas.restapi.data.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rutasturisticas.restapi.data.entity.UsuarioEntity;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioEntity, String> {

	// DEVUELVE ENTITY POR USERNAME
	Optional<UsuarioEntity> findByUsername(String username);
}
