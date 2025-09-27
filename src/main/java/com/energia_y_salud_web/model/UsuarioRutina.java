package com.energia_y_salud_web.model;

public class UsuarioRutina {
    private String id;
    private String usuarioId;
    private String rutinaId;

    public UsuarioRutina() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getRutinaId() { return rutinaId; }
    public void setRutinaId(String rutinaId) { this.rutinaId = rutinaId; }
}
