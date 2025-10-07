import { checkToken, logout } from "./auth.js";
import { showLoader, hideLoader } from "./loader.js";

document.addEventListener("DOMContentLoaded", () => {
  showLoader(); // ⏳ Oculta contenido y muestra el spinner

  try {
    const payload = checkToken("ADMIN");
    if (!payload) return;

    const nombre = payload.nombre || payload.sub || "Administrador";
    const adminNombre = document.getElementById("adminNombre");
    if (adminNombre) adminNombre.textContent = nombre;

    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) logoutBtn.addEventListener("click", logout);

    // ✅ Todo OK → mostrar contenido
    hideLoader();
    console.log("✅ Sesión válida como ADMIN.");
  } catch (err) {
    console.error("Error al validar token:", err);
    localStorage.removeItem("token");
    window.location.replace("/login");
  }
});
