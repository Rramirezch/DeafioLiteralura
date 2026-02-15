package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos convierteDatos = new ConvierteDatos();
    private final List<DatosAutor> autores = new ArrayList<>();
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private final LibroRepository repositorio;
    private final AutorRepository repositoryo;

    public Principal(LibroRepository repositorio, AutorRepository repositoryo) {
        this.repositorio = repositorio;
        this.repositoryo = repositoryo;
        List<Libro> libros = new ArrayList<>();
        List<Autor> autors = new ArrayList<>();
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ***************************
                    Seleccione una Opcion
                    1- Bucar Libro por Titulo
                    2- Buscar libro en B.D.
                    3- Mostrar Libros Registrados en B.D.
                    4- Mostrar Autores registrados en B.D.
                    5- Mostrar Libros Por Idioma
                    6- Mostrar Autores Vivos en YYYY año
                    0- Salir
                    """;
            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    buscarLibroEnBD();
                    break;
                case 3:
                    mostrarLibrosRegistrados();
                    break;
                case 4:
                    mostrarAutoresRegistrados();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 6:
                    mostrarAutoresVivosEnYYYY();
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    public void buscarLibroPorTitulo() {
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println("Ingrese el nombre del libro a buscar");
        var tituloLibro = URLEncoder.encode(scanner.nextLine(), StandardCharsets.UTF_8);
        //adecuamos la api
        json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro);
        var datosBusqueda = convierteDatos.obtenerDatos(json, Results.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado");
            System.out.println(libroBuscado.get());
        } else {
            System.out.println("No se encontro el libro");
        }

        datosLibros = datosBusqueda.resultados();
        Optional<Libro> found = Optional.empty();
        for (DatosLibro d : datosLibros) {
            if (!d.autor().isEmpty()) {
                DatosAutor datosAutor = d.autor().get(0);
                Autor autor = new Autor(datosAutor);
                repositoryo.save(autor);
                Libro libro = new Libro(d, autor);
                repositorio.save(libro);
                found = Optional.of(libro);
                System.out.println("Se guardó el libro " + libroBuscado.get().titulo());
                break;
            }
        }
        if (found.isPresent()) {
            System.out.println("Proceso finalizado con éxito para: " + found.get().getTitulo());
        } else {
            System.out.println("No se encontraron libros válidos para guardar.");
        }
    }

    public void buscarLibroEnBD() {
        System.out.println("Escribe el nombre del libro que desea buscar");
        var nombreLibro = scanner.nextLine();
        List<Libro> libroEncontrado = repositorio.libroEnBD(nombreLibro);
        if (libroEncontrado.isEmpty()) {
            System.out.println("Libro no encontrado en la base de datos.");
        } else {
            libroEncontrado.forEach(l ->
                    System.out.printf("""
                                    ------- LIBRO -------
                                    Título: %s
                                    Autor: %s
                                    Idioma: %s
                                    Descargas: %.2f
                                    ---------------------
                                    """,
                            l.getTitulo(),
                            l.getAutor().getNombre(), // Accedemos al nombre a través del objeto Autor
                            l.getIdioma(),
                            l.getTotalDescargas()
                    ));

        }

    }

    public void mostrarLibrosRegistrados() {
        List<Libro> libros = repositorio.findAll();
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(l ->
                        System.out.printf("""
                                        ------- LIBRO -------
                                        Titulo: %s
                                        Autor: %s
                                        Idioma: %s
                                        Total descargas: %s
                                        ---------------------
                                        """,
                                l.getTitulo(),
                                l.getAutor().getNombre(), // Accedemos al nombre a través del objeto Autor
                                l.getIdioma(),
                                l.getTotalDescargas()
                        ));
        }

    public void mostrarAutoresRegistrados(){
        List<Autor>autores = repositoryo.findAll();
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(a ->
                        System.out.printf("""
                                    ------- AUTOR -------
                                    Nombre: %s
                                    Fecha Nacimiento: %s
                                    Fecha Muerte: %s
                                    ---------------------
                                    """,
                                a.getNombre(),
                                a.getFechaNacimiento(),
                                a.getFechaMuerte()
                        ));
    }
    public void mostrarLibrosPorIdioma(){
        System.out.println("indique el idioma que quiere listar");
        System.out.println("Asi:\n en - para Ingles \n es - para Español \n fr - Para Frances \n pt - para portugues ");
        var idioma = scanner.nextLine();
        List<Libro> libros = repositorio.libroPorIdioma(idioma);
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(l ->
                        System.out.printf("""
                                        ------- LIBRO -------
                                        Titulo: %s
                                        Autor: %s
                                        Idioma: %s
                                        Total descargas: %s
                                        ---------------------
                                        """,
                                l.getTitulo(),
                                l.getAutor().getNombre(), // Accedemos al nombre a través del objeto Autor
                                l.getIdioma(),
                                l.getTotalDescargas()
                        ));
    }
    public void mostrarAutoresVivosEnYYYY(){
        System.out.println("Digite el año (yyyy) que desea saber los autores Vivos");
        var year = scanner.nextInt();
        List<Autor> autores = repositoryo.autoresVivos(year);
        autores.stream()
                .forEach(a ->
                        System.out.printf("""
                                    ------- AUTOR -------
                                    Nombre: %s
                                    Fecha Nacimiento: %s
                                    Fecha Muerte: %s
                                    ---------------------
                                    """,
                                a.getNombre(),
                                a.getFechaNacimiento(),
                                a.getFechaMuerte()
                        ));

    }


}