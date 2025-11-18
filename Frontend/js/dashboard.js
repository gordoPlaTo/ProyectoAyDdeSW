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
        case "materiales":
          loadMateriales();
          break;
        case "ventas":
          loadVentas();
          break;
        case "gestionVentas":
          loadGestionVentas();
          break;
        case "configuracion":
          loadConfiguracion();
          break;
        default:
          mainContent.innerHTML = `<h2>Bienvenido al panel de gestión</h2>`;
      }
    });
  });

  // =====================
  // CARGAR PRODUCTO
  // ===================
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

        if (res.status === 401) {
          alert("La sesión ha expirado. Inicia sesión nuevamente.");
          localStorage.clear();
          window.location.href = "/Frontend/modules/login.html";
          return null;
        }

        if (res.ok) {
          alert("Producto creado correctamente");
          loadProductList(); // refresca la lista
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

  // =====================
  // Obtener Productos
  // ======================
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

      if (res.status === 401) {
        alert("La sesión ha expirado. Inicia sesión nuevamente.");
        localStorage.clear();
        window.location.href = "/Frontend/modules/login.html";
        return null;
      }

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

      // Activar clic sobre los productos para abrir el modal
      document.querySelectorAll(".producto-card").forEach((card, i) => {
        card.addEventListener("click", () => openEditModal(productos[i]));
      });


    } catch (error) {
      console.error("Error al obtener productos:", error);
      productosContainer.innerHTML = "<p style='color:red;'>Error al cargar los productos. Revisa consola o el backend.</p>";
    }
  }

  // ====================================
  // Modal para editar productos
  // =======================================

  function openEditModal(producto) {
    const modalOverlay = document.createElement("div");
    modalOverlay.classList.add("modal-overlay");

    // Ajusta el nombre del campo de estado si en tu DTO se llama distinto:
    const estaHabilitado = producto.habilitado ?? producto.enable ?? producto.estado ?? false;

    modalOverlay.innerHTML = `
    <div class="modal">
      <h3>Editar producto: ${escapeHtml(producto.nombre)}</h3>
      <label>Precio:</label>
      <input type="number" id="editPrecio" value="${producto.precio}" step="0.01">

      <label>Stock:</label>
      <input type="number" id="editStock" value="${producto.stock}" min="0">

      <div class="btns">
        <button class="btn-save">Guardar</button>
        <button class="btn-disable">${estaHabilitado ? "Deshabilitar" : "Habilitar"}</button>
        <button class="btn-close">Cerrar</button>
      </div>
    </div>
  `;

    document.body.appendChild(modalOverlay);

    const btnGuardar = modalOverlay.querySelector(".btn-save");
    const btnCerrar = modalOverlay.querySelector(".btn-close");
    const btnDeshabilitar = modalOverlay.querySelector(".btn-disable");

    // Cerrar modal
    btnCerrar.addEventListener("click", () => modalOverlay.remove());

    // ===========================
    // GUARDAR CAMBIOS
    // ===========================
    btnGuardar.addEventListener("click", async () => {
      const nuevoPrecio = parseFloat(document.getElementById("editPrecio").value);
      const nuevoStock = parseInt(document.getElementById("editStock").value);

      try {
        // PATCH para actualizar precio
        const resPrecio = await fetch(`${API_URL}/admin/producto/mod/${producto.idProducto}`, {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify({ precio: nuevoPrecio })
        });

        if (resPrecio.status === 401) {
          alert("La sesión ha expirado. Inicia sesión nuevamente.");
          localStorage.clear();
          window.location.href = "/Frontend/modules/login.html";
          return null;
        }

        if (!resPrecio.ok) throw new Error("Error al modificar precio");

        // Detectar si stock aumentó o disminuyó
        let endpointStock = null;
        if (nuevoStock > producto.stock) {
          const diff = nuevoStock - producto.stock;
          endpointStock = `${API_URL}/admin/producto/aumentar/${producto.idProducto}?stock=${diff}`;
        } else if (nuevoStock < producto.stock) {
          const diff = producto.stock - nuevoStock;
          endpointStock = `${API_URL}/admin/producto/reducir/${producto.idProducto}?stock=${diff}`;
        }

        // Llamar a endpoint correspondiente si hay cambio
        if (endpointStock) {
          const resStock = await fetch(endpointStock, {
            method: "PATCH",
            headers: { "Authorization": `Bearer ${token}` }
          });
          if (!resStock.ok) throw new Error("Error al modificar stock");
        }

        alert("Producto actualizado correctamente");
        modalOverlay.remove();
        loadProductList();

      } catch (err) {
        console.error(err);
        alert("No se pudo guardar los cambios");
      }
    });

    // ===========================
    // HABILITAR / DESHABILITAR PRODUCTO
    // ===========================
    btnDeshabilitar.addEventListener("click", async () => {
      try {
        const res = await fetch(`${API_URL}/admin/producto/estado/${producto.idProducto}`, {
          method: "PATCH",
          headers: { "Authorization": `Bearer ${token}` }
        });

        if (res.status === 401) {
          alert("La sesión ha expirado. Inicia sesión nuevamente.");
          localStorage.clear();
          window.location.href = "/Frontend/modules/login.html";
          return null;
        }

        if (!res.ok) throw new Error("Error al cambiar estado");

        alert("Estado del producto actualizado correctamente");
        modalOverlay.remove();
        loadProductList();

      } catch (err) {
        console.error(err);
        alert("No se pudo cambiar el estado del producto");
      }
    });
  }

  // =====================
  // MATERIALES (ver + agregar)
  // =====================
  async function loadMateriales() {
    mainContent.innerHTML = `
    <h2>Gestión de Materiales</h2>

    <section class="material-form">
      <h3>Agregar nuevo material</h3>
      <form id="materialForm" class="form-material" enctype="multipart/form-data">
        <label>Nombre:</label>
        <input type="text" id="nombreMat" required>

        <label>Descripción:</label>
        <textarea id="descripcionMat" rows="2" required></textarea>

        <label>Stock:</label>
        <input type="number" id="stockMat" min="0" required>

        <label>Imagen del material:</label>
        <input type="file" id="imgMat" accept="image/*" required>

        <button type="submit">Guardar material</button>
      </form>
    </section>

    <section class="material-list">
      <h3>Materiales cargados</h3>
      <div id="materialesContainer" class="materiales-container">
        <p>Cargando materiales...</p>
      </div>
    </section>
  `;

    const materialesContainer = document.getElementById("materialesContainer");

    // ==================
    // CARGAR LISTA
    // =====================
    async function loadMaterialList() {
      try {
        const res = await fetch(`${API_URL}/admin/material/obtenerTodos`, {
          headers: { "Authorization": `Bearer ${token}` }
        });

        if (res.status === 401) {
          alert("La sesión ha expirado. Inicia sesión nuevamente.");
          localStorage.clear();
          window.location.href = "/Frontend/modules/login.html";
          return null;
        }

        if (!res.ok) {
          materialesContainer.innerHTML = `<p style="color:red;">Error al obtener materiales.</p>`;
          return;
        }

        const materiales = await res.json();

        if (!materiales || materiales.length === 0) {
          materialesContainer.innerHTML = "<p>No hay materiales cargados aún.</p>";
          return;
        }

        materialesContainer.innerHTML = materiales.map(mat => `
        <div class="material-card" data-id="${mat.idMaterial}">
          <h4>${mat.nombre}</h4>
          <p>${mat.descripcion}</p>
          <p><strong>Stock:</strong> ${mat.stock}</p>

          ${mat.urlfoto ? `<img src="${mat.urlfoto}" alt="${mat.nombre}" width="250" height="250" class="material-img">` : ""}


          <div class="acciones">
            <button class="btn-stock" data-id="${mat.idMaterial}">Modificar stock</button>
            <button class="btn-borrar" data-id="${mat.idMaterial}">Eliminar</button>
          </div>
        </div>
      `).join("");

        // === Eventos ===
        document.querySelectorAll(".btn-stock").forEach(btn => {
          btn.addEventListener("click", () => modificarStock(btn.dataset.id));
        });

        document.querySelectorAll(".btn-borrar").forEach(btn => {
          btn.addEventListener("click", () => eliminarMaterial(btn.dataset.id));
        });

      } catch (error) {
        console.error(error);
        materialesContainer.innerHTML = "<p>Error al cargar materiales.</p>";
      }
    }

    // =====================
    // AGREGAR MATERIAL
    // ====================
    const materialForm = document.getElementById("materialForm");

    if (materialForm) {
      materialForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const nombreInput = document.getElementById("nombreMat");
        const descInput = document.getElementById("descripcionMat");
        const stockInput = document.getElementById("stockMat");
        const imgInput = document.getElementById("imgMat");

        if (!nombreInput || !descInput || !stockInput || !imgInput.files.length) {
          alert("Por favor complete todos los campos, incluyendo la imagen.");
          return;
        }

        const formData = new FormData();
        formData.append("nombre", nombreInput.value.trim());
        formData.append("descripcion", descInput.value.trim());
        formData.append("stock", parseInt(stockInput.value));
        formData.append("imgMaterial", imgInput.files[0]); // obligatorio

        try {
          const res = await fetch(`${API_URL}/admin/material/crear`, {
            method: "POST",
            headers: { "Authorization": `Bearer ${token}` },
            body: formData
          });

          if (res.status === 401) {
            alert("La sesión ha expirado. Inicia sesión nuevamente.");
            localStorage.clear();
            window.location.href = "/Frontend/modules/login.html";
            return null;
          }

          if (!res.ok) {
            const msg = await res.text();
            alert("Error al crear material: " + msg);
            return;
          }

          alert("Material agregado correctamente");
          materialForm.reset();
          loadMaterialList();

        } catch (error) {
          console.error("Error al crear material:", error);
          alert("No se pudo conectar al servidor");
        }
      });
    }

    // =================
    // FUNCIONES AUXILIARES
    // =====================
    async function modificarStock(id) {
      const cantidad = prompt("Ingrese cantidad (use signo - para reducir):");
      if (!cantidad) return;

      const endpoint = cantidad.startsWith("-")
        ? `${API_URL}/admin/material/reducir/${id}?stock=${Math.abs(cantidad)}`
        : `${API_URL}/admin/material/aumentar/${id}?stock=${cantidad}`;

      try {
        const res = await fetch(endpoint, {
          method: "PATCH",
          headers: { "Authorization": `Bearer ${token}` }
        });

        if (res.status === 401) {
          alert("La sesión ha expirado. Inicia sesión nuevamente.");
          localStorage.clear();
          window.location.href = "/Frontend/modules/login.html";
          return null;
        }

        if (res.ok) {
          alert("Stock modificado correctamente");
          loadMaterialList();
        } else {
          alert("Error al modificar stock");
        }
      } catch (error) {
        console.error(error);
      }
    }

    async function eliminarMaterial(id) {
      if (!confirm("¿Seguro que deseas eliminar este material?")) return;
      try {
        const res = await fetch(`${API_URL}/admin/material/borrar/${id}`, {
          method: "DELETE",
          headers: { "Authorization": `Bearer ${token}` }
        });

        if (res.status === 401) {
          alert("La sesión ha expirado. Inicia sesión nuevamente.");
          localStorage.clear();
          window.location.href = "/Frontend/modules/login.html";
          return null;
        }

        if (res.ok) {
          alert("Material eliminado correctamente");
          loadMaterialList();
        } else {
          alert("Error al eliminar material");
        }
      } catch (error) {
        console.error(error);
      }
    }

    // =====================
    // CARGA INICIAL
    // ====================
    loadMaterialList();
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

// =========================
// Ventas (Estadísticas con CSS puro) - FIX DE BUGS
// =========================
async function loadVentas() {
    const mainContent = document.getElementById("mainContent");
    const token = localStorage.getItem("token");

    // 1. Estructura base con estilos incrustados
    mainContent.innerHTML = `
    <style>
      /* Estilos simples para el gráfico CSS */
      .stats-cards { display: flex; gap: 20px; margin-bottom: 30px; }
      .stat-card { flex: 1; background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); text-align: center; }
      .stat-value { font-size: 2em; font-weight: bold; color: #333; }
      .stat-label { color: #666; font-size: 0.9em; text-transform: uppercase; }
      
      .chart-container { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 30px; }
      .bar-row { display: flex; align-items: center; margin-bottom: 15px; }
      .bar-label { width: 150px; font-weight: bold; font-size: 0.9em; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
      .bar-track { flex: 1; background: #f0f0f0; height: 25px; border-radius: 4px; margin: 0 15px; position: relative; }
      .bar-fill { height: 100%; background: #4CAF50; border-radius: 4px; transition: width 0.5s ease; }
      .bar-value { width: 80px; text-align: right; font-weight: bold; }
    </style>

    <h2>Dashboard de Ventas</h2>
    <div id="ventasContent">
      <p>Cargando estadísticas...</p>
    </div>
  `;

    const contentDiv = document.getElementById("ventasContent");

    try {
        const res = await fetch(`${API_URL}/compras/pedido/ventasRealizadas`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (res.status === 401) {
            alert("La sesión ha expirado.");
            localStorage.clear();
            window.location.href = "../modules/login.html";
            return;
        }

        if (!res.ok) {
            contentDiv.innerHTML = `<p style="color:red;">Error al conectar con el servidor.</p>`;
            return;
        }

        const ventas = await res.json();

        if (!ventas || ventas.length === 0) {
            contentDiv.innerHTML = `<p>No hay ventas registradas para generar estadísticas.</p>`;
            return;
        }

        // 2. Cálculos matemáticos (FIX: Usamos || 0 en todas las reducciones)
        const totalIngresos = ventas.reduce((acc, v) => acc + (v.totalGenerado || 0), 0);
        const totalUnidades = ventas.reduce((acc, v) => acc + (v.cantidadVendida || 0), 0);
        
        // Buscamos el valor máximo (FIX: Usamos || 0 al mapear)
        const maxVenta = Math.max(...ventas.map(v => v.totalGenerado || 0));

        // 3. Generar HTML
        // Tarjetas de resumen
        let html = `
        <div class="stats-cards">
          <div class="stat-card">
             <div class="stat-value">$${totalIngresos.toLocaleString('es-AR', {minimumFractionDigits: 2})}</div>
             <div class="stat-label">Ingresos Totales</div>
          </div>
          <div class="stat-card">
             <div class="stat-value">${totalUnidades}</div>
             <div class="stat-label">Productos Vendidos</div>
          </div>
        </div>

        <div class="chart-container">
          <h3>Top Ventas (por Ingresos)</h3>
    `;

        // Generar barras
        const ventasOrdenadas = ventas.sort((a, b) => (b.totalGenerado || 0) - (a.totalGenerado || 0));

        ventasOrdenadas.forEach(v => {
            // FIX: Usamos || 0 antes de calcular el porcentaje y antes de toFixed
            const ingreso = v.totalGenerado || 0;
            const porcentaje = maxVenta === 0 ? 0 : (ingreso / maxVenta) * 100;
            
            html += `
            <div class="bar-row">
              <div class="bar-label" title="${v.nombreProducto}">${v.nombreProducto}</div>
              <div class="bar-track">
                 <div class="bar-fill" style="width: ${porcentaje}%;"></div>
              </div>
              <div class="bar-value">$${ingreso.toFixed(0)}</div>
            </div>
            `;
        });

        html += `</div>`; // Cerrar chart-container

        // Tabla detallada
        html += `
          <h3>Detalle Completo</h3>
          <table class="tabla-ventas" style="width:100%; border-collapse:collapse; margin-top:10px;">
            <thead>
               <tr style="background:#eee; text-align:left;">
                 <th style="padding:10px;">Producto</th>
                 <th style="padding:10px;">Cant.</th>
                 <th style="padding:10px;">Total</th>
               </tr>
            </thead>
            <tbody>
               ${ventasOrdenadas.map(v => `
                 <tr style="border-bottom:1px solid #ddd;">
                   <td style="padding:10px;">${v.nombreProducto}</td>
                   <td style="padding:10px;">${v.cantidadVendida || 0}</td>
                   <td style="padding:10px;">$${(v.totalGenerado || 0).toFixed(2)}</td> 
                 </tr>
               `).join('')}
            </tbody>
          </table>
        `;

        contentDiv.innerHTML = html;

    } catch (err) {
        console.error("Error:", err);
        contentDiv.innerHTML = `<p style="color:red;">Ocurrió un error inesperado al cargar las ventas.</p>`;
    }
}

  // =====================
// GESTIONAR VENTAS (ADMIN)
// =====================
async function loadGestionVentas() {
  mainContent.innerHTML = `
    <h2>Gestionar Ventas</h2>
    <div id="gestionVentasContainer" class="ventas-container">
      <p>Cargando pedidos...</p>
    </div>
  `;

  const cont = document.getElementById("gestionVentasContainer");
  const token = localStorage.getItem("token");

  try {
    const res = await fetch(`${API_URL}/compras/completarPedido`, {
      method: "GET",
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (res.status === 401) {
      alert("La sesión ha expirado. Inicia sesión nuevamente.");
      localStorage.clear();
      window.location.href = "/Frontend/modules/login.html";
      return;
    }

    if (!res.ok) {
      cont.innerHTML = `<p style="color:red;">Error al obtener pedidos.</p>`;
      return;
    }

    const pedidos = await res.json();

    if (!pedidos || pedidos.length === 0) {
      cont.innerHTML = "<p>No hay pedidos pendientes.</p>";
      return;
    }

    cont.innerHTML = pedidos.map(p => `
      <div class="pedido-card-admin" data-id="${p.id}">
        <h3>Pedido #${p.id}</h3>

        <p><strong>Fecha:</strong> ${p.fechaCreacion}</p>
        <p><strong>Estado:</strong> ${p.estadoPedido}</p>

        <p><strong>Total:</strong> $${p.totalCompra}</p>

        <h4>Cliente:</h4>
        <p><strong>Nombre:</strong> ${p.usuario?.nombre ?? "N/A"}</p>
        <p><strong>Email:</strong> ${p.usuario?.email ?? "N/A"}</p>

        ${p.urlComprobante 
            ? `<p><a href="${p.urlComprobante}" target="_blank">Ver comprobante</a></p>`
            : `<p style="color:red;">Sin comprobante</p>`
        }

        <div class="acciones-admin">
          <button class="btn-completar" data-id="${p.id}">Completar</button>
          <button class="btn-detalle" data-id="${p.id}">Detalle</button>
        </div>
      </div>
    `).join("");

    // ==================
    // COMPLETAR PEDIDO
    // ==================
    document.querySelectorAll(".btn-completar").forEach(btn => {
      btn.addEventListener("click", async () => {
        const id = btn.dataset.id;
        if (!confirm("Marcar este pedido como COMPLETADO?")) return;

        try {
          const res = await fetch(`${API_URL}/compras/pedido/admin/completar/${id}`, {
            method: "PATCH",
            headers: { "Authorization": `Bearer ${token}` }
          });

          if (!res.ok) {
            const msg = await res.text();
            alert("Error:\n" + msg);
            return;
          }

          alert("Pedido completado correctamente.");
          loadGestionVentas();

        } catch (err) {
          console.error(err);
          alert("Error al conectar con el servidor");
        }
      });
    });

    // ==================
    // VER DETALLE EN MODAL
    // ==================
    document.querySelectorAll(".btn-detalle").forEach(btn => {
      btn.addEventListener("click", () => {
        const id = btn.dataset.id;
        const pedido = pedidos.find(x => x.id == id);

        const modal = document.createElement("div");
        modal.classList.add("modal-overlay");
        modal.innerHTML = `
          <div class="modal">
            <h3>Detalle del pedido #${pedido.id}</h3>

            <ul>
              ${pedido.detallePedido.map(d => `
                <li>
                  <strong>${d.producto}</strong>  
                  <br>Cantidad: ${d.cantidad}
                  <br>Neto: $${d.precioNeto}
                  <br>IVA: $${d.montoIva}
                  <br>Total: $${d.precioTotal}
                  <hr>
                </li>
              `).join("")}
            </ul>

            <button class="btn-close">Cerrar</button>
          </div>
        `;

        document.body.appendChild(modal);
        modal.querySelector(".btn-close").addEventListener("click", () => modal.remove());
      });
    });

  } catch (err) {
    console.error(err);
    cont.innerHTML = "<p style='color:red;'>Error de conexión.</p>";
  }
}



  // =====================
  // Configuracion
  // ========================
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

      <label>CUIT:</label>
      <input type="text" id="cuit" required>

      <label>Email:</label>
      <input type="text" id="email" required>

      <button type="submit">Guardar cambios</button>
    </form>

    <button id="addAdminBtn" class="btn-add-admin">Agregar nuevo administrador</button>
  `;

    const listaContactos = document.getElementById("listaContactos");
    const btnAgregar = document.getElementById("btnAgregarContacto");
    const inputContacto = document.getElementById("nuevoContacto");
    const token = localStorage.getItem("token");
    let config = { titulo: "", descripcion: "", direccion: "", cuit: "", email: "", contactos: [] };

    // ==============================
    // Obtener datos desde el backend
    // ==============================
    async function cargarConfigDesdeBackend() {
      try {
        const res = await fetch(`${API_URL}/emprendimiento/obtener`, {
          method: 'GET',
          headers: { "Content-Type": "application/json" }
        });

        if (res.status === 401) {
          alert("La sesión ha expirado. Inicia sesión nuevamente.");
          localStorage.clear();
          window.location.href = "/Frontend/modules/login.html";
          return null;
        }

        if (!res.ok) throw new Error("Error al obtener configuración");
        config = await res.json();
        renderConfig();
      } catch (err) {
        console.warn("Usando configuración local o vacía:", err);
        config = JSON.parse(localStorage.getItem("configEmprendimiento")) || config;
        renderConfig();
      }
    }

    // ==============================
    // Renderizar datos cargados
    // ============================
    function renderConfig() {
      document.getElementById("titulo").value = config.titulo || "";
      document.getElementById("descripcion").value = config.descripcion || "";
      document.getElementById("direccion").value = config.direccion || "";
      document.getElementById("cuit").value = config.cuit || "";
      document.getElementById("email").value = config.email || "";
      renderContactos();
    }

    // =======================
    // Renderizar contactos
    // =============================
    function renderContactos() {
      listaContactos.innerHTML = "";

      config.contactos.forEach((c, i) => {
        const li = document.createElement("li");
        li.innerHTML = `
        ${c.descripcion}
        <button type="button" class="btn-eliminar-contacto" data-id="${c.idContacto}" data-index="${i}">x</button>
        `;
        listaContactos.appendChild(li);
      });

      // Eliminar un contacto
      document.querySelectorAll(".btn-eliminar-contacto").forEach(btn => {
        btn.addEventListener("click", async e => {
          e.preventDefault();
          e.stopPropagation();
          const index = e.target.dataset.index;
          const id = e.target.dataset.id;
          try {
            const resp = await fetch(`${API_URL}/admin/contacto/delete/${id}`, {
              method: 'DELETE',
              headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
              }
            });

            if (res.status === 401) {
              alert("La sesión ha expirado. Inicia sesión nuevamente.");
              localStorage.clear();
              window.location.href = "/Frontend/modules/login.html";
              return null;
            }

            if (!resp.ok) throw new Error("Error al eliminar el contacto.");

          } catch (err) {
            console.warn("Error al intentar eliminar el contacto")
          }

          config.contactos.splice(index, 1);
          renderContactos();
        });
      });


      const footer = document.getElementById("contacto-container");
      if (footer) {
        footer.innerHTML = "";
        config.contactos.forEach(c => {
          const texto = typeof c === "string" ? c : c.descripcion;
          const label = document.createElement("label");
          label.classList.add("contacto");
          label.textContent = texto;
          footer.appendChild(label);
        });
      }
    }

    // =============================
    // Agregar nuevo contacto
    // ========================
    btnAgregar.addEventListener("click", async () => {
      const valor = inputContacto.value.trim();
      if (valor) {
        config.contactos.push(valor);

        try {
          const resp = await fetch(`${API_URL}/admin/contacto/new`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({ contacto: valor })
          });

          if (resp.status === 401) {
            alert("La sesión ha expirado. Inicia sesión nuevamente.");
            localStorage.clear();
            window.location.href = "/Frontend/modules/login.html";
            return null;
          }

          if (!resp.ok) throw new Error("Error al guardar el nuevo contacto");
          alert("Se completo la carga");

          const text = await resp.text();
          alert(text.message);
        } catch (error) {
          console.warn(error);
        }

        inputContacto.value = "";
        renderContactos();
      }
    });

    document.getElementById("addAdminBtn").addEventListener("click", () => {
      window.location.href = `/Frontend/modules/register.html?id=${config.idEmprendimiento}`;
    });


    // =====================
    // Guardar cambios
    // =======================
    document.getElementById("configForm").addEventListener("submit", async e => {
      e.preventDefault();
      config.titulo = document.getElementById("titulo").value;
      config.descripcion = document.getElementById("descripcion").value;
      config.direccion = document.getElementById("direccion").value;
      config.cuit = document.getElementById("cuit").value;
      config.email = document.getElementById("email").value;
      const bodyReq = {
        titulo: config.titulo,
        descripcion: config.descripcion,
        direccion: config.direccion,
        cuit: config.cuit,
        email: config.email
      };
      try {
        const res = await fetch(`${API_URL}/emprendimiento/info/mod`, {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify(bodyReq)
        });

        if (res.status === 401) {
          alert("La sesión ha expirado. Inicia sesión nuevamente.");
          localStorage.clear();
          window.location.href = "/Frontend/modules/login.html";
          return null;
        }


        if (!res.ok) throw new Error("Error al guardar en el backend");
        alert("Configuracion guardada correctamente");

        // Guarda copia local
        localStorage.setItem("configEmprendimiento", JSON.stringify(config));

        // Actualiza el header
        const headerTitle = document.querySelector(".header h1");
        if (headerTitle) headerTitle.textContent = config.titulo;
        renderContactos();

      } catch (err) {
        console.error(err);
        alert("Error al guardar en el servidor. Cambios solo en local.");
        localStorage.setItem("configEmprendimiento", JSON.stringify(config));
      }
    });

    cargarConfigDesdeBackend();
  }

});