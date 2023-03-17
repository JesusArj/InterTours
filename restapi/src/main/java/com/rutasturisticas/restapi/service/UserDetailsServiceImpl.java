package com.rutasturisticas.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rutasturisticas.restapi.data.entity.UsuarioEntity;
import com.rutasturisticas.restapi.util.CustomPasswordEncoder;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private CustomPasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UsuarioEntity user = new UsuarioEntity();
		user.setUsername(username);
		user.setPassword(passwordEncoder.getPasswordEncoder().encode("asdfasdf"));
		return user;
	}

}
