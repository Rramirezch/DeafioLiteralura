package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String autor;
    private String idioma;
    private Double totalDescargas;

    // Constructor vacío requerido por JPA
    public Libro() {}

    // Constructor que toma el Record de la API y lo convierte en Entidad
    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        // Tomamos el primer autor de la lista y su nombre
        this.autor = datosLibro.autor().isEmpty() ? "Autor desconocido" : datosLibro.autor().get(0).nombre();
        // Tomamos el primer idioma de la lista
        this.idioma = datosLibro.idioma().isEmpty() ? "Desconocido" : datosLibro.idioma().get(0);
        this.totalDescargas = datosLibro.totalDescargas();
    }


    // Getters (necesarios para que Spring/JPA lean los datos)
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getNombreAutor() { return autor; }
    public String getIdioma() { return idioma; }
    public Double getTotalDescargas() { return totalDescargas; }

}
