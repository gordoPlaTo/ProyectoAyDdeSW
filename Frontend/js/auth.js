//decodificar token
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = docedeURIComponent(
            atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("Token inválido", e);
        return null;
    }
}



//login y //registro

const API_URL = "http://localhost:8080/...";

document.addEventListener("DOMContentLoaded", () => {
    const registerForm = document.getElementById("registerForm");
    const loginForm = document.getElementById("loginForm");

    //registro
    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirmPassword").value;

            if (password !== confirmPassword) {
                alert("Las contraseñas no coinciden!");
                return;
            }

            const data = {
                name: document.getElementById("name").value,
                surname: document.getElementById("surname").value,
                dni: document.getElementById("dni").value,
                date: document.getElementById("date").value,
                direction: document.getElementById("direction").value,
                email: document.getElementById("email").value,
                password: password

            };

            try {
                const res = await fetch(`${API_URL}/register`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data)
                });

                if (res.ok) {
                    alert("Registro exitoso. A continuación inicie sesión.");
                    window.location.href = "login.html";
                } else {
                    alert("Error en el registro!");
                }
            } catch (error) {
                console.error("Error al conectar con el servidor:", error);
                alert("Error al conectar con el servidor.");
            }

        });
    }
    
    //login
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const data = {
                email: document.getElementById("email").value,
                password: document.getElementById("password").value
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
                const token = result.token;

                //guardamos el token
                localStorage.setItem("token", token);
                
                //guardamos token y rol en localstorage
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