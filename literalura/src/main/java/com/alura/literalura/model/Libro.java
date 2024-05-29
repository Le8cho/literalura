package com.alura.literalura.model;

import com.alura.literalura.repository.AutorRepository;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne
    private Autor autor;

    private String lenguaje;

    private int numeroDescargas;

    public Libro(LibroRecord libroRecord) {
        this.titulo = libroRecord.titulo();
        try{
            this.autor = new Autor(libroRecord.autores().get(0));
        }catch(IndexOutOfBoundsException i){
            this.autor = new Autor(new PersonaRecord("Desconocido",0,0));
        }
        this.lenguaje = libroRecord.lenguajes().get(0);
        this.numeroDescargas = libroRecord.numeroDescargas();
    }

    public Libro() {

    }

    @Override
    public String toString() {
        return "\n-----------Libro-----------" +
                "\nTitulo:      " + '\'' + titulo + '\'' +
                "\nAutor:       " + '\'' + autor.getNombre() + '\'' +
                "\nIdioma:      " + '\'' + lenguaje + '\'' +
                "\nDescargas:   " + numeroDescargas +
                "\n---------------------------";
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

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}
