package com.energia_y_salud_web.model;

public class RutinaEjercicio {
    private String id;
    private String rutinaId;
    private String ejercicioId;
    private String ejercicioNombre;       // 👈 Nombre visible
    private String ejercicioDescripcion;  // 👈 URL del video
    private Integer repeticiones;
    private Integer pesoSugerido;
    private Integer vueltas;

    public RutinaEjercicio() {}

    // 👉 Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRutinaId() { return rutinaId; }
    public void setRutinaId(String rutinaId) { this.rutinaId = rutinaId; }

    public String getEjercicioId() { return ejercicioId; }
    public void setEjercicioId(String ejercicioId) { this.ejercicioId = ejercicioId; }

    public String getEjercicioNombre() { return ejercicioNombre; }
    public void setEjercicioNombre(String ejercicioNombre) { this.ejercicioNombre = ejercicioNombre; }

    public String getEjercicioDescripcion() { return ejercicioDescripcion; }
    public void setEjercicioDescripcion(String ejercicioDescripcion) { this.ejercicioDescripcion = ejercicioDescripcion; }

    public Integer getRepeticiones() { return repeticiones; }
    public void setRepeticiones(Integer repeticiones) { this.repeticiones = repeticiones; }

    public Integer getPesoSugerido() { return pesoSugerido; }
    public void setPesoSugerido(Integer pesoSugerido) { this.pesoSugerido = pesoSugerido; }

    public Integer getVueltas() { return vueltas; }
    public void setVueltas(Integer vueltas) { this.vueltas = vueltas; }
}
