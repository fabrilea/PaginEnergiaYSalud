package com.energia_y_salud_web.model;

import java.util.ArrayList;
import java.util.List;

public class Rutina {
    private String id;
    private String nombre;
    private String descripcion;

    // ✅ ahora es una lista en vez de un Map
    private List<RutinaEjercicio> ejercicios = new ArrayList<>();

    private List<Historial> historial = new ArrayList<>();

    public Rutina() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<RutinaEjercicio> getEjercicios() { return ejercicios; }
    public void setEjercicios(List<RutinaEjercicio> ejercicios) {
        this.ejercicios = ejercicios != null ? ejercicios : new ArrayList<>();
    }

    public List<Historial> getHistorial() { return historial; }
    public void setHistorial(List<Historial> historial) {
        this.historial = historial != null ? historial : new ArrayList<>();
    }
}
