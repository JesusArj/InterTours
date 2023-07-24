package com.rutasturisticas.restapi.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

	public List<RutaDTO> getRutasByProvinciaAndMunicipio(String provincia, String municipio) {
		ArrayList<RutaDTO> rutasDTO = mapList(rutaRepository.findAllByProvinciaAndMunicipio(provincia, municipio),
				RutaDTO.class);
		ArrayList<CoordenadasEntity> coordenadasEntity = new ArrayList<>();
		ArrayList<CoordenadasDTO> coordenadasDTO = new ArrayList<>();
		for (RutaDTO ruta : rutasDTO) {
			coordenadasEntity.clear();
			coordenadasEntity = coordenadasRepository.findByIdRuta(ruta.getIdRuta());
			coordenadasDTO.clear();
			coordenadasDTO = mapList(coordenadasEntity, CoordenadasDTO.class);
			ruta.setCoordenadas(coordenadasDTO);
		}
		return rutasDTO;
	}

	public byte[] getAudioBytesByRutaAndOrden(Integer id, Integer orden) {
		RutaDTO ruta = getRutaById(id);
		for (CoordenadasDTO coord : ruta.getCoordenadas()) {
			if (coord.getOrden() == orden && coord.getAudio() != null) {
				return readFileToByteArray(coord.getAudio());
			}
		}
		return null;
	}

	private byte[] readFileToByteArray(String filePath) {
		try {
			Path path = Paths.get(filePath);
			return Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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
					file.transferTo(new File(buildFilePath(rutaDTO, file)));
					rutaDTO.getCoordenadas().get(Integer.parseInt(file.getOriginalFilename().substring(6)))
							.setAudio(buildFilePath(rutaDTO, file));
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}));
		insertarCoordenadas(rutaDTO);
	}

	public void editRuta(RutaDTO rutaDTO, List<MultipartFile> audios) throws IOException {
		RutaEntity rutaEntity = modelMapper.map(rutaDTO, RutaEntity.class);
		rutaRepository.save(rutaEntity);
		coordenadasRepository.deleteByIdRuta(rutaDTO.getIdRuta());
		int count = 1;
		for (CoordenadasDTO coord : rutaDTO.getCoordenadas()) {
			coord.setIdRuta(rutaDTO.getIdRuta());
			coord.setOrden(count);
			count++;
		}
		Set<String> existingAudios = listFilesUsingFilesList(RestApiConstants.AUDIOS_FOLDER_PATH + rutaDTO.getIdRuta());
		int i = 0;
		for (MultipartFile file : audios) {
			try {
				if (file != null) {
					if (!existingAudios.isEmpty()) {
						for (String audio : existingAudios) {
							if (rutaDTO.getCoordenadas().get(i).getNombreParada().length() > 6 && audio
									.startsWith(rutaDTO.getCoordenadas().get(i).getNombreParada().substring(0, 7))) {
								try {
									FileUtils.forceDelete(FileUtils.getFile(audio));
								} catch (IllegalStateException | IOException e) {
									e.printStackTrace();
								}
							} else if (rutaDTO.getCoordenadas().get(i).getNombreParada().length() <= 6
									&& audio.startsWith(rutaDTO.getCoordenadas().get(i).getNombreParada())) {
								try {
									FileUtils.forceDelete(FileUtils.getFile(audio));
								} catch (IllegalStateException | IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
					file.transferTo(new File(buildFilePath(rutaDTO, file)));
					rutaDTO.getCoordenadas().get(Integer.parseInt(file.getOriginalFilename().substring(6)))
							.setAudio(buildFilePath(rutaDTO, file));
					count++;
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		Iterable<CoordenadasEntity> it = insertarCoordenadas(rutaDTO);
		Set<String> auxAudios = listFilesUsingFilesList(RestApiConstants.AUDIOS_FOLDER_PATH + rutaDTO.getIdRuta());

		for (CoordenadasEntity coord : it) {
			if (coord.getAudio() == null) {
				for (String aux : auxAudios) {
					if (aux.startsWith(
							coord.getNombreParada().length() > 6 ? coord.getNombreParada().substring(0, 7).trim()
									: coord.getNombreParada())) {
						try {
							Path file = Paths
									.get(RestApiConstants.AUDIOS_FOLDER_PATH + rutaDTO.getIdRuta() + "/" + aux);
							Files.delete(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				if (!coord.getAudio().substring(coord.getAudio().lastIndexOf("_") + 1)
						.equals(coord.getOrden().toString())) {
					Path file = Paths.get(coord.getAudio());
					Files.move(file, file.resolveSibling(coord.getAudio().replaceAll("_\\d", "_" + coord.getOrden())));
					coord.setAudio(coord.getAudio().replaceAll("_\\d", "_" + coord.getOrden()));
				}
			}
		}

		// COMPROBAMOS QUE NINGUN AUDIO QUE NO DEBA SE QUEDE EN LA CARPETA

		Set<String> newAudios = listFilesUsingFilesList(RestApiConstants.AUDIOS_FOLDER_PATH + rutaDTO.getIdRuta());
		String auxBorrar = null;
		for (CoordenadasEntity coord : it) {
			for (String aux : newAudios) {
				if (coord.getAudio() != null && coord.getAudio()
						.equals(RestApiConstants.AUDIOS_FOLDER_PATH + rutaDTO.getIdRuta() + "/" + aux)) {
					auxBorrar = null;
					break;
				}
				if (coord.getAudio() != null) {
					auxBorrar = aux;
				}
			}
			if (auxBorrar != null) {
				Path file = Paths.get(RestApiConstants.AUDIOS_FOLDER_PATH + rutaDTO.getIdRuta() + "/" + auxBorrar);
				Files.delete(file);
			}
		}
	}

	private Set<String> listFilesUsingFilesList(String dir) throws IOException {
		try (Stream<Path> stream = Files.list(Paths.get(dir))) {
			return stream.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString)
					.collect(Collectors.toSet());
		}
	}

	private String buildFilePath(RutaDTO rutaDTO, MultipartFile file) {
		if (rutaDTO.getCoordenadas().get(Integer.parseInt(file.getOriginalFilename().substring(6))).getNombreParada()
				.length() > 6) {
			return RestApiConstants.AUDIOS_FOLDER_PATH + rutaDTO.getIdRuta() + "/"
					+ rutaDTO.getCoordenadas().get(Integer.parseInt(file.getOriginalFilename().substring(6)))
							.getNombreParada().substring(0, 7).trim()
					+ "_" + (Integer.parseInt(file.getOriginalFilename().substring(6)) + 1);
		} else {
			return RestApiConstants.AUDIOS_FOLDER_PATH
					+ rutaDTO.getIdRuta() + "/" + rutaDTO.getCoordenadas()
							.get(Integer.parseInt(file.getOriginalFilename().substring(6))).getNombreParada()
					+ "_" + (Integer.parseInt(file.getOriginalFilename().substring(6)) + 1);
		}

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

	private Iterable<CoordenadasEntity> insertarCoordenadas(RutaDTO rutaDTO) {
		ArrayList<CoordenadasEntity> coordenadasEntity = mapList(rutaDTO.getCoordenadas(), CoordenadasEntity.class);
		return coordenadasRepository.saveAll(coordenadasEntity);
	}

	<S, T> ArrayList<T> mapList(ArrayList<S> source, Class<T> targetClass) {
		return (ArrayList<T>) source.stream().map(element -> modelMapper.map(element, targetClass))
				.collect(Collectors.toList());
	}
}
