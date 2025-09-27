package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.model.*;
import com.energia_y_salud_web.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/admin/rutinas")
public class RutinaController {

    private final RutinaService rutinaService;
    private final UsuarioService usuarioService;
    private final EjercicioService ejercicioService;

    public RutinaController(RutinaService rutinaService,
                            UsuarioService usuarioService,
                            EjercicioService ejercicioService) {
        this.rutinaService = rutinaService;
        this.usuarioService = usuarioService;
        this.ejercicioService = ejercicioService;
    }

    // 👉 LISTAR
    @GetMapping
    public String listarRutinas(Model model) {
        try {
            List<Rutina> rutinas = rutinaService.findAll().get(3, TimeUnit.SECONDS);
            List<Ejercicio> ejercicios = ejercicioService.findAll();
            model.addAttribute("rutinas", rutinas);
            model.addAttribute("ejercicios", ejercicios);
        } catch (Exception e) {
            model.addAttribute("rutinas", List.of());
            model.addAttribute("ejercicios", List.of());
            model.addAttribute("error", "No se pudieron cargar las rutinas: " + e.getMessage());
        }
        return "rutinas";
    }

    // 👉 CREAR
    @PostMapping
    public String crearRutina(@RequestParam String nombre,
                              @RequestParam(required = false) String descripcion,
                              @RequestParam List<String> ejercicioId,
                              @RequestParam(required = false) List<String> repeticiones,
                              @RequestParam(required = false) List<String> peso,
                              @RequestParam(required = false) List<String> vueltas) throws Exception {
        Rutina rutina = new Rutina();
        rutina.setNombre(nombre);
        rutina.setDescripcion(descripcion);

        List<RutinaEjercicio> ejercicios = new ArrayList<>();
        for (int i = 0; i < ejercicioId.size(); i++) {
            String idEj = ejercicioId.get(i);
            if (idEj != null && !idEj.isBlank()) {
                Ejercicio ejercicio = ejercicioService.findById(idEj);
                if (ejercicio != null) {
                    RutinaEjercicio re = new RutinaEjercicio();
                    re.setEjercicioId(idEj);
                    re.setEjercicioNombre(ejercicio.getNombre());
                    re.setEjercicioDescripcion(ejercicio.getDescripcion());
                    re.setRepeticiones(i < repeticiones.size() ? parseIntOrNull(repeticiones.get(i)) : null);
                    re.setPesoSugerido(i < peso.size() ? parseIntOrNull(peso.get(i)) : null);
                    re.setVueltas(i < vueltas.size() ? parseIntOrNull(vueltas.get(i)) : null);
                    ejercicios.add(re);
                }
            }
        }
        rutina.setEjercicios(ejercicios);
        rutinaService.save(rutina);

        return "redirect:/admin/rutinas";
    }

    // 👉 DETALLE
    @GetMapping("/{id}")
    public String verRutina(@PathVariable String id,
                            @RequestParam(required = false) String dni,
                            Model model) throws ExecutionException, InterruptedException {
        Rutina rutina = rutinaService.findById(id).get();
        if (rutina == null) {
            model.addAttribute("error", "Rutina no encontrada con ID: " + id);
            return "rutinas";
        }

        Usuario usuario = null;
        List<?> historial = List.of();
        if (dni != null) {
            usuario = usuarioService.buscarPorDni(dni).get();
            if (usuario != null) {
                historial = rutinaService.obtenerHistorial(dni, id).get();
            }
        }

        model.addAttribute("rutina", rutina);
        model.addAttribute("rutinaEjercicios",
                rutina.getEjercicios() != null ? rutina.getEjercicios() : List.of());
        model.addAttribute("usuario", usuario);
        model.addAttribute("historial", historial);

        return "rutina-detalle";
    }

    // 👉 REGISTRAR HISTORIAL
    @PostMapping("/{id}/historial")
    public String registrarHistorial(@PathVariable String id,
                                     @RequestParam String ejercicioId,
                                     @RequestParam Double pesoUsado,
                                     @RequestParam Integer repeticionesRealizadas,
                                     @RequestParam String dni) {
        rutinaService.registrarHistorial(dni, id, ejercicioId, pesoUsado, repeticionesRealizadas);
        return "redirect:/admin/rutinas/" + id + "?dni=" + dni;
    }

    // 👉 ELIMINAR
    @PostMapping("/{id}/eliminar")
    public String eliminarRutina(@PathVariable String id) {
        rutinaService.delete(id);
        return "redirect:/admin/rutinas";
    }

    // 👉 FORMULARIO EDICIÓN
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable String id, Model model) throws Exception {
        Rutina rutina = rutinaService.findById(id).get();
        if (rutina == null) {
            return "redirect:/admin/rutinas?error=Rutina no encontrada";
        }
        List<Ejercicio> ejercicios = ejercicioService.findAll();
        model.addAttribute("rutina", rutina);
        model.addAttribute("ejercicios", ejercicios);
        model.addAttribute("rutinaEjercicios",
                rutina.getEjercicios() != null ? rutina.getEjercicios() : new ArrayList<>());
        return "rutina-editar";
    }

    @PostMapping("/{id}/editar")
    public String editarRutina(@PathVariable String id,
                               @RequestParam String nombre,
                               @RequestParam(required = false) String descripcion,
                               @RequestParam List<String> ejercicioId,
                               @RequestParam(required = false) List<String> repeticiones,
                               @RequestParam(required = false) List<String> peso,
                               @RequestParam(required = false) List<String> vueltas) throws Exception {
        Rutina rutina = rutinaService.findById(id).get();
        if (rutina == null) {
            return "redirect:/admin/rutinas?error=Rutina no encontrada";
        }

        rutina.setNombre(nombre);
        rutina.setDescripcion(descripcion);

        List<RutinaEjercicio> ejercicios = new ArrayList<>();
        for (int i = 0; i < ejercicioId.size(); i++) {
            String idEj = ejercicioId.get(i);
            if (idEj != null && !idEj.isBlank()) {
                Ejercicio ejercicio = ejercicioService.findById(idEj);
                if (ejercicio != null) {
                    RutinaEjercicio re = new RutinaEjercicio();
                    re.setEjercicioId(idEj);
                    re.setEjercicioNombre(ejercicio.getNombre());
                    re.setEjercicioDescripcion(ejercicio.getDescripcion());
                    re.setRepeticiones(i < repeticiones.size() ? parseIntOrNull(repeticiones.get(i)) : null);
                    re.setPesoSugerido(i < peso.size() ? parseIntOrNull(peso.get(i)) : null);
                    re.setVueltas(i < vueltas.size() ? parseIntOrNull(vueltas.get(i)) : null);
                    re.setRutinaId(rutina.getId());
                    ejercicios.add(re);
                }
            }
        }
        rutina.setEjercicios(ejercicios);
        rutinaService.save(rutina);

        return "redirect:/admin/rutinas/" + id;
    }

    // 👉 Helpers
    private Integer parseIntOrNull(String s) {
        try {
            return (s != null && !s.isBlank()) ? Integer.parseInt(s) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private Double parseDoubleOrNull(String s) {
        try {
            return (s != null && !s.isBlank()) ? Double.parseDouble(s) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
