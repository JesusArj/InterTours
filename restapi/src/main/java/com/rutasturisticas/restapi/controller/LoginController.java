package com.rutasturisticas.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rutasturisticas.restapi.data.entity.UsuarioEntity;
import com.rutasturisticas.restapi.dto.AuthenticationRequest;
import com.rutasturisticas.restapi.service.UsuariosService;
import com.rutasturisticas.restapi.util.JwtUtil;

import ch.qos.logback.core.util.Duration;
import io.jsonwebtoken.ExpiredJwtException;

/*
 * CONTROLLER QUE CONTIENE SERVICIOS RELACIONADOS CON LA AUTENTICACIÓN Y REGISTRO DE USUARIOS
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Value("${cookies.domain}")
	private String domain;

	@Autowired
	private UsuariosService usuariosService;

	//Login de usuario. Devuelve un JWT que pasa a ser válido para ese usuario en la aplicación.
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
		try {
			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			UsuarioEntity user = (UsuarioEntity) authenticate.getPrincipal();
			user.setPassword(null);

			String token = jwtUtil.generateToken(user);
			ResponseCookie cookie = ResponseCookie.from("jwt", token).domain(domain).path("/")
					.maxAge(Duration.buildByDays(365).getMilliseconds()).build();
			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(token);
		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
		}
	}

	//Registra un usuario asignándole el rol de guía turístico. Consumido por la aplicación web.
	@PostMapping("/registerGuiaTuristico")
	public ResponseEntity<?> register(@RequestBody AuthenticationRequest request) {
		return usuariosService.insertarGuiaTuristico(request) ? ResponseEntity.status(HttpStatus.OK).build()
				: ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
	
	//Registra un usuario asignándole el rol de turista. Consumido por la aplicación móvil.
	@PostMapping("/registerTurista")
	public ResponseEntity<?> registerTurista(@RequestBody AuthenticationRequest request) {
		return usuariosService.insertarTurista(request) ? ResponseEntity.status(HttpStatus.OK).build()
				: ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
	
	//Valida que un token es válido, teniendo en cuenta los datos de inicio de sesión del usuario que consume el servicio.
	//Consumido por la aplicación web.
	@GetMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestParam("token") String token,
			@AuthenticationPrincipal UsuarioEntity user) {
		try {
			Boolean isValidToken = jwtUtil.validateToken(token, user);
			return ResponseEntity.ok(isValidToken);
		} catch (ExpiredJwtException e) {
			return ResponseEntity.ok(false);
		}
	}

	//Valida que un token es válido, sin el usuario/contraseña. Consumido por la aplicación móvil.
	@GetMapping("/validateToken")
	public ResponseEntity<?> validateTokenWithoutUser(@RequestParam("token") String token) {
		try {
			Boolean isValidToken = jwtUtil.validateTokenWithoutUser(token);
			return ResponseEntity.ok(isValidToken);
		} catch (ExpiredJwtException e) {
			return ResponseEntity.ok(false);
		}
	}

}
