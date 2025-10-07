import { checkToken, logout } from "./auth.js";

document.addEventListener("DOMContentLoaded", async () => {
    try {
      const res = await fetchWithAuth("/admin/usuarios");
      const data = await res.json();
      console.log("Usuarios:", data);
    } catch (err) {
      console.error("Error al cargar usuarios:", err);
    }
  });
document.addEventListener("DOMContentLoaded", () => {
  const payload = checkToken("ADMIN");
  if (!payload) return;

  const nombre = payload.nombre || payload.sub || payload.dni || "Administrador";
  const adminNombre = document.getElementById("adminNombre");
  if (adminNombre) adminNombre.textContent = nombre;

  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);
});
