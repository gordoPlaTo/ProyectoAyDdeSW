// Animación de aparición al hacer scroll
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

// Efecto de "suavizado" del color del header al hacer scroll
window.addEventListener('scroll', () => {
  const header = document.querySelector('.header');
  const scrollPos = window.scrollY;
  if (scrollPos > 50) {
    header.style.background = 'linear-gradient(135deg, #002e6b, #005fcc)';
  } else {
    header.style.background = 'linear-gradient(135deg, #004aad, #0077ff)';
  }
});
