package com.rutasturisticas.restapi.dto;

import java.util.ArrayList;

public class EditRutaRequest {

	private String titulo;

	private String descripcion;

	private ArrayList<CoordenadasDTO> coordenadas;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ArrayList<CoordenadasDTO> getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(ArrayList<CoordenadasDTO> coordenadas) {
		this.coordenadas = coordenadas;
	}

}
