package com.rutasturisticas.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rutasturisticas.restapi.data.entity.CoordenadasEntity;
import com.rutasturisticas.restapi.data.entity.RutaEntity;
import com.rutasturisticas.restapi.data.repository.CoordenadasRepository;
import com.rutasturisticas.restapi.data.repository.RutaRepository;
import com.rutasturisticas.restapi.dto.CoordenadasDTO;
import com.rutasturisticas.restapi.dto.RutaDTO;

@Service
@Transactional
public class RutaService {

	@Autowired
	private RutaRepository rutaRepository;

	@Autowired
	private CoordenadasRepository coordenadasRepository;

	@Autowired
	private ModelMapper modelMapper;

	public RutaDTO getRutaById(int id) {
		RutaEntity ruta = rutaRepository.findByIdRuta(id);
		ArrayList<CoordenadasEntity> coordenadasEntity = coordenadasRepository.findByIdRuta(id);
		ArrayList<CoordenadasDTO> coordenadasDTO = mapList(coordenadasEntity, CoordenadasDTO.class);
		RutaDTO dto = modelMapper.map(ruta, RutaDTO.class);
		dto.setCoordenadas(coordenadasDTO);
		return dto;
	}

	public List<RutaDTO> getRutasByAutor(String autor) {
		ArrayList<RutaEntity> rutas = (ArrayList<RutaEntity>) rutaRepository.findAllByAutor(autor);
		List<RutaDTO> rutasDTO = mapList(rutas, RutaDTO.class);
		rutasDTO.forEach(entity -> {
			entity.setCoordenadas(
					mapList(coordenadasRepository.findByIdRuta(entity.getIdRuta()), CoordenadasDTO.class));
		});
		return rutasDTO;
	};

	public void insertRuta(RutaDTO rutaDTO) {
		RutaEntity rutaEntity = modelMapper.map(rutaDTO, RutaEntity.class);
		int idRuta = rutaRepository.save(rutaEntity).getIdRuta();
		rutaDTO.setIdRuta(idRuta);
		rutaDTO.getCoordenadas().forEach(c -> c.setIdRuta(rutaDTO.getIdRuta()));
		insertarCoordenadas(rutaDTO);
	}

	public void deleteRutaById(int idRuta) {
		rutaRepository.deleteById(idRuta);
	}

	private void insertarCoordenadas(RutaDTO rutaDTO) {
		ArrayList<CoordenadasEntity> coordenadasEntity = mapList(rutaDTO.getCoordenadas(), CoordenadasEntity.class);
		coordenadasRepository.saveAll(coordenadasEntity);
	}

	<S, T> ArrayList<T> mapList(ArrayList<S> source, Class<T> targetClass) {
		return (ArrayList<T>) source.stream().map(element -> modelMapper.map(element, targetClass))
				.collect(Collectors.toList());
	}
}
