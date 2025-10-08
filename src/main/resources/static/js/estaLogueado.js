import { logout } from "/js/auth.js";

document.addEventListener("DOMContentLoaded", () => {
  // === 🟦 1. Obtener elementos principales ===
  const token = localStorage.getItem("token");
  const navUser = document.getElementById("navUser");
  const logoutBtn = document.getElementById("navLogoutBtn");
  const salirBtn = document.getElementById("salirBtn"); // botón adicional opcional

  // === 🟨 2. Mostrar usuario si hay token ===
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const nombre = payload.nombre || payload.sub || payload.dni || "Usuario";
      const rol = payload.rol ? ` (${payload.rol})` : "";

      if (navUser) navUser.textContent = `👤 ${nombre}${rol}`;

      // Mostrar botón logout solo si hay token
      if (logoutBtn) {
        logoutBtn.classList.remove("d-none");
        logoutBtn.addEventListener("click", logout);
      }
      if (salirBtn) salirBtn.addEventListener("click", logout);
    } catch (e) {
      console.error("Error leyendo token:", e);
      localStorage.removeItem("token");
      window.location.href = "/login";
    }
  } else {
    if (logoutBtn) logoutBtn.classList.add("d-none");
    if (navUser) navUser.textContent = "";
  }

  // === 🟩 3. Cerrar automáticamente el menú móvil al hacer clic ===
  const navLinks = document.querySelectorAll(".navbar-nav .nav-link");
  const navbarCollapse = document.querySelector(".navbar-collapse");
  const toggler = document.querySelector(".navbar-toggler");

  if (navLinks.length && navbarCollapse) {
    navLinks.forEach(link => {
      link.addEventListener("click", () => {
        const isExpanded = toggler?.getAttribute("aria-expanded") === "true";

        if (
          typeof bootstrap !== "undefined" &&
          navbarCollapse.classList.contains("show") &&
          isExpanded
        ) {
          try {
            new bootstrap.Collapse(navbarCollapse).toggle();
          } catch (err) {
            console.warn("⚠️ No se pudo colapsar el menú:", err);
          }
        }
      });
    });
  }
});
