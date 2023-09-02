package com.rutasturisticas.restapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
/*
 * CLASE QUE AYUDA A CODIFICAR LAS CONSTRASEÑAS 
 */

@Component
public class CustomPasswordEncoder {
	private PasswordEncoder passwordEncoder;

	public CustomPasswordEncoder() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

}
