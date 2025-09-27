package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.model.Historial;
import com.energia_y_salud_web.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/historial")
public class HistorialController {

    private final FirebaseService firebaseService;

    public HistorialController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/registrar")
    public String registrarHistorial(
            @RequestParam String usuarioId,
            @RequestParam String rutinaId,
            @RequestParam String ejercicioId,
            @RequestParam Double pesoUsado,
            @RequestParam Integer repeticionesRealizadas
    ) {
        // Generar un ID si no viene
        String id = java.util.UUID.randomUUID().toString();

        Historial historial = new Historial();
        historial.setId(id);
        historial.setUsuarioId(usuarioId);
        historial.setRutinaId(rutinaId);
        historial.setEjercicioId(ejercicioId);
        historial.setPesoUsado(pesoUsado);
        historial.setRepeticionesRealizadas(repeticionesRealizadas);
        historial.setFecha(java.time.LocalDate.now().toString());

        firebaseService.save("historial", id, historial);

        // Redirige al detalle de la rutina
        return "redirect:/admin/rutinas/" + rutinaId;
    }
}

