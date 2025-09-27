// tema oscuro/claro
const themeToggle = document.getElementById("themeToggle");

if (themeToggle) {
    themeToggle.addEventListener("change", () => {
        document.body.classList.toggle("darkMode", themeToggle.checked);
    
        if (themeToggle.checked) {
            localStorage.setItem("theme", "dark");
        } else {
            localStorage.setItem("theme", "light");
        }
    });

    window.addEventListener("DOMContentLoaded", () => {
        const savedTheme = localStorage.getItem("theme");
        if (savedTheme === "dark") {
            document.body.classList.add("darkMode");
            themeToggle.checked = true;
        }    
    });
}

//cambio foto perfil
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