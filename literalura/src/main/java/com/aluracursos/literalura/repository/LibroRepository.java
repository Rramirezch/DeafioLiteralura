package com.aluracursos.literalura.repository;


import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l WHERE l.titulo ILIKE %:nombreLibro%")
    List<Libro> libroEnBD(String nombreLibro);


}
