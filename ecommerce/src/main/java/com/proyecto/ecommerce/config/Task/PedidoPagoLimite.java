package com.proyecto.ecommerce.config.Task;

import com.proyecto.ecommerce.model.EstadoPedido;
import com.proyecto.ecommerce.model.Pedido;
import com.proyecto.ecommerce.repository.IEstadoPedidoRepository;
import com.proyecto.ecommerce.repository.IPedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class PedidoPagoLimite {

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private IEstadoPedidoRepository estadoPedidoRepository;

    //Esta tarea se ejecutara una vez por dia
    @Scheduled(cron = "0 30 21 * * *", zone = "America/Argentina/Buenos_Aires")
    public void verificarPedido(){
        List<Pedido> listPedido = pedidoRepository.obtenerPendientesPago();

        listPedido.forEach(pedido -> {
            LocalDate fechaLimite = pedido.getFechaCreacion().plusDays(3);//tiempo limite respecto al abse
            if (LocalDate.now().isAfter(fechaLimite) && (pedido.getUrlComprobante() == null || pedido.getUrlComprobante().isBlank())){
                EstadoPedido estado = estadoPedidoRepository.findById(3L).orElseThrow(
                        () -> new RuntimeException("No se encontro el estado 'Cancelado', para pedidos fuera del limite."));
                pedido.setEstadoPedido(estado);

                log.info("El pedido de "+ pedido.getUsuario().getEmail() + " no se le ha cargado su comprobante. Se cancelara su pedido");
            }
        });
        pedidoRepository.saveAll(listPedido);
    }

}
