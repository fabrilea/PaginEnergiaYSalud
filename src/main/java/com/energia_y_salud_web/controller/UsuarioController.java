package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.dto.UsuarioConRutinas;
import com.energia_y_salud_web.model.Historial;
import com.energia_y_salud_web.model.Rutina;
import com.energia_y_salud_web.model.RutinaEjercicio;
import com.energia_y_salud_web.model.Ejercicio;
import com.energia_y_salud_web.service.UsuarioService;
import com.energia_y_salud_web.service.RutinaService;
import com.energia_y_salud_web.service.EjercicioService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RutinaService rutinaService;
    private final EjercicioService ejercicioService;

    public UsuarioController(UsuarioService usuarioService,
                             RutinaService rutinaService,
                             EjercicioService ejercicioService) {
        this.usuarioService = usuarioService;
        this.rutinaService = rutinaService;
        this.ejercicioService = ejercicioService;
    }

    // 👉 Ver rutinas asignadas de un usuario
    @GetMapping("/{dni}")
    public String verRutinasUsuario(@PathVariable String dni, Model model) throws Exception {
        UsuarioConRutinas dto = usuarioService.buscarUsuarioConRutinas(dni).get();

        if (dto == null) {
            model.addAttribute("mensajeError", "⚠ Usuario no encontrado con DNI: " + dni);
            return "error-usuario";
        }

        model.addAttribute("usuario", dto.getUsuario());
        model.addAttribute("rutinas", dto.getRutinas());

        return "usuario"; // ✔ carga usuario.html
    }


    // 👉 Ver detalle de una rutina asignada
// 👉 Ver detalle de una rutina asignada
    @GetMapping("/{dni}/rutina/{id}")
    public String verDetalleRutina(@PathVariable String dni,
                                   @PathVariable String id,
                                   Model model) throws Exception {
        UsuarioConRutinas dto = usuarioService.buscarUsuarioConRutinas(dni).get();
        if (dto == null) {
            model.addAttribute("mensajeError", "⚠ Usuario no encontrado");
            return "error-usuario";
        }

        Rutina rutina = dto.getRutinas().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (rutina == null) {
            model.addAttribute("mensajeError", "⚠ Rutina no encontrada");
            return "error-usuario";
        }

        // ✅ Traer ejercicios con nombres resueltos
        List<RutinaEjercicio> ejercicios = rutina.getEjercicios();

        // ✅ Historial últimos 5 entrenamientos
        List<Historial> historial = rutinaService.obtenerHistorial(dni, id).get();
        for (Historial h : historial) {
            Ejercicio e = ejercicioService.findById(h.getEjercicioId());
            if (e != null) h.setEjercicioNombre(e.getNombre());
        }

        // 👉 Pasar datos al modelo
        model.addAttribute("usuario", dto.getUsuario());
        model.addAttribute("rutina", rutina);
        model.addAttribute("rutinaEjercicios", ejercicios);
        model.addAttribute("historial", historial);

        return "rutina-detalle"; // ✔ carga rutina-detalle.html
    }

    @PostMapping("/{dni}/rutina/{id}/historial")
    public String registrarHistorial(@PathVariable String dni,
                                     @PathVariable String id,
                                     @RequestParam String ejercicioId,
                                     @RequestParam double pesoUsado,
                                     @RequestParam int reps) {

        rutinaService.registrarHistorial(dni, id, ejercicioId, pesoUsado, reps);

        // Redirigir de nuevo al detalle de la rutina
        return "redirect:/usuario/" + dni + "/rutina/" + id;
    }


}
