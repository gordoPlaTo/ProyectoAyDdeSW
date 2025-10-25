package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.ProductosDTO.ProductoActRespDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoPatchDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoReqDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoRespDTO;
import com.proyecto.ecommerce.model.Producto;

import java.util.List;

public interface IProductoService {
    //Crear Producto
    void crearProducto(ProductoReqDTO prod);

    //Obtener Producto
    Producto obtenerProductoById (Long id);

    //Obtener Productos con stock y habilitados
    List<ProductoActRespDTO> obtenerProductosActivos();

    //Obtener Productos
    List<ProductoRespDTO> obtenerProductos();


    //Modificar Producto (nombre, descripcion, precio)

    void modificarProducto(Long id, ProductoPatchDTO prod);

    //Reducir Stock
    void reducirStock(Long id, int stock);

    //Aumentar Stock
    void aumentarStock(Long id, int stock);

    //Habilitar o Deshabilitar Producto
    void habDesProducto (Long id);

    boolean elProductoExiste (Long id);
}
