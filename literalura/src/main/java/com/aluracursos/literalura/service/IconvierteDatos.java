package com.aluracursos.literalura.service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public interface IconvierteDatos {

    <T> T obtenerDatos(String json, Class <T> tClass );

}
