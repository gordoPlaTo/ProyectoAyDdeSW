// tema oscuro/claro
const themeToggle = document.getElementById("themeToggle");

window.addEventListener("DOMContentLoaded", () => {
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme === "dark") {
        document.body.classList.add("darkMode");
        if (themeToggle) themeToggle.checked = true;
    }
});

if (themeToggle) {
    themeToggle.addEventListener("change", () => {
        const isDark = themeToggle.checked;
        
        document.body.classList.toggle("darkMode", isDark);
        localStorage.setItem("theme", isDark ? "dark" : "light");
    });



}
