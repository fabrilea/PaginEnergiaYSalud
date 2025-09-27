document.addEventListener("DOMContentLoaded", () => {
    // 👤 Confirmar eliminar usuario
    document.querySelectorAll("form.eliminar-usuario").forEach(form => {
        form.addEventListener("submit", e => {
            e.preventDefault();
            const dni = form.getAttribute("data-dni") || "desconocido";
            if (confirm(`¿Estás seguro de que querés eliminar al usuario con DNI ${dni}?`)) {
                form.submit();
            }
        });
    });

    // 🗑️ Confirmar quitar rutina
    document.querySelectorAll("form.quitar-rutina").forEach(form => {
        form.addEventListener("submit", e => {
            e.preventDefault();
            if (confirm("¿Seguro que quieres quitar esta rutina del usuario?")) {
                form.submit();
            }
        });
    });
});
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".eliminar-usuario, .quitar-rutina").forEach(form => {
        form.addEventListener("submit", e => {
            if (!confirm("¿Seguro que deseas continuar?")) {
                e.preventDefault();
            }
        });
    });
});