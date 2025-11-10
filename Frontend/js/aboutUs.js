const fadeElements = document.querySelectorAll('.fade-in');

function handleScroll() {
  const triggerBottom = window.innerHeight * 0.85;
  fadeElements.forEach(el => {
    const boxTop = el.getBoundingClientRect().top;
    if (boxTop < triggerBottom) {
      el.classList.add('visible');
    }
  });
}

window.addEventListener('scroll', handleScroll);
window.addEventListener('load', handleScroll);

window.addEventListener('scroll', () => {
  const header = document.querySelector('.header');
  const scrollPos = window.scrollY;
  if (scrollPos > 50) {
    header.style.background = 'linear-gradient(135deg, #002e6b, #005fcc)';
  } else {
    header.style.background = 'linear-gradient(135deg, #004aad, #0077ff)';
  }
});

document.addEventListener("DOMContentLoaded", async () => {
  try {
    const res = await fetch("http://localhost:8080/api/emprendimiento/obtener");
    if (!res.ok) throw new Error("Error al obtener datos del emprendimiento");

    const data = await res.json();

    document.querySelector("header h1").textContent = data.titulo || "Nuestro Emprendimiento";

    const aboutText = document.querySelector(".about-text p");
    if (aboutText && data.descripcion) {
      aboutText.textContent = data.descripcion;
    }

    if (data.direccion) {
      const direccionEl = document.createElement("p");
      direccionEl.innerHTML = `<strong>Dirección:</strong> ${data.direccion}`;
      document.querySelector(".about-text").appendChild(direccionEl);
    }

    const footer = document.querySelector(".footer p");
    if (footer && data.contactos?.length) {
      footer.innerHTML = `
        &copy; 2025 ${data.titulo}. Todos los derechos reservados.<br>
        ${data.contactos.map(c => `<span>${c.descripcion}</span>`).join(" • ")}
      `;
    } else {
      footer.innerHTML = `&copy; 2025 ${data.titulo || "Mi Tienda"}. Todos los derechos reservados.`;
    }

  } catch (error) {
    console.error("Error al cargar información del emprendimiento:", error);
  }
});

