package com.rutasturisticas.restapi.dto;

import java.io.Serializable;

public class DeleteRutaRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idRuta;

	public Integer getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(Integer idRuta) {
		this.idRuta = idRuta;
	}

}
