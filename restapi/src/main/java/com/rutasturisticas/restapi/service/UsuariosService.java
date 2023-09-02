package com.rutasturisticas.restapi.service;

import java.io.File;
import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rutasturisticas.restapi.data.entity.Authority;
import com.rutasturisticas.restapi.data.entity.UsuarioEntity;
import com.rutasturisticas.restapi.data.repository.AuthorityRepository;
import com.rutasturisticas.restapi.data.repository.UsuarioRepository;
import com.rutasturisticas.restapi.dto.AuthenticationRequest;
/*
 * CLASE SERVICE QUE USAN LOS CONTROLLERS USUARIOS Y LOGIN
 */
@Service
@Transactional
public class UsuariosService {

	@Autowired
	private UsuarioRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	public void updateAvatar(MultipartFile file) throws IllegalStateException, IOException {
		file.transferTo(
				new File("/home/yisusarj/restapi/src/main/resources/static/images/" + file.getOriginalFilename()));
	}

	public boolean insertarGuiaTuristico(AuthenticationRequest request) {

		if (userRepository.findById(request.getUsername()).orElse(null) != null) {
			return false;
		}
		UsuarioEntity usuario = new UsuarioEntity();
		usuario.setUsername(request.getUsername());
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(request.getPassword());
		usuario.setPassword(encodedPassword);
		userRepository.save(usuario);
		Authority authority = new Authority();
		authority.setAuthority("GUIA_TURISTICO");
		authority.setUsuario(usuario.getUsername());
		authorityRepository.save(authority);
		return true;
	}

	public boolean insertarTurista(AuthenticationRequest request) {
		if (userRepository.findById(request.getUsername()).orElse(null) != null) {
			return false;
		}
		UsuarioEntity usuario = new UsuarioEntity();
		usuario.setUsername(request.getUsername());
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(request.getPassword());
		usuario.setPassword(encodedPassword);
		userRepository.save(usuario);
		Authority authority = new Authority();
		authority.setAuthority("TURISTA");
		authority.setUsuario(usuario.getUsername());
		authorityRepository.save(authority);
		return true;
	}

}
