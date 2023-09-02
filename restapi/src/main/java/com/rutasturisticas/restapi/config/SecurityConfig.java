package com.rutasturisticas.restapi.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rutasturisticas.restapi.filter.JwtFilter;
import com.rutasturisticas.restapi.util.CustomPasswordEncoder;

/*
 * ARCHIVO DE CONFIGURACIÓN DE SEGURIDAD DE LA API. 
 * SOBREESCRIBIMOS FUNCIONES DE SPRING SECURITY PARA ADAPTAR AL COMPORTAMIENTO DESEADO.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private CustomPasswordEncoder customPasswordEncoder;

	@Autowired
	private JwtFilter jwtFilter;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(customPasswordEncoder.getPasswordEncoder());
	}

	//Función principal de configuración de seguridad HTTP
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http = http.csrf().disable().cors().disable();

		http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
		//Se devuelve un código de respuesta 401 UNAUTHORIZED en caso de no tener un jwt válido para atacar al endpoint
		http = http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		}).and();
		//Permitimos request sin JWT los endpoints de login y registro. (/api/auth/** ---> LoginController.java)
		http.authorizeRequests().antMatchers("/api/auth/**").permitAll().anyRequest().authenticated();
		//Añadimos nuestro jwtFilter
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
