const uploadPhoto = document.getElementById("uploadPic");
const userPhoto = document.getElementById("userPic");

if (uploadPhoto && userPhoto) {
  uploadPhoto.addEventListener("change", async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    if (!file.type.startsWith("image/")) {
      alert("Por favor, selecciona un formato de imagen válido.");
      return;
    }

    const token = localStorage.getItem("token");
    const API_URL = "http://localhost:8080/api";

    const formData = new FormData();
    formData.append("imagenPerfil", file);

    try {
      const res = await fetch(`${API_URL}/user/modificar/imgPerfil`, {
        method: "PATCH",
        headers: { "Authorization": `Bearer ${token}` },
        body: formData
      });

      const data = await res.json();

      if (res.ok) {
        alert("Foto de perfil actualizada correctamente.");

        if (data && data.url) {
          userPhoto.src = data.url;
          localStorage.setItem("userProfilePic", data.url);
        }

      } else {
        alert("Error al actualizar la foto de perfil: " + (data.message || "Error desconocido"));
      }
    } catch (err) {
      console.error(err);
      alert("Error al conectar con el servidor.");
    }
  });

  const savedPic = localStorage.getItem("userProfilePic");
  if (savedPic) userPhoto.src = savedPic;
}


document.addEventListener("DOMContentLoaded", async () => {
  const API_URL = "http://localhost:8080/api";
  const token = localStorage.getItem("token");

  const fperfil = document.querySelector(".profPic");
  const nombreElem = document.querySelector(".profInfo h2");
  const emailElem = document.querySelector(".profInfo p:nth-of-type(1)");
  const direccionElem = document.querySelector(".profInfo p:nth-of-type(2)");
  const dniElem = document.querySelector(".profInfo p:nth-of-type(3)");

  if (!token) {
    alert("Debe iniciar sesión para ver su perfil");
    window.location.href = "../modules/login.html";
    return;
  }

  try {
    const res = await fetch(`${API_URL}/user/obtenerDatos`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (!res.ok) {
      console.error("Error al obtener datos del usuario:", res.status);
      alert("No se pudieron cargar los datos del perfil.");
      return;
    }

    const userData = await res.json();

    if(!userData.urlImg){
      fperfil.innerHTML =  `
            <img src="/Frontend/img/userdefault.jpg" alt="Foto de perfil" id="userPic">
            <label for="uploadPic" class="uploadBtn">Cambiar foto</label>
            <input type="file" id="uploadPic" accept="image/*" hidden></input>
    `;
    }else{fperfil.innerHTML  =  `
            <img src="${userData.urlImg}" alt="Foto de perfil" id="userPic">
            <label for="uploadPic" class="uploadBtn">Cambiar foto</label>
            <input type="file" id="uploadPic" accept="image/*" hidden></input>
    `;}
    
    nombreElem.textContent = `${userData.nombre} ${userData.apellido}`;
    emailElem.textContent = `Email: ${userData.email || "No disponible"}`;
    direccionElem.textContent = `Dirección: ${userData.direccion || "No registrada"}`;
    dniElem.textContent =  `DNI: ${userData.dni || "No registrado"}`;
 

  } catch (err) {
    console.error("Error de conexión:", err);
    alert("Error al conectar con el servidor");
  }
});


document.addEventListener("DOMContentLoaded", async () => {
  const API_URL = "http://localhost:8080/api";
  const token = localStorage.getItem("token");
  const pedidosContainer = document.getElementById("pedidosContainer");

  if (!token) {
    pedidosContainer.innerHTML = `<p style="color:red;">Debe iniciar sesión para ver sus pedidos.</p>`;
    return;
  }

  async function cargarPedidos() {
    try {
      const res = await fetch(`${API_URL}/compras/pedido/cliente/obtener`, {
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });

      if (!res.ok) {
        pedidosContainer.innerHTML = `<p style="color:red;">Error al cargar tus pedidos (${res.status})</p>`;
        return;
      }

      const pedidos = await res.json();

      if (!pedidos || pedidos.length === 0) {
        pedidosContainer.innerHTML = "<p>No tienes pedidos registrados.</p>";
        return;
      }

      pedidosContainer.innerHTML = pedidos.map(p => `
        <div class="pedido-card" data-id="${p.id}">
          <h3>Pedido #${p.id}</h3>
          <p><strong>Fecha:</strong> ${p.fechaCreacion}</p>
          <p><strong>Estado:</strong> <span class="estado ${p.estadoPedido.toLowerCase()}">${p.estadoPedido}</span></p>
          <p><strong>Total:</strong> $${p.totalCompra}</p>

          ${p.urlComprobante ? `<p><a href="${p.urlComprobante}" target="_blank">Ver comprobante</a></p>` : ""}

          <div class="detalle-pedido">
            <h4>Detalles:</h4>
            <ul>
              ${p.detallePedido.map(d => `
                <br>
                <li>${d.producto}</li>
                <li>Cantidad: ${d.cantidad}</li>
                <li>Neto:$${d.precioNeto}</li>
                <li>Monto IVA: $${d.montoIva}</li>
                <li>Total: $${d.precioTotal}</li>
                <br>
                <hr>
              `).join("")}
            </ul>
            ${p.estadoPedido === "Espera de Pago" ? `
            <button class="btn-comprobante" data-id="${p.id}">Adjuntar Comprobante</button>
          ` : ""}
            ${p.estadoPedido === "Espera de Pago" ? `
            <button class="btn-cancelar" data-id="${p.id}">Cancelar pedido</button>
          ` : ""}
          </div>
          
        </div>
      `).join("");

      document.querySelectorAll(".btn-cancelar").forEach(btn => {
        btn.addEventListener("click", async () => {
          const id = btn.dataset.id;
          if (!confirm("¿Seguro que deseas cancelar este pedido?")) return;

          try {
            const cancelRes = await fetch(`${API_URL}/compras/pedido/cliente/cancelar/${id}`, {
              method: "PATCH",
              headers: { "Authorization": `Bearer ${token}` }
            });

            if (cancelRes.ok) {
              alert("Pedido cancelado correctamente");
              cargarPedidos();
            } else {
              const msg = await cancelRes.text();
              alert("Error al cancelar pedido:\n" + msg);
            }
          } catch (err) {
            console.error(err);
            alert("Error al conectar con el servidor");
          }
        });
      });

document.querySelectorAll(".btn-comprobante").forEach(btn => {
  btn.addEventListener("click", () => {
    const id = btn.dataset.id;

    const modal = document.createElement("div");
    modal.classList.add("modal");
    modal.style.display = "flex";
    modal.innerHTML = `
      <div class="modal-content">
        <span class="close">&times;</span>
        <h3>Subir comprobante de pago</h3>
        <form id="formComprobante">
          <input type="file" id="imageComprobante" name="comprobante" accept="image/*" required>
          <button type="submit">Enviar</button>
        </form>
      </div>
    `;

    document.body.appendChild(modal);

    modal.querySelector(".close").addEventListener("click", () => modal.remove());
    window.addEventListener("click", e => { if (e.target === modal) modal.remove(); });

    const form = modal.querySelector("#formComprobante");
    form.addEventListener("submit", async (e) => {
      e.preventDefault();

      const file = document.getElementById("imageComprobante").files[0];
      if (!file) {
        alert("Debe seleccionar una imagen.");
        return;
      }
      if (!file.type.startsWith("image/")) {
        alert("El archivo debe ser una imagen válida.");
        return;
      }
      if (file.size > 5_000_000) {
        alert("La imagen no puede superar los 5 MB.");
        return;
      }

      const formData = new FormData();
      formData.append("idPedido", id);
      formData.append("comprobante", file);

      try {
        const res = await fetch(`${API_URL}/compras/pedido/comprobante/cargar`, {
          method: "PATCH",
          headers: { "Authorization": `Bearer ${token}` },
          body: formData
        });

        if (res.ok) {
          const data = await res.json();
          alert(data.mensaje || "Comprobante cargado correctamente.");
          modal.remove();
          cargarPedidos();
        } else {
          const msg = await res.text();
          alert("Error al subir comprobante:\n" + msg);
        }
      } catch (err) {
        console.error(err);
        alert("Error al conectar con el servidor.");
      }
    });
  });
});


    } catch (error) {
      console.error(error);
      pedidosContainer.innerHTML = "<p>Error al conectar con el servidor.</p>";
    }
  }

  cargarPedidos();
});


document.addEventListener("DOMContentLoaded", () => {
  const API_URL = "http://localhost:8080/api/user";

  const modalPass = document.getElementById("modalPass");
  const modalDesactivar = document.getElementById("modalDesactivar");
  const btnCambiarPass = document.querySelector(".changePassBtn");
  const btnDesactivarCuenta = document.querySelector(".desactivateBtn");
  const cerrarBtns = document.querySelectorAll(".close, .cancelar");

  btnCambiarPass.addEventListener("click", () => (modalPass.style.display = "flex"));
  btnDesactivarCuenta.addEventListener("click", () => (modalDesactivar.style.display = "flex"));

  cerrarBtns.forEach((btn) => {
    btn.addEventListener("click", () => {
      const target = btn.dataset.close;
      document.getElementById(target).style.display = "none";
    });
  });

  window.addEventListener("click", (e) => {
    if (e.target.classList.contains("modal")) e.target.style.display = "none";
  });

  const formPass = document.getElementById("formPass");
  formPass.addEventListener("submit", async (e) => {
    e.preventDefault();

    const oldPass = document.getElementById("oldPass").value;
    const newPass = document.getElementById("newPass").value;
    const token = localStorage.getItem("token");

    try {
      const res = await fetch(`${API_URL}/modificar/password`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify({
          passwordOld: oldPass,
          passwordNew: newPass
        }),
      });

      if (!res.ok) {
        const errorMsg = await res.text();
        throw new Error(errorMsg || "Error al cambiar contraseña");
      }

      alert("Contraseña actualizada correctamente.");
      modalPass.style.display = "none";
      formPass.reset();

      location.reload();

    } catch (err) {
      console.error(err);
      alert("No se pudo cambiar la contraseña.");
    }
  });

  const btnConfirmarDesactivar = document.getElementById("btnConfirmarDesactivar");
  btnConfirmarDesactivar.addEventListener("click", async () => {
    const token = localStorage.getItem("token");

    if (!confirm("¿Seguro que querés desactivar tu cuenta?")) return;

    try {
      const res = await fetch(`${API_URL}/modificar/deshabilitarCuenta`, {
        method: "PATCH",
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!res.ok) {
        const errorMsg = await res.text();
        throw new Error(errorMsg || "Error al desactivar cuenta");
      }

      alert("Cuenta desactivada correctamente.");
      localStorage.clear();
      window.location.href = "../index.html";
    } catch (err) {
      console.error(err);
      alert("No se pudo desactivar la cuenta.");
    }
  });

  const changeDirBtn = document.querySelector(".changeDirBtn");
  const changeDirModal = document.getElementById("changeDirModal");
  const closeDirModal = changeDirModal.querySelector(".close");
  const changeDirForm = document.getElementById("changeDirForm");

  changeDirBtn.addEventListener("click", () => {
    changeDirModal.style.display = "flex";
  });

  closeDirModal.addEventListener("click", () => {
    changeDirModal.style.display = "none";
  });

  window.addEventListener("click", (e) => {
    if (e.target === changeDirModal) {
      changeDirModal.style.display = "none";
    }
  });

  changeDirForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const newAddress = document.getElementById("newAddress").value.trim();
    if (!newAddress) {
      alert("Por favor ingrese una dirección válida");
      return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
      alert("No se encontró sesión activa. Inicie sesión nuevamente.");
      window.location.href = "../modules/login.html";
      return;
    }

    try {
      const res = await fetch(`${API_URL}/modificar/direccion`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ direccion: newAddress })
      });

      if (!res.ok) {
        const errorMsg = await res.text();
        throw new Error(errorMsg);
      }

      alert("Dirección actualizada correctamente");
      changeDirModal.style.display = "none";
      location.reload();

    } catch (err) {
      console.error("Error al actualizar dirección:", err);
      alert("No se pudo actualizar la dirección. Verifique los datos o su sesión.");
    }
  });


  const logoutBtn = document.getElementById("logoutBtn");

  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      if (confirm("¿Seguro que deseas cerrar sesión?")) {
        localStorage.clear();
        alert("Sesión cerrada correctamente");
        window.location.href = "../index.html";
      }
    });
  }


});
