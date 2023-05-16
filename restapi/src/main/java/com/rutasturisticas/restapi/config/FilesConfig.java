package com.rutasturisticas.restapi.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@EnableAutoConfiguration
@Configuration
public class FilesConfig {
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(5 * 1024 * 1024); // Tamaño máximo de archivo en bytes (5MB)
		return resolver;
	}
}
