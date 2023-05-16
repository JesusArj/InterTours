package com.rutasturisticas.restapi.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class RutaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int idRuta;

	private String autor;

	private String titulo;

	private String descripcion;

	private String municipio;

	private String provincia;

	private ArrayList<CoordenadasDTO> coord = new ArrayList<>();

	public RutaDTO() {
	}

	public RutaDTO(String autor, String titulo, String descripcion, String municipio, String provincia) {
		super();
		this.autor = autor;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.municipio = municipio;
		this.provincia = provincia;
	}

	public int getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(int id) {
		this.idRuta = id;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

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
		return coord;
	}

	public void setCoordenadas(ArrayList<CoordenadasDTO> coordenadas) {
		this.coord = (this.coord == null) ? new ArrayList<>() : this.coord;
		coordenadas.forEach(c -> this.coord.add(c));
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

}
