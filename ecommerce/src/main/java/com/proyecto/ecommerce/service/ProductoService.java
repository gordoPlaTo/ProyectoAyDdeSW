package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.ProductosDTO.ProductoActRespDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoPatchDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoReqDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoRespDTO;
import com.proyecto.ecommerce.model.IVA;
import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.repository.IIvaRepository;
import com.proyecto.ecommerce.repository.IProductoRepository;
import com.proyecto.ecommerce.utils.ConvText;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService implements IProductoService {
    @Autowired
    private IProductoRepository productoRepository;
    @Autowired
    private IIvaRepository ivaRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public void crearProducto(ProductoReqDTO prodDto) {
        Producto prod = new Producto();
        prod.setNombre(prodDto.nombre().toLowerCase());
        prod.setDescripcion(prodDto.descripcion().toLowerCase());
        prod.setStock(prodDto.stock());
        prod.setPrecio(prodDto.precio());

        IVA iva = ivaRepository.findById(prodDto.idIva())
                        .orElseThrow(() -> new EntityNotFoundException("No se encontro una categoria de IVA con el id " + prodDto.idIva()));

        if (prodDto.imgProducto() == null || prodDto.imgProducto().isEmpty()) {
            throw new IllegalArgumentException("Debe subir una imagen para el producto.");
        }

        prod.setUrl(cloudinaryService.subirImagen(prodDto.imgProducto(),"producto"));

        prod.setIva(iva);
        prod.setEnable(true);

        productoRepository.save(prod);
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
    public List<ProductoRespDTO> obtenerProductos() {
        return productoRepository.findAll().stream()
                .map(p -> new ProductoRespDTO(
                        p.getIdProducto(),
                        ConvText.toUpperWords(p.getNombre()),
                        p.getDescripcion(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getIva().getCategoria(),
                        p.getIva().getPorcentaje(),
                        p.getUrl(),
                        p.isEnable()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoActRespDTO> obtenerProductosActivos() {

        return productoRepository.findAll().stream()
                .filter(Producto::isEnable)
                .filter(p -> p.getStock()>0)
                .map(producto -> new ProductoActRespDTO(
                        producto.getIdProducto(),
                        producto.getNombre(),
                        producto.getDescripcion(),
                        producto.getPrecio(),
                        producto.getUrl()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Producto obtenerProductoById(Long id) {
        return productoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Algo sucedio mal, no se encontro o no existe el producto con id " + id ));
    }

    @Override
    public boolean elProductoExiste(Long id) {
        return productoRepository.existsById(id);
    }

    @Override
    public void modificarProducto(Long id, ProductoPatchDTO prodDTO) {
        Producto prod = this.obtenerProductoById(id);
        boolean fueModificado = false;

        if(prodDTO.nombre()!=null && !prodDTO.nombre().isBlank()){
            prod.setNombre(prodDTO.nombre());
            fueModificado = true;
        }
        if(prodDTO.descripcion()!=null && !prodDTO.descripcion().isBlank()){
            prod.setDescripcion(prodDTO.descripcion());
            fueModificado = true;
        }
        if(prodDTO.precio()!=null){
            prod.setPrecio(prodDTO.precio());
            fueModificado = true;
        }

        if (id != 0){
            IVA iva = ivaRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("No se encontro el IVA que quieres asignar."));
            prod.setIva(iva);
            fueModificado = true;
        }

        if (!fueModificado){
            throw new IllegalArgumentException("Debe modificar al menos un campo");
        }

        productoRepository.save(prod);
    }
}
