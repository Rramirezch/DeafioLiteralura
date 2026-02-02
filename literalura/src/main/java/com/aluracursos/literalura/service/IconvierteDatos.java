package com.aluracursos.literalura.service;

public interface IconvierteDatos {
    <T> T obtenerDatos(String json, Class <T> tClass );
}
