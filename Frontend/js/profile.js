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
    })

}

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
                    oldPassword: oldPass,
                    newPassword: newPass,
                }),
            });

            if (!res.ok) {
                const errorMsg = await res.text();
                throw new Error(errorMsg || "Error al cambiar contraseña");
            }

            alert("Contraseña actualizada correctamente.");
            modalPass.style.display = "none";
            formPass.reset();
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

        } catch (err) {
            console.error("Error al actualizar dirección:", err);
            alert("No se pudo actualizar la dirección. Verifique los datos o su sesión.");
        }
    });

});
