package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.ProductoReqDTO;
import com.proyecto.ecommerce.dto.ProductoRespDTO;
import com.proyecto.ecommerce.model.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {
    //Crear Producto
    Producto crearProducto(ProductoReqDTO prod);

    //Obtener Producto
    Producto obtenerProductoById (Long id);

    //Obtener Productos con stock y habilitados
    List<ProductoRespDTO> obtenerProductosActivos();

    //Obtener Productos
    List<Producto> obtenerProductos();


    //Modificar Producto (nombre, descripcion, precio)

    void modificarProducto(Long id, ProductoReqDTO prod);

    //Reducir Stock
    void reducirStock(Long id, int stock);

    //Aumentar Stock
    void aumentarStock(Long id, int stock);

    //Habilitar o Deshabilitar Producto
    void habDesProducto (Long id);
}
