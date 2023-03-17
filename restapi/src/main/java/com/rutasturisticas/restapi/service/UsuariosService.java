package com.rutasturisticas.restapi.service;

import java.io.File;
import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UsuariosService {

	public void updateAvatar(MultipartFile file) throws IllegalStateException, IOException {
		file.transferTo(
				new File("/home/yisusarj/restapi/src/main/resources/static/images/" + file.getOriginalFilename()));
	}

}
