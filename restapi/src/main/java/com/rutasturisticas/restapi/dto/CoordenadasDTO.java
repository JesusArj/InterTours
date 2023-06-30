package com.rutasturisticas.restapi.dto;

import java.io.Serializable;

public class CoordenadasDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idRuta;

	private Integer orden;

	private double latitud;

	private double longitud;

	private String nombreParada;

	private String descripcionParada;

	private String audio;

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	/**
	 * @return the audio
	 */
	public String getAudio() {
		return audio;
	}

	/**
	 * @param audio the audio to set
	 */
	public void setAudio(String audio) {
		this.audio = audio;
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

	public int getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(int idRuta) {
		this.idRuta = idRuta;
	}

	public String getNombreParada() {
		return nombreParada;
	}

	public void setNombreParada(String nombreParada) {
		this.nombreParada = nombreParada;
	}

	public String getDescripcionParada() {
		return descripcionParada;
	}

	public void setDescripcionParada(String descripcionParada) {
		this.descripcionParada = descripcionParada;
	}

}
