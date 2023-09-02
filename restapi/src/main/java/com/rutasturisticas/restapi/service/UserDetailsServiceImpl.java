package com.rutasturisticas.restapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rutasturisticas.restapi.data.entity.UsuarioEntity;
import com.rutasturisticas.restapi.data.repository.UsuarioRepository;
/*
 * CLASE SERVICE QUE IMPLEMENTA UserDetailsService (REQUERIDO POR SPRING SECURITY)
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	//Método sobreescrito para obtener el usuario de nuestro repository de usuarios
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UsuarioEntity> optionalUser = usuarioRepository.findByUsername(username);
		return optionalUser.orElseThrow(() -> new UsernameNotFoundException("Credenciales Inválidas"));
	}

}
