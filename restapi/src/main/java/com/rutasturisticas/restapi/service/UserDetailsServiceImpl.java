package com.rutasturisticas.restapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rutasturisticas.restapi.data.entity.UsuarioEntity;
import com.rutasturisticas.restapi.data.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UsuarioEntity> optionalUser = usuarioRepository.findByUsername(username);
		return optionalUser.orElseThrow(() -> new UsernameNotFoundException("Credenciales Inválidas"));
	}

}
