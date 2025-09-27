package com.energia_y_salud_web.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String dni;
    private String nombre;
    private String apellido;
    private List<Rutina> usuarioRutinas = new ArrayList<>();

    public Usuario() {}

    public Usuario(String dni, String nombre, String apellido) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuarioRutinas = new ArrayList<>();
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public List<Rutina> getUsuarioRutinas() {
        if (usuarioRutinas == null) usuarioRutinas = new ArrayList<>();
        return usuarioRutinas;
    }
    public void setUsuarioRutinas(List<Rutina> usuarioRutinas) {
        this.usuarioRutinas = usuarioRutinas;
    }
}
