package com.aluracursos.literalura.service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IconvierteDatos{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    //el @override queda en rojo, agrego(implements IconvierteDatos) y se soluciona
    public <T> T obtenerDatos(String json, Class<T> tClass) {
        //cambiamos null por objectMapper.readValue(json, tClase), que recibe los atributos json y tClass
        try {
            return objectMapper.readValue(json, tClass);
            //hago un try catch por si hay fallas en la obtencion de los datos
        }catch(JacksonException e){
            throw new RuntimeException(e);
        }

    }
}
