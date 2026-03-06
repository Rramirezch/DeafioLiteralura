## 🎓 Desafío Alura - Literalura
Este proyecto forma parte del programa **Oracle Next Education**, diseñado para aplicar conocimientos de Java, Spring Boot y bases de datos.


📚 Literalura - Catálogo de Libros
Literalura es una aplicación de consola desarrollada en Java que utiliza la API de Gutendex para buscar libros, procesar datos en formato JSON y 
almacenarlos en una base de datos relacional postgres. El proyecto permite gestionar libros y autores, permitiendo consultas específicas sobre 
autores vivos en determinados años e idiomas.
🚀 Características

    Búsqueda de libros por título: Consulta datos directamente desde una API externa.
    Persistencia de datos: Almacena libros y autores en una base de datos PostgreSQL.
    Relaciones JPA: Implementa una relación @ManyToOne entre Libros y Autores, evitando duplicidad de registros de autores.
    Consultas personalizadas:
        Listar todos los libros registrados.
	Listar un libro específicamente de la base de datos.
        Listar autores registrados con sus respectivos libros.
        Filtrar autores vivos en un año específico.
        Listar libros por idioma.

🛠️ Tecnologías utilizada
	
    Java 25.
    Maven (Gestión de dependencias).
    Spring Boot 4.0.2
        Spring Data JPA.
    PostgreSQL (Base de datos).
    Jackson (Para el parseo de JSON).
    Gutendex API (Fuente de datos).
    Intellij IDEA

📋 Requisitos previos
Antes de ejecutar el proyecto, asegúrate de tener instalado:

    JDK 25.
    PostgreSQL en ejecución.
    Intellij IDEA

⚙️ Configuración

    Clona el repositorio:
    https://github.com/Rramirezch/DeafioLiteralura

Configura las credenciales de tu base de datos en el archivo src/main/resources/application.properties:
	spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
	spring.datasource.username=tu_usuario
	spring.datasource.password=tu_contraseña
	spring.jpa.hibernate.ddl-auto=update

🖥️ Uso
Al iniciar la aplicación, se desplegará un menú interactivo en la consola con las siguientes opciones:

    Buscar libro por título: Ingresa el nombre del libro para traerlo desde la API y guardarlo.
    Listar un libro específico desde la BD.
    Listar libros registrados: Muestra todos los libros guardados en la BD.
    Listar autores registrados: Muestra los autores y los títulos de sus libros.
    Listar autores vivos en un determinado año: Filtra autores por cronología.
    Listar libros por idioma: Filtra libros por código de idioma (es, en, fr, .).

## 📊 Estadísticas de Uso
El sistema incluye una funcionalidad de análisis de datos que utiliza `DoubleSummaryStatistics` para procesar la información de los libros almacenados. 

Esta función permite obtener de forma instantánea:
*   **Media de descargas:** Calcula el impacto promedio de tu biblioteca digital.
*   **Top de popularidad:** Identifica el libro con el mayor número de descargas registradas, también los 3 mas              	descargados
*   **Análisis de volumen:** Cantidad total de registros procesados.
