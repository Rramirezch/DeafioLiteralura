package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.DatosAutor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.model.Results;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);

    private String menu = """
            ***************************
            Seleccione una Opcion
            1- Bucar Libro por Titulo
            """;
    private Libro libro;


    public void mostrarMenu(){
        System.out.println(menu);
        var opcion = Integer.parseInt(scanner.nextLine());

        switch (opcion){
            case 1:
                buscarLibroPorTitulo();
                break;
            default:
                System.out.println("Opcion no valida");
                break;
        }

    }
    public void buscarLibroPorTitulo(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println("Ingrese el nombre del libro a buscar");
        var tituloLibro = URLEncoder.encode(scanner.nextLine(), StandardCharsets.UTF_8);
        //adecuamos la api
        json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro);
        var datosBusqueda = convierteDatos.obtenerDatos(json, Results.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado");
            System.out.println(libroBuscado.get());
        }else{
            System.out.println("No se encontro el libro");
        }
        List <DatosAutor> autors = libroBuscado.get().autor();
        System.out.println(autors);
        var resultados = datosBusqueda.resultados();
        //System.out.println("Resultados ="+ resultados);
        List<DatosLibro> datosLibros = datosBusqueda.resultados();
        List <Libro> libros = datosBusqueda.resultados().stream()
                .map(d ->new Libro(d))
                .collect(Collectors.toList());
    }

}

