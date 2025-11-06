function toggleSidebar() {
    document.querySelector('.sidebar').classList.toggle('open');
    document.querySelector('.overlay').classList.toggle('active');
}

document.addEventListener("DOMContentLoaded", () => {
  const botonesAgregar = document.querySelectorAll(".btnAgregar");

  async function loadProductList() {
    mainContent.innerHTML = `
    <h2>Mis productos</h2>
    <div id="productosContainer" class="productos-container">
      <p>Cargando productos...</p>
    </div>


  `;

    const productosContainer = document.getElementsByClassName("product-container");

    try {
      const token = localStorage.getItem("token");
      if (!token) {
        productosContainer.innerHTML = "<p style='color:red;'>No autenticado. Inicie sesión.</p>";
        return;
      }

      const res = await fetch(`http://localhost:8080/api/emprendimiento/productos/obtenerActivos`, {
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

  botonesAgregar.forEach(btn => {
    btn.addEventListener("click", () => {
      const nombre = btn.dataset.nombre;
      const precio = parseFloat(btn.dataset.precio);

      let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

      const productoExistente = carrito.find(p => p.nombre === nombre);

      if (productoExistente) {
        productoExistente.cantidad += 1;
      } else {
        carrito.push({
          nombre,
          precio,
          cantidad: 1
        });
      }

      localStorage.setItem("carrito", JSON.stringify(carrito));

      alert(`${nombre} se agregó al carrito`);
    });
  });


});
