import { checkToken, logout } from "./auth.js";

document.addEventListener("DOMContentLoaded", async () => {
  const payload = checkToken(["USER", "ADMIN"]);
  if (!payload) return;

  const nombre = payload.nombre || payload.sub || payload.dni || "Usuario";
  const usuarioNombre = document.getElementById("usuarioNombre");
  if (usuarioNombre) usuarioNombre.textContent = nombre;

  console.log(`Usuario autenticado: ${payload.sub}`);

  const logoutBtn = document.getElementById("salirBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);
});
