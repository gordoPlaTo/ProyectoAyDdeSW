function togglePassword(fieldID, btn) {
    const input = document.getElementById(fieldID);

    if (input.type === "password") {
        input.type = "text";
        btn.textcontent = "🙈"
    }
    else {
        input.type = "password";
        btn.textcontent = "👁️";
    }
}

//login

const API_URL = "http://localhost:8080/...";

document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const data = {
                email: document.getElementById("email").value;
                password: document.getElementById("password").value;
            };

            try {
                const res = await fetch(`${API_URL}/login`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data)
                });

                if (!res.ok) {
                    alert("Credenciales incorrectas");
                    return;
                }

                const result = await res.json();

                //guaramos token y rol en localstorage
                localStorage.setItem("email", result.email);
                localStorage.setItem("message", result.message);
                localStorage.setItem("token", result.token);

                //redirigimos dependiendo del rol
                if (result.role === "ADMIN") {
                    window.location.href = "dashboard.html";
                } else {
                    window.location.href = "index.html";
                }
            } catch (err) {
                console.error("Error de conexión", err);
                alert("Error al conectarse con el servidor")
            }

        });
    }
});