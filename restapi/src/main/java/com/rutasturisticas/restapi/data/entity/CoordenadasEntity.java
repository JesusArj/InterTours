package com.rutasturisticas.restapi.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "coordenadas")
@IdClass(CoordenadasEntityPK.class)
public class CoordenadasEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "idruta", unique = true, nullable = false)
	@Id
	private int idRuta;

	@Column(name = "orden", nullable = false)
	@Id
	private Integer orden;

	@Column(name = "latitud", nullable = false)
	private double latitud;

	@Column(name = "longitud", nullable = false)
	private double longitud;

	@Column(name = "audio", nullable = true)
	private String audio;

	@Column(name = "descripcion", nullable = false)
	private String descripcionParada;

	@Column(name = "nombre", nullable = false)
	private String nombreParada;

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

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
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

	public String getDescripcionParada() {
		return descripcionParada;
	}

	public void setDescripcionParada(String descripcionParada) {
		this.descripcionParada = descripcionParada;
	}

	public String getNombreParada() {
		return nombreParada;
	}

	public void setNombreParada(String nombreParada) {
		this.nombreParada = nombreParada;
	}

}
