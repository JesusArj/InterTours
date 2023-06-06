package com.rutasturisticas.restapi.data.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rutasturisticas.restapi.data.entity.CoordenadasEntity;
import com.rutasturisticas.restapi.data.entity.CoordenadasEntityPK;

@Repository
public interface CoordenadasRepository extends CrudRepository<CoordenadasEntity, CoordenadasEntityPK> {

	// BUSCA POR IDRUTA
	ArrayList<CoordenadasEntity> findByIdRuta(int idRuta);

	void deleteByIdRuta(Integer id);

}
