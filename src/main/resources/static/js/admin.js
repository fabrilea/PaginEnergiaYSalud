import { checkToken, logout } from "./auth.js";

document.addEventListener("DOMContentLoaded", () => {
  // ✅ 1. Verificar token y rol ADMIN
  const payload = checkToken("ADMIN");
  if (!payload) return;

  // ✅ 2. Mostrar el nombre del admin si existe
  const nombre = payload.nombre || payload.sub || "Administrador";
  const adminNombre = document.getElementById("adminNombre");
  if (adminNombre) adminNombre.textContent = nombre;

  // ✅ 3. Agregar logout funcional
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);

  console.log("✅ Sesión válida como ADMIN.");
});
