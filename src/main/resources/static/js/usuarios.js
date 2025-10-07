import { checkToken, logout, fetchWithAuth } from "./auth.js";
import { showLoader, hideLoader } from "./loader.js";

document.addEventListener("DOMContentLoaded", async () => {
  showLoader(); // ⏳ Oculta contenido y muestra el loader visual

  try {
    // ✅ 1. Verificar token y rol
    const payload = checkToken("ADMIN");
    if (!payload) return;

    // ✅ 2. Mostrar nombre del admin
    const nombre = payload.nombre || payload.sub || payload.dni || "Administrador";
    const adminNombre = document.getElementById("adminNombre");
    if (adminNombre) adminNombre.textContent = nombre;

    // ✅ 3. Logout funcional
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) logoutBtn.addEventListener("click", logout);

    // ✅ 4. Cargar datos opcionales
    try {
      const res = await fetchWithAuth("/admin/usuarios/json");
      if (res.ok) {
        const usuarios = await res.json();
        console.log("Usuarios cargados:", usuarios);
      } else {
        console.warn("⚠️ No se pudieron cargar los usuarios. Código:", res.status);
      }
    } catch (err) {
      console.error("💥 Error al cargar usuarios:", err);
    }

  } catch (err) {
    console.error("Error general en panel admin:", err);
    localStorage.removeItem("token");
    window.location.replace("/login");
  } finally {
    // ✅ 5. Mostrar contenido y ocultar loader
    hideLoader();
  }
});
