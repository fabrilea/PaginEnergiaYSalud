document.addEventListener("DOMContentLoaded", () => {
    // 🔹 Obtener elementos
    const navLinks = document.querySelectorAll(".navbar-nav .nav-link");
    const navbarCollapse = document.querySelector(".navbar-collapse");
  
    // Si no existe navbar o links, salir
    if (!navLinks.length || !navbarCollapse) return;
  
    navLinks.forEach(link => {
      link.addEventListener("click", () => {
        // Solo cerrar si Bootstrap está cargado y el menú está abierto
        if (
          typeof bootstrap !== "undefined" &&
          navbarCollapse.classList.contains("show")
        ) {
          try {
            new bootstrap.Collapse(navbarCollapse).toggle();
          } catch (err) {
            console.warn("⚠️ No se pudo colapsar el menú:", err);
          }
        }
      });
    });
  });
  