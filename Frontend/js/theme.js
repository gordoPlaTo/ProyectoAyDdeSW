document.addEventListener("DOMContentLoaded", () => {
  const toggle = document.getElementById("themeToggle");
  const body = document.body;

  const savedTheme = localStorage.getItem("theme");

  if (savedTheme === "dark") {
    body.classList.add("darkMode");
    if (toggle) toggle.checked = true;
  } else {
    body.classList.remove("darkMode");
    if (toggle) toggle.checked = false;
  }

  if (toggle) {
    toggle.addEventListener("change", () => {
      if (toggle.checked) {
        body.classList.add("darkMode");
        localStorage.setItem("theme", "dark");
      } else {
        body.classList.remove("darkMode");
        localStorage.setItem("theme", "light");
      }
    });
  }
});
