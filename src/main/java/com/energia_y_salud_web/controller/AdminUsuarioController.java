package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.model.Rutina;
import com.energia_y_salud_web.model.Usuario;
import com.energia_y_salud_web.service.UsuarioService;
import com.energia_y_salud_web.service.RutinaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
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
        List<Rutina> rutinas = rutinaService.findAll().get();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("rutinas", rutinas);
        return "usuarios"; // templates/usuarios.html
    }

    // 👉 Crear usuario con rol y contraseña
    @PostMapping
    public String crearUsuario(@RequestParam String dni,
                               @RequestParam String nombre,
                               @RequestParam String apellido,
                               @RequestParam(required = false, defaultValue = "USER") String rol,
                               @RequestParam(required = false, defaultValue = "1234") String password) {

        Usuario usuario = new Usuario(dni, nombre, apellido);
        usuario.setRol(rol);
        usuario.setPassword(password); // ⚠️ cifrar con BCrypt si querés más seguridad

        usuarioService.save(usuario);
        return "redirect:/admin/usuarios";
    }

    // 👉 Eliminar usuario
    @PostMapping("/{dni}/eliminar")
    public String eliminarUsuario(@PathVariable String dni) {
        usuarioService.deleteByDni(dni);
        return "redirect:/admin/usuarios";
    }

    // 👉 Asignar rutina
    @PostMapping("/{dni}/rutinas")
    public String asignarRutina(@PathVariable String dni,
                                @RequestParam String rutinaId) throws Exception {
        usuarioService.asignarRutina(dni, rutinaId).get(2, TimeUnit.SECONDS);
        return "redirect:/admin/usuarios";
    }

    // 👉 Quitar rutina
    @PostMapping("/{dni}/rutinas/{id}/eliminar")
    public String quitarRutina(@PathVariable String dni, @PathVariable String id) throws Exception {
        usuarioService.quitarRutina(dni, id);
        return "redirect:/admin/usuarios";
    }

    // 🧩 Nuevo: cambiar rol de un usuario (ADMIN puede hacerlo)
    @PostMapping("/{dni}/rol")
    public String cambiarRol(@PathVariable String dni,
                             @RequestParam String nuevoRol) {
        usuarioService.actualizarRol(dni, nuevoRol);
        return "redirect:/admin/usuarios";
    }

    // 🧩 Nuevo: cambiar contraseña de un usuario
    @PostMapping("/{dni}/password")
    public String cambiarPassword(@PathVariable String dni,
                                  @RequestParam String nuevaPassword) {
        usuarioService.actualizarPassword(dni, nuevaPassword);
        return "redirect:/admin/usuarios";
    }
}
