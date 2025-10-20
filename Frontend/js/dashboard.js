document.addEventListener("DOMContentLoaded", () => {
  const mainContent = document.getElementById("mainContent");
  const buttons = document.querySelectorAll(".menu-btn");
  const logoutBtn = document.getElementById("logoutBtn");
  const API_URL = "http://localhost:8080/api";

  // Verificar token
  const token = localStorage.getItem("token");
  if (!token) {
    alert("Debe iniciar sesión primero");
    window.location.href = "../modules/login.html";
    return;
  }

  // Cerrar sesión
  logoutBtn.addEventListener("click", () => {
    localStorage.clear();
    alert("Sesión cerrada correctamente");
    window.location.href = "../index.html";
  });

  // Navegación entre secciones
  buttons.forEach((btn) => {
    btn.addEventListener("click", () => {
      const section = btn.dataset.section;
      switch (section) {
        case "productos":
          loadProductForm();
          break;
        case "verProductos":
          loadProductList();
          break;
        case "ventas":
          loadVentas();
          break;
        case "configuracion":
          loadConfiguracion();
          break;
        default:
          mainContent.innerHTML = `<h2>Bienvenido al panel de gestión</h2>`;
      }
    });
  });

  // ===========================
  // 🟢 CARGAR PRODUCTO
  // ===========================
  function loadProductForm() {
    mainContent.innerHTML = `
      <h2>Cargar producto</h2>
      <form id="productForm" class="form-producto">
        <label>Nombre:</label>
        <input type="text" id="nombre" required>

        <label>Descripción:</label>
        <textarea id="descripcion" rows="3" required></textarea>

        <label>Precio:</label>
        <input type="number" id="precio" step="0.01" required>

        <label>Stock:</label>
        <input type="number" id="stock" min="0" required>

        <label>ID de IVA:</label>
        <input type="number" id="idIva" min="1" required>

        <button type="submit">Guardar producto</button>
      </form>
    `;

    document.getElementById("productForm").addEventListener("submit", async (e) => {
      e.preventDefault();

      const producto = {
        nombre: document.getElementById("nombre").value,
        descripcion: document.getElementById("descripcion").value,
        precio: parseFloat(document.getElementById("precio").value),
        stock: parseInt(document.getElementById("stock").value),
        idIva: parseInt(document.getElementById("idIva").value)
      };

      try {
        const res = await fetch(`${API_URL}/producto/crear`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify(producto)
        });

        if (res.ok) {
          alert("✅ Producto creado correctamente");
          loadProductList(); // refresca la lista
        } else {
          const error = await res.text();
          alert("❌ Error al crear el producto:\n" + error);
        }
      } catch (err) {
        console.error(err);
        alert("⚠️ Error al conectar con el servidor");
      }
    });
  }

  // ===========================
  // 🟡 LISTAR PRODUCTOS
  // ===========================
  async function loadProductList() {
  // muestra carga mientras viene la respuesta
  mainContent.innerHTML = `
    <h2>Mis productos</h2>
    <div id="productosContainer" class="productos-container">
      <p>Cargando productos...</p>
    </div>
  `;

  const productosContainer = document.getElementById("productosContainer");

  try {
    const token = localStorage.getItem("token");
    if (!token) {
      productosContainer.innerHTML = "<p style='color:red;'>No autenticado. Inicie sesión.</p>";
      return;
    }

    const res = await fetch(`${API_URL}/producto/obtenerTodos`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    });

    // Si el servidor responde con error (4xx/5xx) lo manejamos
    if (!res.ok) {
      const text = await res.text().catch(() => "");
      console.error("Respuesta no OK:", res.status, text);
      productosContainer.innerHTML = `<p style="color:red;">Error al obtener productos (status ${res.status})</p>`;
      return;
    }

    const productos = await res.json();

    if (!productos || productos.length === 0) {
      productosContainer.innerHTML = "<p>No hay productos cargados aún.</p>";
      return;
    }

    // formateo seguro del precio (puede venir string o number)
    const formatPrice = (p) => {
      if (p === null || p === undefined) return "N/A";
      const n = typeof p === "string" ? parseFloat(p) : p;
      return isNaN(n) ? "N/A" : n.toFixed(2);
    };

    productosContainer.innerHTML = productos.map(prod => `
      <div class="producto-card">
        <h3>${escapeHtml(prod.nombre)}</h3>
        <p class="descripcion">${escapeHtml(prod.descripcion)}</p>
        <p><strong>Precio:</strong> $${formatPrice(prod.precio)}</p>
        <p><strong>Stock:</strong> ${prod.stock ?? "N/A"}</p>
        <p><strong>IVA:</strong> ${prod.iva ? escapeHtml(prod.iva.tipo ?? String(prod.iva)) : 'N/A'}</p>
      </div>
    `).join("");

  } catch (error) {
    console.error("Error al obtener productos:", error);
    productosContainer.innerHTML = "<p style='color:red;'>Error al cargar los productos. Revisa consola o el backend.</p>";
  }
}

// pequeña utilidad para evitar inyección de HTML si los campos vienen sucios
function escapeHtml(str) {
  if (!str && str !== 0) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}


  // ===========================
  // 🔵 VENTAS
  // ===========================
  function loadVentas() {
    mainContent.innerHTML = `
      <h2>Historial de ventas</h2>
      <p>(Aquí se mostrarán las ventas realizadas)</p>
    `;
  }

  // ===========================
  // ⚙️ CONFIGURACIÓN
  // ===========================
  function loadConfiguracion() {
    mainContent.innerHTML = `
      <h2>Configuración del emprendimiento</h2>
      <p>Aquí podrás actualizar tu información o cambiar el tema.</p>
    `;
  }
});
