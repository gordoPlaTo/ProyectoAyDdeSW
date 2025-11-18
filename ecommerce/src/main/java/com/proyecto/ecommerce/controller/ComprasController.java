package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.PedidosDTO.ComprobanteReqDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidoCompletarDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidoCrearReqDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidosClienteDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoVentaDTO;
import com.proyecto.ecommerce.dto.RespDTO;
import com.proyecto.ecommerce.model.Pedido;
import com.proyecto.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {

    //GenerarCompra
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/pedido/cliente/crear")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<RespDTO> crearPedido(@Valid @RequestBody PedidoCrearReqDTO pedidoDTO){
        pedidoService.crearPedido(pedidoDTO);
        RespDTO response = new RespDTO(
                "Se cargó el pedido correctamente.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //CancelarCompra
    @PatchMapping("/pedido/cliente/cancelar/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<RespDTO> cancelarPedido (@NotNull(message = "El id del pedido es obligatorio")
                                                      @Positive(message = "El id debe ser un número positivo")
                                                      @PathVariable Long id){
        pedidoService.cancelarPedido(id);
        RespDTO response = new RespDTO(
                "Se eliminó correctamente el pedido.",
                true,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //Solicitar Pedidos del usuario
    @GetMapping("pedido/cliente/obtener")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<PedidosClienteDTO>> obtenerPedidosCliente(){
        return ResponseEntity.ok(pedidoService.obtenerPedidoByEmail());
    }

    //Subir Comprobante (cliente)
    @PatchMapping("/pedido/comprobante/cargar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<RespDTO> cargarComprobante(@Valid @ModelAttribute ComprobanteReqDTO compDTO){

        if (compDTO.comprobante().getSize() > 5_000_000){//asi se indica el tamaño aca 5mb maximo
            throw new IllegalArgumentException("El tamaño de la imagen excede las 5mb permitidos");
        }
        String tipoArchivo = compDTO.comprobante().getContentType();
        if (tipoArchivo == null || !tipoArchivo.startsWith("image/")){
            //aca validamos el tipo de archivo para que sea una imagen
            throw  new IllegalArgumentException("El archivo ingresado debe ser una imagen valida");
        }

        pedidoService.adjuntarComprobante(compDTO);

        RespDTO response = new RespDTO(
                "El comprobante se cargó correctamente.",
                true,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //Marcar como completo el pedido
    @PatchMapping("/completarPedido")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> completarPedido(@Valid @RequestBody PedidoCompletarDTO completarDTO){
        pedidoService.completarPedido(completarDTO);

        RespDTO response = new RespDTO(
                "El pedido se completó con éxito.",
                true,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //Endpoinsts para:

    //Acceder a estadisticas (Ventas, Producto mas comprado)
    //Obtener Listado de Pedidos Completados

    @GetMapping("/pedido/ventasRealizadas")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductoVentaDTO> obtenerVentas(){
        return pedidoService.ventasRealizadas();
    }

    //Obtener Listado de Pedidos
    @GetMapping("/pedido/obtenerPedidos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Pedido> obtenerPedidos(){
        return pedidoService.obtenerPedidos();
    }




    @GetMapping("/pedido/enTramite")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidosClienteDTO>> obtenerPedidosEnTramite(){
        return ResponseEntity.ok(pedidoService.obtenerPedidosEnTramite());
    }




}
