/**
 * auth.js
 * Módulo centralizado para validación de tokens JWT y control de sesión.
 */

export function checkToken(requiredRoles = null) {
    const token = localStorage.getItem("token");
  
    // 🟥 No hay token → redirigir a login
    if (!token) {
      window.location.href = "/login";
      return null;
    }
  
    try {
      // 🔎 Decodificar token
      const payload = JSON.parse(atob(token.split(".")[1]));
      const exp = payload.exp * 1000;
      const ahora = Date.now();
  
      // 🕒 Token expirado
      if (ahora > exp) {
        alert("Tu sesión ha expirado. Por favor, inicia sesión nuevamente.");
        localStorage.removeItem("token");
        window.location.href = "/login";
        return null;
      }
  
      // 🧩 Validar rol si se especifica
      if (requiredRoles) {
        // Acepta string ("ADMIN") o array (["USER", "ADMIN"])
        const roles = Array.isArray(requiredRoles) ? requiredRoles : [requiredRoles];
  
        if (!roles.includes(payload.rol)) {
          alert("Acceso denegado: no tienes permiso para ver esta página.");
          localStorage.removeItem("token");
          window.location.href = "/login";
          return null;
        }
      }
  
      return payload; // ✅ Devuelve los datos decodificados
    } catch (err) {
      console.error("Error al validar token:", err);
      localStorage.removeItem("token");
      window.location.href = "/login";
      return null;
    }
  }
  
  /**
   * Limpia el token y redirige al login.
   */
  export function logout() {
    localStorage.removeItem("token");
    window.location.href = "/login";
  }
  