import { checkToken, fetchWithAuth, logout } from "./auth.js";

document.addEventListener("DOMContentLoaded", async () => {
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

  // ✅ 4. Cargar datos del panel (ejemplo)
  try {
    const res = await fetchWithAuth("/admin");
    if (res.ok) {
      const html = await res.text();
      console.log("Panel cargado correctamente.");
      // Podés renderizar algo dinámico si querés
    } else {
      console.warn("No se pudo cargar el panel admin.");
    }
  } catch (err) {
    console.error("Error al cargar panel admin:", err);
  }
});
