package com.energia_y_salud_web.model;

public class Historial {
    private String id;
    private String usuarioId;
    private String rutinaId;
    private String ejercicioId;
    private String ejercicioNombre; // 👈 nuevo
    private Double pesoUsado;
    private Integer repeticionesRealizadas;
    private String fecha;

    public Historial() {}

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getRutinaId() { return rutinaId; }
    public void setRutinaId(String rutinaId) { this.rutinaId = rutinaId; }

    public String getEjercicioId() { return ejercicioId; }
    public void setEjercicioId(String ejercicioId) { this.ejercicioId = ejercicioId; }

    public String getEjercicioNombre() { return ejercicioNombre; }
    public void setEjercicioNombre(String ejercicioNombre) { this.ejercicioNombre = ejercicioNombre; }

    public Double getPesoUsado() { return pesoUsado; }
    public void setPesoUsado(Double pesoUsado) { this.pesoUsado = pesoUsado; }

    public Integer getRepeticionesRealizadas() { return repeticionesRealizadas; }
    public void setRepeticionesRealizadas(Integer repeticionesRealizadas) { this.repeticionesRealizadas = repeticionesRealizadas; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
