document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
  
    if (!token) {
      alert("Tu sesión ha expirado. Iniciá sesión nuevamente.");
      window.location.href = "/login";
      return;
    }
  
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      if (payload.rol !== "ADMIN") {
        alert("Acceso denegado. Solo administradores.");
        window.location.href = "/login";
        return;
      }
    } catch (e) {
      console.error("Token inválido:", e);
      localStorage.removeItem("token");
      window.location.href = "/login";
    }
  });
  