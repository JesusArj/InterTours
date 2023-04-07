package com.rutasturisticas.restapi.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rutasturisticas.restapi.service.UsuariosService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

	@Autowired
	private UsuariosService usuariosService;

	@PutMapping("/updateAvatar")
	public ResponseEntity<String> updateAvatar(@RequestParam("file") MultipartFile file) {
		String response;
		try {
			usuariosService.updateAvatar(file);
			response = "Avatar actualizado correctamente";
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			response = "No se ha podido actualizar su avatar.";
			return new ResponseEntity<String>(response, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

}
