import { checkToken, logout } from "./auth.js";

document.addEventListener("DOMContentLoaded", async () => {
  const payload = checkToken(["USER", "ADMIN"]);
  if (!payload) return;

  // Mostrar nombre
  const nombre = payload.nombre || payload.sub || payload.dni || "Usuario";
  const usuarioNombre = document.getElementById("usuarioNombre");
  if (usuarioNombre) usuarioNombre.textContent = nombre;

  // 👉 No cargamos con fetch, solo dejamos que el HTML (Thymeleaf) lo muestre
  console.log(`Usuario autenticado: ${payload.sub}`);

  // Botón cerrar sesión
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);
});
