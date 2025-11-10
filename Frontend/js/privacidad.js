document.addEventListener("DOMContentLoaded", async () => {
  const tituloEmp       = document.querySelector(".tituloEmp");
  const responsable  = document.querySelector(".responsable");
  const derechos     = document.querySelector(".derechos");
  const contacto     = document.querySelector(".contacto");

  try {
    const res = await fetch("http://localhost:8080/api/emprendimiento/obtener", {
      method: "GET",
      headers: { "Content-Type": "application/json" }
    });

    if (!res.ok) {
      const text = await res.text();
      alert("Error al obtener datos del emprendimiento:" + text);
      return;
    }

    const data = await res.json();
    const nombre   = data.titulo ?? "Nombre del Emprendimiento";
    const domicilio = data.direccion ?? "(domicilio no especificado)";
    const cuit     = data.cuit ?? "(CUIT no informado)";
    const email    = data.emailPrivacidad ?? data.email ?? "(correo no informado)";

    tituloEmp.textContent = `${nombre} – Protección de Datos Personales`;

    responsable.innerHTML = `
      La empresa <strong>${nombre}</strong>, con domicilio legal en ${domicilio} y CUIT ${cuit},
      es responsable del tratamiento de los datos personales que recaba y procesa a través del Sitio.
      Podrá designar un oficial de protección de datos conforme la Ley N.º 25.326.
    `;

    derechos.innerHTML = `
      El Usuario cuenta con los derechos reconocidos por la Ley 25.326: acceso, rectificación, actualización,
      supresión, confidencialidad y oposición (ARCO). Para ejercerlos, podrá dirigir su solicitud a
      <a href="mailto:${email}">${email}</a> o al domicilio legal de la Empresa, acreditando su identidad.
    `;

    contacto.innerHTML = `
      Para consultas o ejercicio de derechos, escribir a <a href="mailto:${email}">${email}</a>
      o remitir notificación al domicilio: ${domicilio}.
    `;


  } catch (error) {
    console.error("Error de conexión:", error);
  }
});