package com.rutasturisticas.restapi.data.entity;

import java.io.Serializable;

//import org.locationtech.jts.awt.PointShapeFactory.Point;

//import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity(name = "coordenadas")
@IdClass(CoordenadasEntityPK.class)
public class CoordenadasEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "idruta", unique = true, nullable = false)
	@Id
	private int idRuta;

	@Column(name = "orden", nullable = false)
	@Id
	private int orden;

	@Column(name = "latitud", nullable = false)
	private double latitud;

	@Column(name = "longitud", nullable = false)
	private double longitud;

	@Column(name = "audio", nullable = true)
	private String audio;

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

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

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

}
