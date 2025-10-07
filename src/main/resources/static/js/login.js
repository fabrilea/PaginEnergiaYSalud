document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("loginForm");
    const mensaje = document.getElementById("mensaje");
  
    if (!form) {
      console.error("No se encontró el formulario con id=loginForm");
      return;
    }
  
    // 🧹 Limpia mensaje si el usuario vuelve a escribir
    ["dni", "password"].forEach(id => {
      const input = document.getElementById(id);
      input.addEventListener("input", () => mensaje.textContent = "");
    });
  
    form.addEventListener("submit", async function(e) {
      e.preventDefault();
      mensaje.textContent = "";
  
      const dni = document.getElementById("dni").value.trim();
      const password = document.getElementById("password").value.trim();
  
      if (!dni || !password) {
        mensaje.textContent = "Por favor, complete todos los campos.";
        return;
      }
  
      try {
        const response = await fetch("/auth/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username: dni, password })
        });
  
        if (!response.ok) {
          mensaje.textContent = "Credenciales inválidas.";
          return;
        }
  
        const data = await response.json();
        localStorage.setItem("token", data.token);
  
        const payload = JSON.parse(atob(data.token.split(".")[1]));
        const rol = payload.rol;
  
        if (rol === "ADMIN") {
          window.location.href = "/admin";
        } else {
          window.location.href = `/usuario/${dni}`;
        }
      } catch (err) {
        console.error("Error en login:", err);
        mensaje.textContent = "Error al conectar con el servidor.";
      }
    });
  });
  