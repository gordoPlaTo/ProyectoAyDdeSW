const uploadPhoto = document.getElementById("uploadPic");
const userPhoto = document.getElementById("userPic");

if (uploadPhoto && userPhoto) {
  uploadPhoto.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (file && file.type.startsWith("image/")) {
      const reader = new FileReader();
      reader.onload = (event) => {
        userPhoto.src = event.target.result;
      };
      reader.readAsDataURL(file);
    } else {
      alert("Por favor, selecciona un formato de imagen válido.");
    }
  });
}

document.addEventListener("DOMContentLoaded", async () => {
  const API_URL = "http://localhost:8080/api";
  const token = localStorage.getItem("token");

  const nombreElem = document.querySelector(".profInfo h2");
  const emailElem = document.querySelector(".profInfo p:nth-of-type(1)");

  if (!token) {
    alert("Debe iniciar sesión para acceder al perfil de administrador");
    window.location.href = "../modules/login.html";
    return;
  }

  try {
    const res = await fetch(`${API_URL}/user/obtenerDatos`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (res.status === 401) {
            alert("La sesión ha expirado. Inicia sesión nuevamente.");
            localStorage.clear();
            window.location.href = "/Frontend/modules/login.html";
            return null;
          }

    if (!res.ok) {
      console.error("Error al obtener datos del administrador:", res.status);
      alert("No se pudieron cargar los datos del perfil.");
      return;
    }

    const adminData = await res.json();

    nombreElem.textContent = `${adminData.nombre || ""} ${adminData.apellido || ""}`;
    emailElem.textContent = `Email: ${adminData.email || "No disponible"}`;
  } catch (err) {
    console.error("Error de conexión:", err);
    alert("Error al conectar con el servidor");
  }
});

document.addEventListener("DOMContentLoaded", () => {
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
