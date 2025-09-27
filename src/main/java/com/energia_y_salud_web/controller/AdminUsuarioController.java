package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.model.Rutina;
import com.energia_y_salud_web.model.Usuario;
import com.energia_y_salud_web.service.UsuarioService;
import com.energia_y_salud_web.service.RutinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;
    private final RutinaService rutinaService;

    public AdminUsuarioController(UsuarioService usuarioService, RutinaService rutinaService) {
        this.usuarioService = usuarioService;
        this.rutinaService = rutinaService;
    }

    // 👉 Lista de usuarios
    @GetMapping
    public String listarUsuarios(Model model) throws Exception {
        List<Usuario> usuarios = usuarioService.findAll().get();
        List<Rutina> rutinas = rutinaService.findAll().get(); // ✅ resolvemos el Future

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("rutinas", rutinas);

        return "usuarios"; // templates/usuarios.html
    }


    // 👉 Crear usuario
    @PostMapping
    public String crearUsuario(@RequestParam String dni,
                               @RequestParam String nombre,
                               @RequestParam String apellido) {
        Usuario usuario = new Usuario(dni, nombre, apellido);
        usuarioService.save(usuario);
        return "redirect:/admin/usuarios";
    }

    // 👉 Eliminar usuario
    @PostMapping("/{dni}/eliminar")
    public String eliminarUsuario(@PathVariable String dni) {
        usuarioService.deleteByDni(dni);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{dni}/rutinas")
    public String asignarRutina(@PathVariable String dni,
                                @RequestParam String rutinaId) throws Exception {
        usuarioService.asignarRutina(dni, rutinaId).get(2, TimeUnit.SECONDS);
        return "redirect:/admin/usuarios";
    }




    // 👉 Quitar rutina de usuario
    @PostMapping("/{dni}/rutinas/{id}/eliminar")
    public String quitarRutina(@PathVariable String dni, @PathVariable String id) throws Exception {
        usuarioService.quitarRutina(dni, id);
        return "redirect:/admin/usuarios";
    }

}
