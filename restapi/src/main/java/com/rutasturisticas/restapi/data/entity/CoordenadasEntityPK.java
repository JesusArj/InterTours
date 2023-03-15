package com.rutasturisticas.restapi.data.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class CoordenadasEntityPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idRuta;

	private int orden;

	public int getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(int idRuta) {
		this.idRuta = idRuta;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}
}
