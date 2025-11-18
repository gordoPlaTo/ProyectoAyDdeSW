function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
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


// --- función de redirección inteligente ---
async function smartRedirectToIndex() {
    const origin = window.location.origin;

    const candidates = [
        new URL('../index.html', window.location.href).href,
        `${origin}/Frontend/index.html`,
        `${origin}/index.html`
    ];

    for (const url of candidates) {
        try {
            const resp = await fetch(url, { method: 'HEAD' });
            if (resp.ok) {
                window.location.href = url;
                return;
            }
        } catch (err) {
            // Ignoramos y probamos el siguiente
        }
    }

    // Si nada funcionó, intenta al menos ../index.html
    window.location.href = new URL('../index.html', window.location.href).href;
}


const API_URL = "http://localhost:8080/api/auth";
const BASE_PATH = "/Frontend"; // carpeta raíz de tu proyecto


document.addEventListener("DOMContentLoaded", () => {

    const registerForm = document.getElementById("registerForm");
    const loginForm = document.getElementById("loginForm");

    if (registerForm) {

        // STEP 4: Obtener id del emprendimiento desde la URL
        const params = new URLSearchParams(window.location.search);
        const idEmprendimiento = params.get("id");


        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirmPassword").value;

            if (password !== confirmPassword) {
                alert("Las contraseñas no coinciden!");
                return;
            }
            const ATerms = document.getElementById("terms");
            const data = {
                nombre: document.getElementById("name").value,
                apellido: document.getElementById("surname").value,
                dni: document.getElementById("dni").value,
                fechaNac: document.getElementById("date").value,
                direccion: document.getElementById("direction").value,
                email: document.getElementById("email").value,
                password: password,
                acceptTerms: ATerms.checked,
                idEmprendimiento: idEmprendimiento

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
                    const errorHttp = await res.text();
                    console.error(errorHttp);

                }
            } catch (error) {
                console.error("Error al conectar con el servidor:", error);
                alert("Error al conectar con el servidor.");
            }

        });
    }

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
                    const errorHttp = await res.text();
                    console.log(errorHttp);
                    return;
                }

                const result = await res.json();
                const token = result.token;

                localStorage.setItem("email", result.email);
                localStorage.setItem("message", result.message)
                localStorage.setItem("token", result.token);

                const payload = JSON.parse(atob(token.split(".")[1]));
                const authorities = payload.authorities;

                console.log("Token payload:", payload);
                console.log("Authorities:", authorities);

                if (authorities && authorities.includes("ADMIN")) {
                    window.location.href = "../modules/dashboard.html";
                } else {
                    await smartRedirectToIndex();
                }

            } catch (err) {
                console.error("Error de conexión:", err);
                alert("Error al conectarse con el servidor");
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