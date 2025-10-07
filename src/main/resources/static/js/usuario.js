import { checkToken, logout } from "./auth.js";

document.addEventListener("DOMContentLoaded", () => {
  const payload = checkToken(["USER", "ADMIN"]); // 👈 acepta ambos roles
  if (!payload) return;

  const nombre = payload.nombre || payload.sub || payload.dni || "Usuario";
  const usuarioNombre = document.getElementById("usuarioNombre");
  if (usuarioNombre) usuarioNombre.textContent = nombre;

  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);
});
