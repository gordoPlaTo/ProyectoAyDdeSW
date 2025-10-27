function toggleSidebar() {
    document.querySelector('.sidebar').classList.toggle('open');
    document.querySelector('.overlay').classList.toggle('active');
}

document.addEventListener("DOMContentLoaded", () => {
  const botonesAgregar = document.querySelectorAll(".btnAgregar");

  botonesAgregar.forEach(btn => {
    btn.addEventListener("click", () => {
      const nombre = btn.dataset.nombre;
      const precio = parseFloat(btn.dataset.precio);

      // Recuperar carrito existente o inicializarlo vacío
      let carrito = JSON.parse(localStorage.getItem("carrito")) || [];

      // Verificar si el producto ya está en el carrito
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

      // Guardar carrito actualizado
      localStorage.setItem("carrito", JSON.stringify(carrito));

      alert(`${nombre} se agregó al carrito ✅`);
    });
  });
});
