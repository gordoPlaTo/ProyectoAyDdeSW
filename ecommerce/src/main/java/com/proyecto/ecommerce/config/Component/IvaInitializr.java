package com.proyecto.ecommerce.config.Component;

import com.proyecto.ecommerce.model.IVA;
import com.proyecto.ecommerce.repository.IIvaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Order(5)
public class IvaInitializr implements CommandLineRunner {
    @Autowired
    private IIvaRepository ivaRepository;

    @Override
    public void run(String... args) throws Exception {

        if(ivaRepository.count() == 0){
            List<IVA> listIva = Arrays.asList(
                    new IVA("Exento", new BigDecimal("0.00")),
                    new IVA("No Gravado / Exportaciones", new BigDecimal("0.00")),
                    new IVA("General 21% - Productos de consumo general", new BigDecimal("21.00")),
                    new IVA("Reducido 10.5% - Alimentos básicos y medicamentos", new BigDecimal("10.50")),
                    new IVA("Incrementado 27% - Servicios y energía eléctrica", new BigDecimal("27.00")),
                    new IVA("Servicios de telecomunicaciones", new BigDecimal("27.00")),
                    new IVA("Transporte público o escolar", new BigDecimal("10.50")),
                    new IVA("Servicios de agua y cloacas", new BigDecimal("10.50")),
                    new IVA("Medicamentos y productos farmacéuticos", new BigDecimal("10.50")),
                    new IVA("Alimentos de primera necesidad", new BigDecimal("10.50")),
                    new IVA("Productos de lujo o electrónica", new BigDecimal("21.00")),
                    new IVA("Automóviles y motovehículos", new BigDecimal("21.00")),
                    new IVA("Educación privada", new BigDecimal("0.00")),
                    new IVA("Salud pública", new BigDecimal("0.00")),
                    new IVA("Servicios financieros y bancarios", new BigDecimal("21.00")),
                    new IVA("Construcción y obras públicas", new BigDecimal("10.50")),
                    new IVA("Importaciones", new BigDecimal("21.00")),
                    new IVA("Reparaciones y mantenimiento general", new BigDecimal("21.00")),
                    new IVA("Software y servicios digitales", new BigDecimal("21.00"))
            );

            listIva.forEach(iva -> {
                ivaRepository.save(iva);
            });
        }

    }
}
