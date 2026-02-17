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

    private static void accept(Autor u) {
        System.out.printf("""
                        ------- AUTOR -------
                        Nombre: %s
                        Fecha Nacimiento: %s
                        Fecha Muerte: %s
                        ---------------------
                        """,
                u.getNombre(),
                u.getFechaNacimiento(),
                u.getFechaMuerte()
        );
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ***************************
                    Seleccione una Opcion
                    1- Bucar Libro por Titulo en la API Gutendex
                    2- Buscar libro en B.D.
                    3- Mostrar Libros Registrados en B.D.
                    4- Mostrar Autores registrados en B.D.
                    5- Mostrar Libros Por Idioma
                    6- Mostrar Autores Vivos en YYYY año
                    7- Ver estadisticas de los libros en B.D.
                    8- ver top 3 de Libros
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
                    break;
                case 7:
                    mostrarEstadisticas();
                    break;
                case 8:
                    top3DemayoresDescargados();
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
        Optional<Libro> libroExistente = repositorio.findByTituloContainsIgnoreCase(tituloLibro);
        if (libroExistente.isPresent()) {
            System.out.println("El libro '" + tituloLibro + "' ya está registrado en la base de datos.");

        } else {
            //adecuamos la api
            json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro);
            var datosBusqueda = convierteDatos.obtenerDatos(json, Results.class);
            Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                    .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                    .findFirst();
            if (libroBuscado.isEmpty()) {
                System.out.println("No se encontro el libro");
            } else {
                System.out.println("Libro encontrado");
                System.out.println(libroBuscado.get());
                datosLibros = datosBusqueda.resultados();
                Optional<Libro> found = Optional.empty();

                for (DatosLibro d : datosLibros) {
                    // 2. Si no existe, verificamos el autor y guardamos
                    if (!d.autor().isEmpty()) {
                        DatosAutor datosAutor = d.autor().get(0);
                        // 1. BUSCAR si el autor ya existe en la base de datos
                        Autor autor = repositoryo.findByNombreIgnoreCase(datosAutor.nombre())
                                .orElseGet(() -> {
                                    // 2. Si NO existe, creamos uno nuevo y lo guardamos
                                    Autor nuevoAutor = new Autor(datosAutor);
                                    return repositoryo.save(nuevoAutor);
                                });
                        Libro libro = new Libro(d, autor);
                        repositorio.save(libro);

                        found = Optional.of(libro);
                        System.out.println("Se guardó el libro: " + libro.getTitulo());
                        break; // Detenemos el bucle tras guardar el libro con éxito
                    } else {
                        System.out.println("El libro '" + d.titulo() + "' no tiene autor y no se puede registrar.");
                    }
                }
            }
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

    public void mostrarAutoresRegistrados() {
        List<Autor> autores = repositoryo.findAllAutoresWithLibros();

        Set<String> nombresUnicos = new HashSet<>();
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .filter(a -> nombresUnicos.add(a.getNombre()))
                .forEach(a -> {
                    String titulosLibros = a.getLibros().stream()
                            .map(Libro::getTitulo)
                            .collect(Collectors.joining(", "));
                    System.out.printf("""
                                    ------- AUTOR -------
                                    Nombre: %s
                                    Fecha Nacimiento: %s
                                    Fecha Muerte: %s
                                    Libros escritos: [%s]
                                    ---------------------
                                    """,
                            a.getNombre(),
                            a.getFechaNacimiento(),
                            a.getFechaMuerte(),
                            titulosLibros
                    );
    });

    }

    public void mostrarLibrosPorIdioma() {
        System.out.println("indique el idioma que quiere listar");
        System.out.println("Asi:\n en - para Ingles \n es - para Español \n fr - Para Frances \n pt - para portugues ");
        var idioma = scanner.nextLine();
        List<Libro> libros = repositorio.libroPorIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en- " + idioma +" -en la base de datos.");
        } else {

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
    }

    public void mostrarAutoresVivosEnYYYY() {
        System.out.println("Digite el año (yyyy) para mostrar los autores vivos");
        var year = scanner.nextInt();
        List<Autor> autores = repositoryo.autoresVivos(year);
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores registrados vivos en el año."+ year);
        } else {
            Set<String> nombresImpresos = new HashSet<>();
            autores.stream()
            .filter(a -> nombresImpresos.add(a.getNombre()))
            /*autores*/.forEach(a -> {
                String libros = a.getLibros().stream()
                        .map(Libro::getTitulo)
                        .collect(Collectors.joining(", "));

                System.out.printf("""
                        Autor: %s
                        Fecha de Nacimiento: %s
                        Fecha de Fallecimiento: %s
                        Libros: [%s]
                        ----------------------------------
                        """, a.getNombre(), a.getFechaNacimiento(), a.getFechaMuerte(), libros);
            });
        }
    }

    public void mostrarEstadisticas() {
        List<Libro> libros = repositorio.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay datos suficientes para generar estadísticas.");
            return;
        }

        DoubleSummaryStatistics est = libros.stream()
                .filter(l -> l.getTotalDescargas() > 0)
                .collect(Collectors.summarizingDouble(Libro::getTotalDescargas));

        System.out.println("""
            --------- ESTADÍSTICAS ---------
            Cantidad de libros: %d
            Promedio de descargas: %.2f
            Máximo de descargas: %.2f
            Mínimo de descargas: %.2f
            --------------------------------
            """.formatted(est.getCount(), est.getAverage(), est.getMax(), est.getMin()));
    }
    public void top3DemayoresDescargados(){
        List<Libro> libros = repositorio.findTop3ByOrderByTotalDescargasDesc();

        if (libros.isEmpty()) {
            System.out.println("No hay datos suficientes para generar estadísticas.");
            return;
        }
        libros.stream()
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

}