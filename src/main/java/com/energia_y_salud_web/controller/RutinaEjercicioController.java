package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.model.RutinaEjercicio;
import com.energia_y_salud_web.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rutina-ejercicios")
public class RutinaEjercicioController {

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping
    public List<RutinaEjercicio> getAll() throws ExecutionException, InterruptedException {
        return firebaseService.findAll("rutinaEjercicios", RutinaEjercicio.class).get();
    }

    @GetMapping("/{id}")
    public RutinaEjercicio getById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return firebaseService.findById("rutinaEjercicios", id, RutinaEjercicio.class).get();
    }

    @PostMapping
    public void createOrUpdate(@RequestBody RutinaEjercicio rutinaEjercicio) {
        firebaseService.save("rutinaEjercicios", rutinaEjercicio.getId(), rutinaEjercicio);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        firebaseService.delete("rutinaEjercicios", id);
    }
}
