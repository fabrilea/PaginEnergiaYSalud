import { checkToken, logout, fetchWithAuth } from "./auth.js";

document.addEventListener("DOMContentLoaded", async () => {
  const payload = checkToken(["USER", "ADMIN"]);
  if (!payload) return;

  // Mostrar nombre
  const nombre = payload.nombre || payload.sub || payload.dni || "Usuario";
  const usuarioNombre = document.getElementById("usuarioNombre");
  if (usuarioNombre) usuarioNombre.textContent = nombre;

  // Cargar rutinas del usuario autenticado
  try {
    const res = await fetchWithAuth(`/usuario/${payload.sub}`);
    if (res.ok) {
      const html = await res.text();
      document.getElementById("contenidoUsuario").innerHTML = html;
    } else {
      console.error("No se pudieron cargar las rutinas del usuario.");
    }
  } catch (err) {
    console.error("Error al cargar rutinas:", err);
  }

  // Cerrar sesión
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);
});
