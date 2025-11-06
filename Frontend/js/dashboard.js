document.addEventListener("DOMContentLoaded", () => {
  const mainContent = document.getElementById("mainContent");
  const buttons = document.querySelectorAll(".menu-btn");
  const logoutBtn = document.getElementById("logoutBtn");
  const API_URL = "http://localhost:8080/api";

  const token = localStorage.getItem("token");
  if (!token) {
    alert("Debe iniciar sesión primero");
    window.location.href = "../modules/login.html";
    return;
  }

  logoutBtn.addEventListener("click", () => {
    localStorage.clear();
    alert("Sesión cerrada correctamente");
    window.location.href = "../index.html";
  });

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

        <label>Imágen representativa</label>
        <input type="file" id="image" name="image" accept="image/*" required></input>

        <button type="submit">Guardar producto</button>
      </form>
    `;

    document.getElementById("productForm").addEventListener("submit", async (e) => {
      e.preventDefault();

      const formData = new FormData();

      const nombre = document.getElementById("nombre").value
      const descripcion = document.getElementById("descripcion").value
      const precio = parseFloat(document.getElementById("precio").value)
      const stock = parseInt(document.getElementById("stock").value)
      const idIva = parseInt(document.getElementById("idIva").value)
      const imgProducto = document.getElementById("image").files[0]

      formData.append("nombre", nombre)
      formData.append("descripcion", descripcion)
      formData.append("precio", precio)
      formData.append("stock", stock)
      formData.append("idIva", idIva)
      formData.append("imgProducto", imgProducto)

      try {
        const res = await fetch(`${API_URL}/admin/producto/crear`, {
          method: "POST",
          headers: {
            "Authorization": `Bearer ${token}`
          },
          body: formData
        });

        if (res.ok) {
          alert("Producto creado correctamente");
          loadProductList(); 
        } else {
          const error = await res.text();
          alert("Error al crear el producto:\n" + error);
        }
      } catch (err) {
        console.error(err);
        alert("Error al conectar con el servidor");
      }
    });
  }

  async function loadProductList() {
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

      const res = await fetch(`${API_URL}/admin/producto/obtenerTodos`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        }
      });

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
        <img src=${prod.url ? escapeHtml(prod.url ?? String(prod.url)) : 'N/A'}>
      </div>
    `).join("");

    } catch (error) {
      console.error("Error al obtener productos:", error);
      productosContainer.innerHTML = "<p style='color:red;'>Error al cargar los productos. Revisa consola o el backend.</p>";
    }
  }

  function escapeHtml(str) {
    if (!str && str !== 0) return "";
    return String(str)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
  }

  function loadVentas() {
    mainContent.innerHTML = `
      <h2>Historial de ventas</h2>
      <p>(Aquí se mostrarán las ventas realizadas)</p>
    `;
  }

  function loadConfiguracion() {
    mainContent.innerHTML = `
    <h2>Configuración del emprendimiento</h2>
    <form id="configForm" class="form-config">
      <label for="titulo">Nombre del emprendimiento:</label>
      <input type="text" id="titulo" required>

      <label for="descripcion">Descripción:</label>
      <textarea id="descripcion" rows="3"></textarea>

      <label for="direccion">Dirección:</label>
      <input type="text" id="direccion" required>

      <label>Contactos:</label>
      <ul id="listaContactos"></ul>
      <div class="add-contacto">
        <input type="text" id="nuevoContacto" placeholder="Teléfono, mail, red social">
        <button type="button" id="btnAgregarContacto">Agregar</button>
      </div>

      <button type="submit">Guardar cambios</button>
    </form>
  `;

    const listaContactos = document.getElementById("listaContactos");
    const btnAgregar = document.getElementById("btnAgregarContacto");
    const inputContacto = document.getElementById("nuevoContacto");
    const token = localStorage.getItem("token");
    let config = { titulo: "", descripcion: "", direccion: "", contactos: [] };

    async function cargarConfigDesdeBackend() {
      try {
        const res = await fetch(`${API_URL}/emprendimiento/emp`, {
          headers: { "Authorization": `Bearer ${token}` }
        });
        if (!res.ok) throw new Error("Error al obtener configuración");
        config = await res.json();
        renderConfig();
      } catch (err) {
        console.warn("Usando configuración local o vacía:", err);
        config = JSON.parse(localStorage.getItem("configEmprendimiento")) || config;
        renderConfig();
      }
    }

    function renderConfig() {
      document.getElementById("titulo").value = config.titulo || "";
      document.getElementById("descripcion").value = config.descripcion || "";
      document.getElementById("direccion").value = config.direccion || "";
      renderContactos();
    }

    function renderContactos() {
      listaContactos.innerHTML = "";
      config.contactos.forEach((c, i) => {
        const li = document.createElement("li");
        li.innerHTML = `${c} <button class="btn-eliminar-contacto" data-index="${i}">x</button>`;
        listaContactos.appendChild(li);
      });
      document.querySelectorAll(".btn-eliminar-contacto").forEach(btn => {
        btn.addEventListener("click", e => {
          const index = e.target.dataset.index;
          config.contactos.splice(index, 1);
          renderContactos();
        });
      });
    }

    btnAgregar.addEventListener("click", () => {
      const valor = inputContacto.value.trim();
      if (valor) {
        config.contactos.push(valor);
        inputContacto.value = "";
        renderContactos();
      }
    });

    document.getElementById("configForm").addEventListener("submit", async e => {
      e.preventDefault();
      config.titulo = document.getElementById("titulo").value;
      config.descripcion = document.getElementById("descripcion").value;
      config.direccion = document.getElementById("direccion").value;

      try {
        const res = await fetch(`${API_URL}/emprendimiento/contactos`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify(config)
        });

        if (!res.ok) throw new Error("Error al guardar en backend");
        alert("Configuración guardada correctamente.");

        localStorage.setItem("configEmprendimiento", JSON.stringify(config));

        const headerTitle = document.querySelector(".header h1");
        if (headerTitle) headerTitle.textContent = config.titulo;

      } catch (err) {
        console.error(err);
        alert("Error al guardar en el servidor. Cambios solo locales.");
        localStorage.setItem("configEmprendimiento", JSON.stringify(config));
      }
    });

    cargarConfigDesdeBackend();
  }
});