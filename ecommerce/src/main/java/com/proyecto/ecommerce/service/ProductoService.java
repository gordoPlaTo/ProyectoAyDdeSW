package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.ProductoReqDTO;
import com.proyecto.ecommerce.dto.ProductoRespDTO;
import com.proyecto.ecommerce.model.IVA;
import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.repository.IIvaRepository;
import com.proyecto.ecommerce.repository.IProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService implements IProductoService {
    @Autowired
    private IProductoRepository productoRepository;
    @Autowired
    private IIvaRepository ivaRepository;

    @Override
    public Producto crearProducto(ProductoReqDTO prodDto) {
        Producto prod = new Producto();
        prod.setNombre(prodDto.nombre().toLowerCase());
        prod.setDescripcion(prodDto.descripcion().toLowerCase());
        prod.setPrecio(prodDto.precio());

        IVA iva = ivaRepository.findById(prodDto.idIva())
                        .orElseThrow(() -> new EntityNotFoundException("No se encontro una categoria de IVA con el id " + prodDto.idIva()));

        prod.setIva(iva);
        prod.setEnable(true);

        return productoRepository.save(prod);
    }

    @Override
    public void aumentarStock(Long id, int stock) {
        Producto prod =this.obtenerProductoById(id);
        prod.setStock(prod.getStock() + stock);
        productoRepository.save(prod);
    }

    @Override
    public void habDesProducto(Long id) {
        Producto prod =this.obtenerProductoById(id);
        prod.setEnable(!prod.isEnable());
    }

    @Override
    public void reducirStock(Long id, int stock) {
        Producto prod =this.obtenerProductoById(id);
        if (prod.getStock() < stock){
            throw new IllegalArgumentException ("No puedes restar mas stock del que realmente dispones en tu inventario.");
        }
        prod.setStock(prod.getStock() - stock);
        productoRepository.save(prod);
    }

    @Override
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    @Override
    public List<ProductoRespDTO> obtenerProductosActivos() {
        //Aca implementar que devuelva los que tienen stock > 1

        return productoRepository.findAll().stream()
                .filter(Producto::isEnable)
                .filter(p -> p.getStock()>0)
                .map(producto -> new ProductoRespDTO(
                        producto.getNombre(),
                        producto.getDescripcion(),
                        producto.getPrecio(),
                        producto.getIva()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Producto obtenerProductoById(Long id) {
        return productoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Algo sucedio mal y no se encontro el producto con id " + id ));
    }

    @Override
    public void modificarProducto(Long id, ProductoReqDTO prod) {

    }
}
