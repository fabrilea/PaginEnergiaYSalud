package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.model.Ejercicio;
import com.energia_y_salud_web.model.Rutina;
import com.energia_y_salud_web.service.EjercicioService;
import com.energia_y_salud_web.service.RutinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/admin/ejercicios")
@PreAuthorize("hasRole('ADMIN')")
public class EjercicioController {

    private final EjercicioService ejercicioService;
    private final RutinaService rutinaService;

    public EjercicioController(EjercicioService ejercicioService, RutinaService rutinaService) {
        this.ejercicioService = ejercicioService;
        this.rutinaService = rutinaService;
    }

    // 👉 LISTAR EJERCICIOS
    @GetMapping
    public String listarEjercicios(Model model) throws Exception {
        List<Ejercicio> ejercicios = ejercicioService.findAll(); // ✅ lista de ejercicios
        model.addAttribute("ejercicios", ejercicios);
        return "ejercicios"; // ⚠️ crea ejercicios.html
    }


    // 👉 CREAR
    @PostMapping
    public String crearEjercicio(@RequestParam String nombre,
                                 @RequestParam String descripcion) {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombre(nombre);
        ejercicio.setDescripcion(descripcion);
        ejercicioService.save(ejercicio);
        return "redirect:/admin/ejercicios";
    }

    // 👉 DETALLE
    @GetMapping("/{id}")
    public String verEjercicio(@PathVariable String id, Model model) throws ExecutionException, InterruptedException {
        Ejercicio ejercicio = ejercicioService.findById(id);
        if (ejercicio == null) {
            model.addAttribute("error", "Ejercicio no encontrado con ID: " + id);
            return "ejercicios";
        }
        model.addAttribute("ejercicio", ejercicio);
        return "ejercicio"; // ⚠️ deberías crear ejercicio.html (detalle)
    }

    // 👉 ELIMINAR
    @PostMapping("/{id}/eliminar")
    public String eliminarEjercicio(@PathVariable String id) {
        ejercicioService.delete(id);
        return "redirect:/admin/ejercicios";
    }
}
