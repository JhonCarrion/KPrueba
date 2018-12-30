package com.example.usuario.kprueba.servicios;


import com.example.usuario.kprueba.response.Response_API_Lista;

public interface OnComunicacionLista {
    void respuestaListaUsuario(Response_API_Lista response);

    void errorListaUsuario(Response_API_Lista response_api);
}
