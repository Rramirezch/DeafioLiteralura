package com.aluracursos.literalura.repository;


import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l WHERE l.titulo ILIKE %:nombreLibro%")
    List<Libro> libroEnBD(String nombreLibro);

   @Query("SELECT l FROM Libro l WHERE l.idioma ILIKE %:idioma%")
    List<Libro> libroPorIdioma(String idioma);


   Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findTop3ByOrderByTotalDescargasDesc();



}
