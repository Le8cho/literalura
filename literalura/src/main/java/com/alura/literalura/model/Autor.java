package com.alura.literalura.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String nombre;

    private int birthYear;

    private int deathYear;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Libro> libros = new ArrayList<>();


   public Autor(){

   }

   public Autor(PersonaRecord personaRecord) {
        this.nombre = personaRecord.nombre();
        this.birthYear = personaRecord.birthYear();
        this.deathYear = personaRecord.deathYear();
   }

    public Autor(Libro libro) {
       this.nombre = libro.getAutor().getNombre();
       this.birthYear= libro.getAutor().getBirthYear();
       this.deathYear = libro.getAutor().getDeathYear();
    }

    @Override
    public String toString() {
        return "\n-----------Autor-----------" +
                "\nNombre:      " + '\'' + this.getNombre() + '\'' +
                "\nAño Nacimiento:       " + '\'' + this.getBirthYear() + '\'' +
                "\nAño Fallecimiento:      " + '\'' + this.getDeathYear() + '\'' +
                "\n---------------------------";
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(int deathYear) {
        this.deathYear = deathYear;
    }
}
