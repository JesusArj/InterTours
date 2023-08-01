package com.rutasturisticas.restapi.data.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ValoracionEntityPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private String usuario;

	private Integer idRuta;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(Integer idRuta) {
		this.idRuta = idRuta;
	}

}
