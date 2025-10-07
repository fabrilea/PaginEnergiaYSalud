document.addEventListener("DOMContentLoaded", () => {
    // Confirmar eliminación de usuario
    document.querySelectorAll("form.eliminar-usuario").forEach(form => {
      form.addEventListener("submit", e => {
        const dni = form.getAttribute("data-dni") || "desconocido";
        if (!confirm(`¿Estás seguro de que querés eliminar al usuario con DNI ${dni}?`)) {
          e.preventDefault();
        }
      });
    });
  
    // Confirmar quitar rutina
    document.querySelectorAll("form.quitar-rutina").forEach(form => {
      form.addEventListener("submit", e => {
        if (!confirm("¿Seguro que querés quitar esta rutina del usuario?")) {
          e.preventDefault();
        }
      });
    });
  });
  