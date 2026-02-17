package com.aluracursos.literalura.model;


import jakarta.persistence.*;


@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Double totalDescargas;
    @ManyToOne
    private Autor autor;

    // Constructor vacío requerido por JPA
    public Libro() {}

    // Constructor que toma el Record de la API y lo convierte en Entidad
    /*public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        // Tomamos el primer autor de la lista y su nombre
        //this.autor = datosLibro.autor().get(0).nombre();
        // Tomamos el primer idioma de la lista
        this.idioma = datosLibro.idioma().isEmpty() ? "Desconocido" : datosLibro.idioma().get(0);
        this.totalDescargas = datosLibro.totalDescargas();
    }*/
    public Libro(DatosLibro d, Autor autor){
        this.titulo = d.titulo();
        this.idioma = d.idioma().get(0);
        this.totalDescargas = d.totalDescargas();
        this.autor = autor;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /*public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }*/

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getTotalDescargas() {
        return totalDescargas;
    }

    public void setTotalDescargas(Double totalDescargas) {
        this.totalDescargas = totalDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", idioma='" + idioma + '\'' +
                ", totalDescargas=" + totalDescargas;
    }
}
