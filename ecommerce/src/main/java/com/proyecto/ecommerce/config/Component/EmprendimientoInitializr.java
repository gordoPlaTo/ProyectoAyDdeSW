package com.proyecto.ecommerce.config.Component;

import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.repository.IEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class EmprendimientoInitializr implements CommandLineRunner {
    @Autowired
    private IEmpRepository empRepository;

    @Override
    public void run(String... args) throws Exception {


        if(!empRepository.existsById(1L)){
            Emprendimiento emp = new Emprendimiento();

            emp.setIdEmprendimiento(1L);
            emp.setTitulo("Titulo por Defecto");
            emp.setDescripcion("Descripcion por Defecto");
            emp.setDireccion("Direccion por Defecto");
            emp.setCuit("00000000000");
            emp.setEmail("MiEmprendimiento@Gmail.com");
            emp.setMod(false);

            empRepository.save(emp);

        }

    }
}
