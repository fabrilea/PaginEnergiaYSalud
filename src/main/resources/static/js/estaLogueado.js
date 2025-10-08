import { logout } from "/js/auth.js";

document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("token");
  const navUser = document.getElementById("navUser");
  const logoutBtn = document.getElementById("navLogoutBtn");

  if (token) {
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const nombre = payload.nombre || payload.sub || payload.dni || "Usuario";
      const rol = payload.rol ? ` (${payload.rol})` : "";

      if (navUser) navUser.textContent = `👤 ${nombre}${rol}`;
      if (logoutBtn) {
        logoutBtn.classList.remove("d-none"); // Mostrar el botón solo si hay token
        logoutBtn.addEventListener("click", logout);
      }
    } catch (e) {
      console.error("Error leyendo token:", e);
    }
  } else {
    if (logoutBtn) logoutBtn.classList.add("d-none");
    if (navUser) navUser.textContent = "";
  }

  // 🔹 Cerrar automáticamente el menú móvil al hacer clic en un enlace
  const navLinks = document.querySelectorAll(".navbar-nav .nav-link");
  const navbarCollapse = document.querySelector(".navbar-collapse");

  if (navLinks.length && navbarCollapse && typeof bootstrap !== "undefined") {
    navLinks.forEach(link => {
      link.addEventListener("click", () => {
        if (navbarCollapse.classList.contains("show")) {
          new bootstrap.Collapse(navbarCollapse).toggle();
        }
      });
    });
  }
});
