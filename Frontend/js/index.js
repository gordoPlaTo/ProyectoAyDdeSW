function toggleSidebar() {
  document.querySelector('.sidebar').classList.toggle('open');
  document.querySelector('.overlay').classList.toggle('active');
}

const productosContainer = document.querySelector(".product-container");

document.addEventListener("DOMContentLoaded", () => {
  const urlperfil = localStorage.getItem("urlPerfil");

  // =================
  // Obtener Productos
  // ===================
  async function loadProductList() {
    // muestra carga mientras viene la respuesta
    productosContainer.innerHTML = `
    <h2>Mis productos</h2>
    <div id="productosContainer" class="productos-container">
      <p>Cargando productos...</p>
    </div>
  `;

    try {

      const res = await fetch(`http://localhost:8080/api/emprendimiento/productos/obtenerActivos`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json"
        }
      });

      if (!res.ok) {
        const text = await res.text().catch(() => "");
        console.error("Error al obtener:", res.status, text);
        productosContainer.innerHTML = `<p style="color:red;">Error al obtener productos (status ${res.status})</p>`;
        return;
      }

      const productos = await res.json();

      if (!productos || productos.length === 0) {
        productosContainer.innerHTML = "<p>No hay productos cargados aún.</p>";
        productosContainer.innerHTML = `
          <div class="product-card-vac">
            <div class="text-vac1"></div>
            <div class="img-vac"></div>
            <div class="text-vac2"></div>
            <div class="text-vac3"></div>
            <div class="btn-vac"></div>
          </div>`;
    
        return;
      }

      const formatPrice = (p) => {
        if (p === null || p === undefined) return "N/A";
        const n = typeof p === "string" ? parseFloat(p) : p;
        return isNaN(n) ? "N/A" : n.toFixed(2);
      };

      productosContainer.innerHTML = productos.map(prod => `
      <div class="product-card">
        <h3>${escapeHtml(prod.nombre)}</h3>
        <img src=${prod.url ? escapeHtml(prod.url ?? String(prod.url)) : 'N/A'}>
        <p class="descripcion">${escapeHtml(prod.descripcion)}</p>
        <p><strong>Precio:</strong> $${formatPrice(prod.precio)}</p>
        <button class="btnAgregar"
            data-id="${prod.idProducto}"
            data-nombre="${escapeHtml(prod.nombre)}"
            data-precio="${prod.precio}"
        >Añadir al Carrito</button>
      </div>
    `).join("");

    // ... dentro de loadProductList, después de: productosContainer.innerHTML = productos.map(...)

document.querySelectorAll(".btnAgregar").forEach(btn => { 
    // CORRECCIÓN 1: No necesitas agregar una nueva validación aquí, la que tienes
    // en el evento 'click' ya funciona.

    btn.addEventListener("click", () => {
        const id = btn.dataset.id
        const nombre = btn.dataset.nombre;
        const precio = parseFloat(btn.dataset.precio);
        const token = localStorage.getItem("token"); // Correcto: Trae el token
        
        // CORRECCIÓN 2: Asegúrate de que el ID del carrito sea un String para ser consistente
        // ya que el dataset siempre devuelve strings.
        const idString = String(id); 

        let carrito = JSON.parse(localStorage.getItem("carrito")) || [];


        // VALIDACIÓN DE TOKEN (Correcto)
        if (!token) {
            alert("Debes iniciar sesion para realizar una compra.");
            window.location.href = "/Frontend/modules/login.html";
            return;
        }

        // Se busca si ya está ese producto en el carrito
        // CORRECCIÓN 3: La búsqueda debe ser por ID, no por nombre, para evitar duplicados
        // si dos productos tienen el mismo nombre pero diferente ID.
        const productoExistente = carrito.find(p => String(p.id) === idString); 

        if (productoExistente) {
            productoExistente.cantidad += 1;
        } else {
            carrito.push({
                id: idString, // Usar el ID como string
                nombre,
                precio,
                cantidad: 1
            });
        }

        localStorage.setItem("carrito", JSON.stringify(carrito));

        alert(`${nombre} se agregó al carrito`);
    });
});
// ... fin de loadProductList

    } catch (error) {
      console.error("Error al obtener productos:", error);
      productosContainer.innerHTML = `
      <div class="product-card-vac">
        <div class="text-vac1"></div>
        <div class="img-vac"></div>
        <div class="text-vac2"></div>
        <div class="text-vac3"></div>
        <div class="btn-vac"></div>
      </div>`;
    
    
    }
  }
  // ============================
  // Ordenar por precio
  // ==============================
  const ascCheckbox = document.querySelector('input[value="asc"]');
  const descCheckbox = document.querySelector('input[value="desc"]');

  ascCheckbox.addEventListener("change", () => {
    if (ascCheckbox.checked) {
      descCheckbox.checked = false;
      ordenarProductos("asc");
    } else {
      loadProductList(); // vuelve al orden original
    }
  });

  descCheckbox.addEventListener("change", () => {
    if (descCheckbox.checked) {
      ascCheckbox.checked = false;
      ordenarProductos("desc");
    } else {
      loadProductList();
    }
  });

  // =====================
  // busqueda de productos
  // =======================
  const searchInput = document.querySelector(".search-bar");

  searchInput.addEventListener("input", () => {
    const term = searchInput.value.toLowerCase().trim();
    const productos = document.querySelectorAll(".product-card");

    productos.forEach(prod => {
      const nombre = prod.querySelector("h3").textContent.toLowerCase();
      const descripcion = prod.querySelector(".descripcion").textContent.toLowerCase();

      if (nombre.includes(term) || descripcion.includes(term)) {
        prod.style.display = "block";
      } else {
        prod.style.display = "none";
      }
    });
  });


  function ordenarProductos(direccion) {
    const cards = Array.from(document.querySelectorAll(".product-card"));
    if (!cards.length) return;

    const productosOrdenados = cards.sort((a, b) => {
      const precioA = parseFloat(a.querySelector("p strong").nextSibling.textContent.replace("$", "")) || 0;
      const precioB = parseFloat(b.querySelector("p strong").nextSibling.textContent.replace("$", "")) || 0;
      return direccion === "asc" ? precioA - precioB : precioB - precioA;
    });

    const contenedor = document.querySelector(".product-container");
    contenedor.innerHTML = "";
    productosOrdenados.forEach(card => contenedor.appendChild(card));
  }



  async function loadContactos() {
    try {

      const res = await fetch(`http://localhost:8080/api/emprendimiento/contactos`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json"
        }
      });

      if (!res.ok) {
        const text = await res.text().catch(() => "");
        console.error("Error al obtener:", res.status, text);
        return;
      }

    } catch (error) {
      console.error("Error al obtener contactos:", error);
    }

  }

  async function loadInfoEmp() {
    try {
      const res = await fetch("http://localhost:8080/api/emprendimiento/obtener");
      if (res.ok) {
        const data = await res.json();
        document.getElementById("nombre-emprendimiento").textContent = data.titulo;
        document.getElementById("derechosR").textContent = `© 2025 ${data.titulo}`;

        const containerContactos = document.getElementById("contacto-container");
        containerContactos.innerHTML = "";

        if (Array.isArray(data.contactos)) {
          data.contactos.forEach(element => {
            const div = document.createElement("div");
            div.classList.add("contacto");
            div.textContent = element.descripcion;
            containerContactos.appendChild(div);
          });

        } else {
          console.warn("El emprendimiento no contiene contactos");

        }
      }
    } catch (err) {
      console.warn("No se pudo obtener configuración del emprendimiento", err);
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

  loadInfoEmp();
  loadContactos();
  loadProductList();
});

/*document.addEventListener("DOMContentLoaded", () => {
  const botonesAgregar = document.querySelectorAll(".btn-agregar");

  botonesAgregar.forEach((btn) => {
    btn.addEventListener("click", (e) => {
      const id = e.target.dataset.id;
      const nombre = e.target.dataset.nombre;
      const precio = parseFloat(e.target.dataset.precio);

      let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

      // Verifica si el producto ya está en el carrito
      const productoExistente = carrito.find((item) => item.id === id);

      if (productoExistente) {
        // Si ya existe, solo suma 1 a la cantidad
        productoExistente.cantidad += 1;
      } else {
        // Si no existe, lo agrega nuevo
        carrito.push({ id, nombre, precio, cantidad: 1 });
      }

      localStorage.setItem("carrito", JSON.stringify(carrito));

      alert(`${nombre} agregado al carrito`);
    });
  });
});
*/

