package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record PersonaRecord(
        @JsonAlias("name")
        String nombre,
        @JsonAlias("birth_year")
        Integer birthYear,
        @JsonAlias("death_year")
        Integer deathYear
) {
}
