package com.rutasturisticas.restapi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Declaración de Bean para usar ModelMapper.
 */
@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
