function toggleSidebar() {
    document.querySelector('.sidebar').classList.toggle('open');
    document.querySelector('.overlay').classList.toggle('active');
}

  const productosContainer = document.querySelector(".product-container");

document.addEventListener("DOMContentLoaded", () => {

   document.querySelectorAll(".btnAgregar").forEach(btn => { //Aca agregar validacion, para saber si es login o no.
    btn.addEventListener("click", () => {
      const id = btn.dataset.id
      const nombre = btn.dataset.nombre;
      const precio = parseFloat(btn.dataset.precio);

      let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

      //aca se busca si ya esta ese producto en el carrito
      const productoExistente = carrito.find(p => p.nombre === nombre);

      if (productoExistente) {
        productoExistente.cantidad += 1;
      } else {
        carrito.push({
          id,
          nombre,
          precio,
          cantidad: 1
        });
      }

      localStorage.setItem("carrito", JSON.stringify(carrito));

      alert(`${nombre} se agregó al carrito `);
    });
  });
  
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

    } catch (error) {
      console.error("Error al obtener productos:", error);
      productosContainer.innerHTML = "<p style='color:red;'>Error al cargar los productos. Revisa consola o el backend.</p>";
    }
  }

 


  async function loadContactos(){
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

  async function loadInfoEmp(){
     try {
        const res = await fetch("http://localhost:8080/api/emprendimiento/obtener");
        if (res.ok) {
          const data = await res.json();
          document.getElementById("nombre-emprendimiento").textContent = data.titulo;
          document.getElementById("derechosR").textContent = `© 2025 ${data.titulo}`;
                
          const containerContactos = document.getElementById("contacto-container");
          containerContactos.innerHTML = "";

          if(Array.isArray(data.contactos)){
            data.contactos.forEach(element => {
                const div = document.createElement("div");
                div.classList.add("contacto");
                div.textContent = element.descripcion;
                containerContactos.appendChild(div);
              });

          }else {
            console.warn("El emprendimiento no contiene contactos");
          
          }
       }
      }catch(err) {
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
