import { checkToken, logout } from "./auth.js";

document.addEventListener("DOMContentLoaded", () => {
  try {
    // ✅ Verificar token y rol ADMIN
    const payload = checkToken("ADMIN");
    if (!payload) return;

    // ✅ Mostrar nombre del admin
    const nombre = payload.nombre || payload.sub || "Administrador";
    const adminNombre = document.getElementById("adminNombre");
    if (adminNombre) adminNombre.textContent = nombre;

    // ✅ Logout
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) logoutBtn.addEventListener("click", logout);

    console.log("✅ Sesión válida como ADMIN.");
  } catch (err) {
    console.error("Error al validar token:", err);
    localStorage.removeItem("token");
    window.location.href = "/login";
  }
});
