package com.alura.literalura.conversorAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversorDatos {
    ObjectMapper mapper = new ObjectMapper();

    public <T> T convertirJson(String json, Class<T> claseObjeto){
        try {
            return mapper.readValue(json,claseObjeto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
