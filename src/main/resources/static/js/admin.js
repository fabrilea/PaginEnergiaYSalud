import { checkToken, logout } from "./auth.js";

document.addEventListener("DOMContentLoaded", () => {
  const payload = checkToken("ADMIN");
  if (!payload) return; // ya redirige si falla

  const nombre = payload.nombre || payload.sub || payload.dni || "Administrador";
  const adminNombre = document.getElementById("adminNombre");
  if (adminNombre) adminNombre.textContent = nombre;

  // logout
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);
});
