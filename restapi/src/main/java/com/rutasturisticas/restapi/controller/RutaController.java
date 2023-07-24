package com.rutasturisticas.restapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rutasturisticas.restapi.dto.CoordenadasDTO;
import com.rutasturisticas.restapi.dto.RutaDTO;
import com.rutasturisticas.restapi.service.RutaService;
import com.rutasturisticas.restapi.util.JwtUtil;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {

	@Autowired
	private RutaService rutaService;

	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/detalleRuta/{id}")
	public ResponseEntity<RutaDTO> getRutaById(@PathVariable("id") int id) {
		RutaDTO rutaEntity = rutaService.getRutaById(id);
		return new ResponseEntity<RutaDTO>(rutaEntity, HttpStatus.OK);
	}

	@GetMapping("/detalleRuta")
	public ResponseEntity<List<RutaDTO>> getRutaByProvinciaAndMunicipio(@RequestParam("provincia") String provincia,
			@RequestParam("municipio") String municipio) {
		List<RutaDTO> rutaEntity = rutaService.getRutasByProvinciaAndMunicipio(provincia, municipio);
		return new ResponseEntity<List<RutaDTO>>(rutaEntity, HttpStatus.OK);
	}

	@GetMapping("/{autor}")
	public ResponseEntity<List<RutaDTO>> getRutasByAutor(@PathVariable("autor") String autor) {
		List<RutaDTO> rutaEntity = rutaService.getRutasByAutor(autor);
		return new ResponseEntity<List<RutaDTO>>(rutaEntity, HttpStatus.OK);
	}

	@GetMapping("/audio")
	public ResponseEntity<byte[]> getAudioByRutaAndOrden(@RequestParam("idRuta") Integer id,
			@RequestParam("orden") Integer orden) {
		byte[] audioData = rutaService.getAudioBytesByRutaAndOrden(id, orden); // Obtener el audio como byte[]
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return audioData != null ? new ResponseEntity<>(audioData, headers, HttpStatus.OK)
				: ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

	}

	@PostMapping("/insertarRuta/")
	public ResponseEntity<String> insertRuta(MultipartHttpServletRequest request,
			@CookieValue(name = "jwt") String token) {
		String response = null;
		List<MultipartFile> audios = request.getFiles("audios");

		RutaDTO rutaDTO = new RutaDTO(request.getParameter("autor"), request.getParameter("titulo"),
				request.getParameter("descripcion"), request.getParameter("municipio"),
				request.getParameter("provincia"));
		String coordenadasDTOJson = request.getParameter("coordenadas");
		try {
			ArrayList<CoordenadasDTO> coordenadas = new ObjectMapper().readValue(coordenadasDTOJson,
					new TypeReference<ArrayList<CoordenadasDTO>>() {
					});
			rutaDTO.setCoordenadas(coordenadas);
		} catch (JsonProcessingException e1) {
			return new ResponseEntity<String>("Error al procesar las coordenadas", HttpStatus.BAD_REQUEST);
		}

		if (!rutaDTO.getAutor().equals(jwtUtil.getUsernameFromToken(token))) {
			response = "USUARIO NO AUTORIZADO";
			return new ResponseEntity<String>(response, HttpStatus.UNAUTHORIZED);
		}
		try {
			rutaService.insertRuta(rutaDTO, audios);
			response = "Ruta insertada correctamente";
		} catch (Exception e) {
			response = "Error al insertar ruta --> " + e;
			return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@DeleteMapping("/eliminarRuta/{id}")
	public ResponseEntity<String> deleteRuta(@PathVariable("id") int id, @CookieValue(name = "jwt") String token) {
		String response = null;
		RutaDTO ruta = rutaService.getRutaById(id);
		if (!ruta.getAutor().equals(jwtUtil.getUsernameFromToken(token))) {
			response = "USUARIO NO AUTORIZADO";
			return new ResponseEntity<String>(response, HttpStatus.UNAUTHORIZED);
		}
		try {
			rutaService.deleteRutaById(id);
			response = "Ruta eliminada correctamente";
		} catch (Exception e) {
			response = "Error al eliminar ruta --> " + e;
			return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	// Post porque MultipartHttpServletRequest solo permite POST
	@PostMapping("/editarRuta/{id}")
	public ResponseEntity<?> editRuta(@PathVariable("id") Integer id, MultipartHttpServletRequest request,
			@CookieValue(name = "jwt") String token) {
		RutaDTO updateRuta = new RutaDTO();
		List<MultipartFile> audios = request.getFiles("audios");
		try {
			updateRuta = rutaService.getRutaById(id);
			if (!updateRuta.getAutor().equals(jwtUtil.getUsernameFromToken(token))
					|| updateRuta.getIdRuta() != Integer.parseInt(request.getParameter("idRuta"))) {
				return new ResponseEntity<RutaDTO>(new RutaDTO(), HttpStatus.UNAUTHORIZED);
			}
			updateRuta.setTitulo(request.getParameter("titulo"));
			updateRuta.setDescripcion(request.getParameter("descripcion"));
			updateRuta.setMunicipio(request.getParameter("municipio"));
			updateRuta.setProvincia(request.getParameter("provincia"));
			updateRuta.getCoordenadas().clear();
			String coordenadasDTOJson = request.getParameter("coordenadas");
			try {
				ArrayList<CoordenadasDTO> coordenadas = new ObjectMapper().readValue(coordenadasDTOJson,
						new TypeReference<ArrayList<CoordenadasDTO>>() {
						});
				updateRuta.setCoordenadas(coordenadas);
			} catch (JsonProcessingException e1) {
				return new ResponseEntity<String>("Error al procesar las coordenadas", HttpStatus.BAD_REQUEST);
			}
			rutaService.editRuta(updateRuta, audios);
		} catch (Exception e) {
			return new ResponseEntity<RutaDTO>(updateRuta, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<RutaDTO>(updateRuta, HttpStatus.OK);
	}

}
