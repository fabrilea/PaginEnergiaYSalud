package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.model.UsuarioRutina;
import com.energia_y_salud_web.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/usuario-rutinas")
public class UsuarioRutinaController {

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping
    public List<UsuarioRutina> getAll() throws ExecutionException, InterruptedException {
        return firebaseService.findAll("usuarioRutinas", UsuarioRutina.class).get();
    }

    @GetMapping("/{id}")
    public UsuarioRutina getById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return firebaseService.findById("usuarioRutinas", id, UsuarioRutina.class).get();
    }

    @PostMapping
    public void createOrUpdate(@RequestBody UsuarioRutina usuarioRutina) {
        firebaseService.save("usuarioRutinas", usuarioRutina.getId(), usuarioRutina);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        firebaseService.delete("usuarioRutinas", id);
    }
}
