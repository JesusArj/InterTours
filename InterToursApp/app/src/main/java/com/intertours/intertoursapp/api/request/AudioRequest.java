package com.intertours.intertoursapp.api.request;

public class AudioRequest {
    private Integer idRuta;
    private Integer orden;

    public AudioRequest(Integer idRuta, Integer orden) {
        this.idRuta = idRuta;
        this.orden = orden;
    }

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
