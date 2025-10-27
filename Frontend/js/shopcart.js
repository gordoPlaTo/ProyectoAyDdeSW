document.addEventListener("DOMContentLoaded", () => {
  const carritoBody = document.getElementById("carrito-body");
  const totalElement = document.getElementById("total");

  let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

  function renderCarrito() {
    carritoBody.innerHTML = "";
    let total = 0;

    carrito.forEach((item, index) => {
      const fila = document.createElement("tr");
      fila.innerHTML = `
        <td><button class="btn-eliminar" data-index="${index}">x</button></td>
        <td>${item.nombre}</td>
        <td>${item.cantidad}</td>
        <td>$${item.precio.toFixed(2)}</td>
      `;
      carritoBody.appendChild(fila);
      total += item.precio * item.cantidad;
    });

    totalElement.textContent = total.toFixed(2);

    document.querySelectorAll(".btn-eliminar").forEach(btn => {
      btn.addEventListener("click", (e) => {
        const i = e.target.dataset.index;
        carrito.splice(i, 1);
        localStorage.setItem("carrito", JSON.stringify(carrito));
        renderCarrito();
      });
    });
  }

  renderCarrito();

  document.getElementById("btnComprar").addEventListener("click", () => {
    alert("Compra realizada correctamente (simulada)");
    localStorage.removeItem("carrito");
    carrito = [];
    renderCarrito();
  });
});
