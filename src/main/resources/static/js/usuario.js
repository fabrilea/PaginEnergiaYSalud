import { checkToken, logout } from "./auth.js";
import { showLoader, hideLoader } from "./loader.js";

document.addEventListener("DOMContentLoaded", async () => {
  showLoader(); // ⏳ Bloquear renderizado y mostrar spinner

  try {
    // ✅ Verificar token válido (permite USER y ADMIN)
    const payload = checkToken(["USER", "ADMIN"]);
    if (!payload) return;

    // ✅ Mostrar nombre
    const nombre = payload.nombre || payload.sub || payload.dni || "Usuario";
    const usuarioNombre = document.getElementById("usuarioNombre");
    if (usuarioNombre) usuarioNombre.textContent = nombre;

    console.log(`✅ Usuario autenticado: ${payload.sub}`);

    // ✅ Logout funcional
    const logoutBtn = document.getElementById("salirBtn");
    if (logoutBtn) logoutBtn.addEventListener("click", logout);

  } catch (err) {
    console.error("Error al validar token:", err);
    localStorage.removeItem("token");
    window.location.replace("/login");
  } finally {
    hideLoader(); // ✅ Mostrar contenido y ocultar spinner
  }
});
