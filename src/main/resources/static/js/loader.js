/**
 * loader.js
 * Control centralizado del loader y del bloqueo visual hasta validar sesión.
 * 
 * 👉 Incluir en cualquier vista con <script type="module" th:src="@{/js/loader.js}"></script>
 */

export function showLoader() {
    const loader = document.getElementById("loader");
    const body = document.getElementById("mainBody");
  
    if (loader) loader.style.display = "flex";
    if (body) body.style.display = "none";
  }
  
  export function hideLoader() {
    const loader = document.getElementById("loader");
    const body = document.getElementById("mainBody");
  
    if (loader) {
      loader.style.opacity = "0";
      setTimeout(() => (loader.style.display = "none"), 300);
    }
  
    if (body) body.style.display = "block";
  }
  
  export function blockUntilReady(callback) {
    // Muestra loader hasta que callback termine (promesa o función normal)
    showLoader();
    const result = callback();
  
    if (result instanceof Promise) {
      result.finally(() => hideLoader());
    } else {
      hideLoader();
    }
  }
  