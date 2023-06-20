package com.rutasturisticas.restapi.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rutasturisticas.restapi.data.entity.CoordenadasEntity;
import com.rutasturisticas.restapi.data.entity.RutaEntity;
import com.rutasturisticas.restapi.data.repository.CoordenadasRepository;
import com.rutasturisticas.restapi.data.repository.RutaRepository;
import com.rutasturisticas.restapi.dto.CoordenadasDTO;
import com.rutasturisticas.restapi.dto.RutaDTO;
import com.rutasturisticas.restapi.util.RestApiConstants;

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

	public Resource getAudiosByRuta(Integer id) {
		RutaDTO ruta = getRutaById(id);
		List<Resource> audioResources = new ArrayList<>();
		ruta.getCoordenadas().forEach(stop -> {
			if (stop.getAudio() != null) {
				audioResources.add(new FileSystemResource(stop.getAudio()));
			}
		});
		return audioResources.get(1);
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

	public void insertRuta(RutaDTO rutaDTO, List<MultipartFile> audios) {
		RutaEntity rutaEntity = modelMapper.map(rutaDTO, RutaEntity.class);
		int idRuta = rutaRepository.save(rutaEntity).getIdRuta();
		rutaDTO.setIdRuta(idRuta);
		rutaDTO.getCoordenadas().forEach(c -> {
			c.setIdRuta(rutaDTO.getIdRuta());
		});
		audios.forEach(withCounter((i, file) -> {
			try {
				if (file != null) {
					file.transferTo(new File(RestApiConstants.AUDIOS_FOLDER_PATH + rutaDTO.getIdRuta() + "/"
							+ rutaDTO.getCoordenadas().get(Integer.parseInt(file.getOriginalFilename().substring(6)))
									.getNombreParada().substring(0, 7).trim()
							+ "_" + (Integer.parseInt(file.getOriginalFilename().substring(6)) + 1)));
					rutaDTO.getCoordenadas().get(Integer.parseInt(file.getOriginalFilename().substring(6)))
							.setAudio(RestApiConstants.AUDIOS_FOLDER_PATH + rutaDTO.getIdRuta() + "/"
									+ rutaDTO.getCoordenadas()
											.get(Integer.parseInt(file.getOriginalFilename().substring(6)))
											.getNombreParada().substring(0, 7).trim()
									+ "_" + (Integer.parseInt(file.getOriginalFilename().substring(6)) + 1));
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}));
		insertarCoordenadas(rutaDTO);
	}

	public void editRuta(RutaDTO rutaDTO) {
		RutaEntity rutaEntity = modelMapper.map(rutaDTO, RutaEntity.class);
		rutaRepository.save(rutaEntity);
		coordenadasRepository.deleteByIdRuta(rutaDTO.getIdRuta());
		insertarCoordenadas(rutaDTO);
	}

	private static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
		AtomicInteger counter = new AtomicInteger(0);
		return item -> consumer.accept(counter.getAndIncrement(), item);
	}

	public void deleteRutaById(int idRuta) {
		File file = new File(RestApiConstants.AUDIOS_FOLDER_PATH + idRuta);
		try {
			if (file != null) {
				FileUtils.deleteDirectory(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
