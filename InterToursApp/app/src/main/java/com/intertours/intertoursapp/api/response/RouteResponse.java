package com.intertours.intertoursapp.api.response;

import java.util.ArrayList;

public class RouteResponse {
    private int idRuta;

    private String autor;

    private String titulo;

    private String descripcion;

    private String municipio;

    private String provincia;

    private ArrayList<CoordenadasDTO> coordenadas = new ArrayList<>();

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
        return coordenadas;
    }

    public void setCoordenadas(ArrayList<CoordenadasDTO> coordenadas) {
        this.coordenadas = (this.coordenadas == null) ? new ArrayList<>() : this.coordenadas;
        coordenadas.forEach(c -> this.coordenadas.add(c));
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
