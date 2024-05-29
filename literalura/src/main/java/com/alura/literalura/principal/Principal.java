package com.alura.literalura.principal;

import com.alura.literalura.conversorAPI.ConversorDatos;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.model.LibroRecord;
import com.alura.literalura.model.ResultadoAPI;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.solicitud.ManejadorHTTP;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final String URL = "https://gutendex.com/books/?search=";
    private ManejadorHTTP manejadorHTTP = new ManejadorHTTP();
    private int opcion = 0;
    private Scanner input = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu() {

        do{
            menuOpciones();
            opcion = input.nextInt();
            input.skip("\n");
            evaluandoOpcion(opcion);
        } while(opcion != 0);

    }

    public void menuOpciones(){
        System.out.println("""
                Elija la opción a través de su número:
                1.- buscar libro por título
                2.- listar libros registrados
                3.- listar autores registrados
                4.- listar autores vivos en un determinado año
                5.- listar libros por idioma
                0.- salir
                """);
    }

    public void evaluandoOpcion(int opcion){

        switch(opcion){
            case 1 ->{
                String titulo = null;
                System.out.println("Ingrese el titulo de la obra");
                titulo = input.nextLine();
                buscarLibroTitulo(titulo);
            }
            case 2->{
                listarLibros();
            }
            case 3->{
                listarAutores();
            }
            case 4->{
                int year = 0;
                System.out.println("Ingrese el año requerido");
                year = input.nextInt();
                if(year < 0 ){
                    System.out.println("Por favor ingrese un año valido de consulta");
                    return;
                }
                listarAutoresVivos(year);
            }
            case 5 ->{
                String idioma = null;
                System.out.println("Ingrese el idioma de la obra");
                System.out.println("""
                        es - español
                        en - inglés
                        fr - francés
                        pt - portugués
                        """);
                idioma = input.nextLine();
                switch (idioma){
                    case "es" :
                    case "en" :
                    case "fr" :
                    case "pt" :
                        listarLibrosIdioma(idioma);
                        break;
                    default:
                        System.out.println("Error, por favor ingrese un idioma del menu indicado");
                }

            }
            case 0 ->{
                System.out.println("Gracias por usar la aplicacion");
            }
            default ->{
                System.out.println("Opcion Incorrecta, intentelo nuevamente");
            }
        }

    }


    private void buscarLibroTitulo(String titulo) {

        LibroRecord libroRecord = obtenerLibroApi(titulo);

        if(libroRecord == null){
            System.out.println("Libro: \'" + titulo + "\' no encontrado, intente nuevamente");
            return;
        }
        //Si el libro ya existe en la base de datos?
        //Falta manejar eso
        if(existeLibro(libroRecord)){
            System.out.println("El libro" + libroRecord.titulo() +" ya se encuentra registrado intente nuevamente");
            return;
        }

        //Crear libro, pero despues verificar si el autor esta en el repositorio
        var libro = new Libro(libroRecord);
        verificarAutorRepositorio(libro);

    }

    private void listarLibros() {
        System.out.println("Libros registrados");
        libros = libroRepository.findAll();
        if(libros.isEmpty()){
            System.out.println("Sin libros registrados por el momento");
            return;
        }
        libros.forEach(System.out::println);
    }

    private void listarAutores() {
        System.out.println("Autores registrados");
        autores = autorRepository.findAll();
        if(autores.isEmpty()){
            System.out.println("Sin autores registrados por el momento");
            return;
        }
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivos(int year) {

        List<Autor> autoresVivos = autorRepository.findByDeathYearAfterAndBirthYearBefore(year, year);

        if(autoresVivos.isEmpty()){
            System.out.println("No existen autores vivos en el año " + year +  " registrados aqui por el momento");
            return;
        }
        System.out.println("Autores vivos en el año: " + year);
        autoresVivos.forEach(System.out::println);
    }

    private void listarLibrosIdioma(String idioma) {
        List<Libro> librosIdioma = libroRepository.findByLenguaje(idioma);
        if(librosIdioma.isEmpty()){
            System.out.println("Sin libros en el idioma especificado: " + idioma);
            return;
        }
        System.out.println("Libros en el idioma: " + idioma);
        librosIdioma.forEach(System.out::println);
    }

    private LibroRecord obtenerLibroApi(String titulo){
        //Obtenemos el json
        var json = manejadorHTTP.getJson(URL+ URLEncoder.encode(titulo,StandardCharsets.UTF_8));

        //El resultado es una lista de libros
        var resultadoApi = new ConversorDatos().convertirJson(json, ResultadoAPI.class);

        //Retornamos el primer libro devuelto por la API. Si no hay resultados devolver null
        return resultadoApi.librosRecord().isEmpty() ? null : resultadoApi.librosRecord().get(0);
    }

    private void verificarAutorRepositorio(Libro libro) {
        //Buscamos el autor
        autores = autorRepository.findAll();

        //Buscamos si el autor del libro que queremos agregar ya existe ...
        Optional<Autor> autorOptional = autores.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(libro.getAutor().getNombre())).findFirst();

        //Si encontramos al autor del nuevo libro en la base de datos
        if (autorOptional.isPresent()) {
            var autorEncontrado = autorOptional.get();
            //Añadimos el libro a su repertorio de libros
            autorEncontrado.getLibros().add(libro);
            libro.setAutor(autorEncontrado);

            autorRepository.save(autorEncontrado); //Actualizamos el autor y los libros vinculados a él
        } else {
            //En caso no encontremos al autor de ese libro
            libro.getAutor().getLibros().add(libro); //Añadimos el libro al repertorio del autor nuevo
            autorRepository.save(libro.getAutor()); //guardamos el registro en la tabla
        }
    }

    private boolean existeLibro(LibroRecord libroRecord){

        System.out.println("Libro a buscar \'" + libroRecord.titulo() + '\'');

        List<Libro> libroBusqueda = libroRepository.findByTitulo(libroRecord.titulo());

        return !libroBusqueda.isEmpty();
    }
}
