import { checkToken, logout, fetchWithAuth } from "./auth.js";

document.addEventListener("DOMContentLoaded", async () => {
  // ✅ 1. Verificar token y rol ADMIN
  const payload = checkToken("ADMIN");
  if (!payload) return;

  // ✅ 2. Mostrar el nombre del admin
  const nombre = payload.nombre || payload.sub || payload.dni || "Administrador";
  const adminNombre = document.getElementById("adminNombre");
  if (adminNombre) adminNombre.textContent = nombre;

  // ✅ 3. Agregar logout funcional
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);

  // ✅ 4. Cargar datos de usuarios (ejemplo)
  try {
    const res = await fetchWithAuth("/admin/usuarios/json"); // 👈 mejor usar endpoint JSON
    if (!res.ok) {
      console.warn("⚠️ No se pudieron cargar los usuarios. Código:", res.status);
      return;
    }

    const usuarios = await res.json();
    console.log("Usuarios cargados:", usuarios);

    // (opcional) mostrar en una lista:
    const lista = document.getElementById("listaUsuarios");
    if (lista) {
      lista.innerHTML = usuarios.map(u =>
        `<li>${u.nombre} ${u.apellido} - DNI: ${u.dni}</li>`
      ).join("");
    }

  } catch (err) {
    console.error("💥 Error al cargar usuarios:", err);
  }
});
