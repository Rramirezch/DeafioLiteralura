package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collector;

@JsonIgnoreProperties(ignoreUnknown = true)
    public record DatosLibro(
            @JsonAlias("title") String titulo,
            @JsonAlias("authors") List<DatosAutor> autor,
            @JsonAlias("languages")List<String> idioma,
            @JsonAlias("download_count")Double totalDescargas
    ) {

}
