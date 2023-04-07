package com.rutasturisticas.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rutasturisticas.restapi.dto.RutaDTO;
import com.rutasturisticas.restapi.service.RutaService;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {

	@Autowired
	private RutaService rutaService;

	@GetMapping("/detalleRuta/{id}")
	public ResponseEntity<RutaDTO> getRutaById(@PathVariable("id") int id) {
		RutaDTO rutaEntity = rutaService.getRutaById(id);
		return new ResponseEntity<RutaDTO>(rutaEntity, HttpStatus.OK);
	}

	@PutMapping("/insertarRuta/")
	public ResponseEntity<String> insertRuta(@RequestBody RutaDTO rutaDTO) {
		String response = null;
		try {
			rutaService.insertRuta(rutaDTO);
			response = "Ruta insertada correctamente";

		} catch (Exception e) {
			response = "Error al insertar ruta --> " + e;
			return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

}
