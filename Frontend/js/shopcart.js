document.addEventListener("DOMContentLoaded", () => {
  const carritoBody = document.getElementById("carrito-body");
  const totalElement = document.getElementById("total");

  let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

  function renderCarrito() {
    carritoBody.innerHTML = "";
    let total = 0;

    if (carrito.length === 0) {
      carritoBody.innerHTML = `
        <tr><td colspan="4" style="text-align:center; color:#666;">
          No hay productos en el carrito
        </td></tr>
      `;
      totalElement.textContent = "0.00";
      return;
    }


    carrito.forEach((item) => {
      const fila = document.createElement("tr");
      fila.innerHTML = `
        <td><button class="btn-eliminar" data-id="${item.id}">x</button></td>
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
        const id = e.target.dataset.id;
        carrito = carrito.filter(item => item.id !==id);
        localStorage.setItem("carrito", JSON.stringify(carrito));
        renderCarrito();
      });
    });
  }
  renderCarrito();

  document.getElementById("btnComprar").addEventListener("click", async() => {
    const token = localStorage.getItem("token");
    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];

    if (carrito.length === 0) {
    alert("El carrito está vacio.");
    return;
    }

    if (!token) {
      alert("Debes iniciar sesion para realizar una compra.");
      window.location.href = "/Frontend/modules/login.html";
      return;
    }

    const pedido = carrito.map(item => ({
      id: Number(item.id),
      cantidad: item.cantidad
    }));

    const request = {
      listProductos: pedido
    };


    try{
      const res = await fetch(`http://localhost:8080/api/compras/pedido/cliente/crear`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
         body: JSON.stringify(request)
      });

      if(!res.ok){
        const errorT = await res.text();
        console.error("Error al procesar el pedido", res.status, errorT);
        return;
      }
      const resp = await res.json();
      alert(resp.message);
      
      localStorage.setItem("carrito", JSON.stringify([]));
      renderCarrito();
      location.reload();
    }catch(error){
      console.warn("Error al tratar de enviar el pedido",error);
    }
      
})

});



  