package com.energia_y_salud_web.model;

import java.util.ArrayList;
import java.util.List;

public class Ejercicio {
    private String id;
    private String nombre;
    private String descripcion;
    private List<String> rutinaEjercicioIds = new ArrayList<>();

    public Ejercicio() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<String> getRutinaEjercicioIds() { return rutinaEjercicioIds; }
    public void setRutinaEjercicioIds(List<String> rutinaEjercicioIds) {
        this.rutinaEjercicioIds = rutinaEjercicioIds;
    }
}
