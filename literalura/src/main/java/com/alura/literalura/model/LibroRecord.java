package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroRecord(
        @JsonAlias("title")
        String titulo,
        @JsonAlias("authors")
        List<PersonaRecord> autores,
        @JsonAlias("languages")
        List<String> lenguajes,
        @JsonAlias("download_count")
        Integer numeroDescargas
) {
}
