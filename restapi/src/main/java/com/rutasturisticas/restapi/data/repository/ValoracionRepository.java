package com.rutasturisticas.restapi.data.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rutasturisticas.restapi.data.entity.ValoracionEntity;
import com.rutasturisticas.restapi.data.entity.ValoracionEntityPK;

@Repository
public interface ValoracionRepository extends CrudRepository<ValoracionEntity, ValoracionEntityPK> {

	Optional<ValoracionEntity> findByUsuarioAndIdRuta(String usuario, Integer idRuta);

	Optional<ArrayList<ValoracionEntity>> findAllByIdRuta(Integer id);
}
