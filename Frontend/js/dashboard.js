document.querySelectorAll('.menu-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    const section = btn.dataset.section;
    loadSection(section);
  });
});

function loadSection(section) {
  const main = document.getElementById('mainContent');

  switch (section) {
    case 'productos':
      main.innerHTML = `
        <h2>Cargar Producto</h2>
        <form id="formProducto">
          <label>Nombre:</label>
          <input type="text" required><br>
          <label>Precio:</label>
          <input type="number" step="0.01" required><br>
          <button type="submit">Guardar</button>
        </form>
      `;
      break;

    case 'ver-productos':
      main.innerHTML = `
        <h2>Mis Productos</h2>
        <p>Aquí se listarán los productos cargados.</p>
      `;
      break;

    case 'ventas':
      main.innerHTML = `
        <h2>Historial de Ventas</h2>
        <p>Listado de ventas realizadas.</p>
      `;
      break;

    case 'configuracion':
      main.innerHTML = `
        <h2>Configuración del Emprendimiento</h2>
        <p>Ajustes del perfil, contraseña, etc.</p>
      `;
      break;

    default:
      main.innerHTML = `<h2>Seleccioná una opción del menú</h2>`;
  }
}
