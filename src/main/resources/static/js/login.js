document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("loginForm");
    if (!form) {
        console.error("No se encontró el formulario con id=loginForm");
        return;
    }

    form.addEventListener("submit", function(e) {
        e.preventDefault();

        const dni = document.getElementById("dni").value.trim();
        const mensaje = document.getElementById("mensaje");

        if (!dni) {
            mensaje.textContent = "Por favor ingrese un DNI válido.";
            return;
        }

        // Caso especial: admin
        if (dni === "123456789") {
            window.location.href = "/admin";
            return;
        }

        // Usuario normal
        window.location.href = "/usuario/" + dni;
    });
});
