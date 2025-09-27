package com.energia_y_salud_web.dto;

import com.energia_y_salud_web.model.Rutina;
import com.energia_y_salud_web.model.Usuario;

import java.util.List;

public class UsuarioConRutinas {

    private Usuario usuario;
    private List<Rutina> rutinas;

    public UsuarioConRutinas() {}

    public UsuarioConRutinas(Usuario usuario, List<Rutina> rutinas) {
        this.usuario = usuario;
        this.rutinas = rutinas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Rutina> getRutinas() {
        return rutinas;
    }

    public void setRutinas(List<Rutina> rutinas) {
        this.rutinas = rutinas;
    }
}
