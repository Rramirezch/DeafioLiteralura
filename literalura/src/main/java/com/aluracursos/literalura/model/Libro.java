package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class Libro {
    private String titulo;
    private List<DatosAutor> autor;
    private List<String> idioma;
    private Double totalDescargas;

    public Libro(String titulo, List<DatosAutor> autor, List<String> idioma, Double totalDescargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.totalDescargas = totalDescargas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<DatosAutor> getAutor() {
        return autor;
    }

    public void setAutor(List<DatosAutor> autor) {
        this.autor = autor;
    }

    public List<String> getIdioma() {
        return idioma;
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = idioma;
    }

    public Double getTotalDescargas() {
        return totalDescargas;
    }

    public void setTotalDescargas(Double totalDescargas) {
        this.totalDescargas = totalDescargas;
    }

    @Override
    public String toString() {
        return
                "titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", idioma=" + idioma +
                ", totalDescargas=" + totalDescargas;
    }
}
