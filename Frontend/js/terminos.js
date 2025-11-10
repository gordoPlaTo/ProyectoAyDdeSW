
document.addEventListener("DOMContentLoaded", async () => {
    const obj = document.querySelector(".objeto");
    const tituloEmp = document.querySelector(".tituloEmp");
    try{
        const res = await fetch(`http://localhost:8080/api/emprendimiento/obtener`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json"
        }
      });

      if (!res.ok) {
        const text = await res.text();
        alert(text);
        return;
      }

      const info = await res.json();

      tituloEmp.textContent = info.titulo;
      obj.innerHTML = `
      Mediante estos Términos de Uso, <strong>${info.titulo ?? "la Empresa"}</strong> (la “Empresa”),
      con domicilio legal en ${info.direccion ?? "(dirección no especificada)"} y CUIT ${info.cuit ?? "(sin CUIT)"},
      regula el acceso y uso de su plataforma de comercio electrónico (el “Sitio”),
      a través de la cual ofrece productos (y únicamente la Empresa vende dichos productos)
      y presta servicios accesorios al comercio electrónico, quedando el Usuario
      (cliente que accede al Sitio) obligado a respetar los presentes términos.
    `;

    }catch(error){

    }


})