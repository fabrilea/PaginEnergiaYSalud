      
document.addEventListener("DOMContentLoaded", () => {
  const navLinks = document.querySelectorAll(".navbar-nav .nav-link");
  const navbarCollapse = document.querySelector(".navbar-collapse");

  if (!navLinks.length || !navbarCollapse) return;

  navLinks.forEach(link => {
    link.addEventListener("click", () => {
      // Solo cerrar si bootstrap está cargado y el menú está abierto
      if (typeof bootstrap !== "undefined" && navbarCollapse.classList.contains("show")) {
        new bootstrap.Collapse(navbarCollapse).toggle();
      }
    });
  });
});

