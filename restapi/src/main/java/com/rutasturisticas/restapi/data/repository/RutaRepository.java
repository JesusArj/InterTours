package com.rutasturisticas.restapi.data.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rutasturisticas.restapi.data.entity.RutaEntity;

@Repository
public interface RutaRepository extends CrudRepository<RutaEntity, Integer> {

	// Devuelve ruta por Id
	RutaEntity findByIdRuta(Integer id);

	// Devuelve rutas por autor
	List<RutaEntity> findAllByAutor(String autor);

	ArrayList<RutaEntity> findAllByProvinciaAndMunicipioAndPais(String provincia, String municipio, String pais);
}
