package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);
    private String json;

    private String menu = """
            ***************************
            Seleccione una Opcion
            1- Bucar Libro por Titulo
            """;

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

    private void buscarLibroPorTitulo() {
        System.out.println("Escriba el Titulo del Libro");
        String titulo = URLEncoder.encode(scanner.nextLine(), StandardCharsets.UTF_8);
        json = consumoAPI.obtenerDatos(URL_BASE+"/?search="+ titulo);
        System.out.println(json);
    }

}

