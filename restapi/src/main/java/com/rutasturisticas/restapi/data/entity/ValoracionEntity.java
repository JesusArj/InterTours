package com.rutasturisticas.restapi.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "valoraciones")
@IdClass(ValoracionEntityPK.class)
public class ValoracionEntity {

	@Id
	@Column(name = "usuario")
	private String usuario;

	@Id
	@Column(name = "idruta")
	private Integer idRuta;

	@Column(name = "nota")
	private Integer nota;

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

	public Integer getNota() {
		return nota;
	}

	public void setNota(Integer nota) {
		this.nota = nota;
	}

}
