package com.proyecto.ecommerce.config.Component;

import com.proyecto.ecommerce.model.EstadoPedido;
import com.proyecto.ecommerce.repository.IEstadoPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

@Configuration
@Order(4)
public class EstadoPedidoInitializr implements CommandLineRunner {
    @Autowired
    private IEstadoPedidoRepository estadoPedidoR;

    @Override
    public void run(String... args) throws Exception {

        if (estadoPedidoR.count() == 0){
            List<EstadoPedido> listEstados =  Arrays.asList(
                    new EstadoPedido("Espera de Pago"),
                    new EstadoPedido("Completado"),
                    new EstadoPedido("Cancelado")
            );

            listEstados.forEach(estado -> {
                estadoPedidoR.save(estado);
            });
        }

    }
}
