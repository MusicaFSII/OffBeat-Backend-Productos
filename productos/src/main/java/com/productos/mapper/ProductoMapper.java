package com.productos.mapper;

import org.springframework.stereotype.Component;

import com.productos.dto.ProductoCreateDTO;
import com.productos.dto.ProductoDTO;
import com.productos.dto.ProductoUpdateDTO;
import com.productos.model.Categoria;
import com.productos.model.Producto;
import com.productos.repository.CategoriaRepository;

@Component 
public class ProductoMapper {

    private final CategoriaRepository categoriaRepository;

    public ProductoMapper(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Producto toEntity(ProductoCreateDTO dto){
        if(dto == null){
            return null;
        }

        Categoria categoria = categoriaRepository.findByCodigoCategoria(dto.getCodigoCategoria());
        if (categoria == null) {
            throw new IllegalArgumentException("No existe la categoría " + dto.getCodigoCategoria());
        }

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock() != null ? dto.getStock() : 0);
        producto.setCategoria(categoria);
        producto.setEnOferta(Boolean.TRUE.equals(dto.getEnOferta()));
        producto.setPorcOferta(dto.getPorcOferta() != null ? dto.getPorcOferta() : 0);
        return producto;
    } 

    public ProductoDTO toDTO(Producto p) {
    if (p == null) {
        return null;
    }

    double precioIngresado = p.getPrecio();
    int descuento = p.isEnOferta() ? p.getPorcOferta() : 0;

    double precioFinal = Math.round(precioIngresado * (100 - descuento) / 100.0);
    double neto = Math.round(precioFinal / (1 + 0.19));

    ProductoDTO dto = new ProductoDTO();
    dto.setCodigo(p.getCodigo());
    dto.setNombre(p.getNombre());
    dto.setDescripcion(p.getDescripcion());
    dto.setPrecio(precioIngresado);
    dto.setPrecioFinal(precioFinal);
    dto.setPrecioIva(precioFinal - neto);
    dto.setStock(p.getStock());
    dto.setImagenUrl(p.getImagenProducto());
    dto.setEnOferta(p.isEnOferta());
    dto.setPorcOferta(p.getPorcOferta());
    dto.setCodigoCategoria(p.getCategoria().getCodigoCategoria());
    dto.setNombreCategoria(p.getCategoria().getNombreCategoria());
    return dto;
    }

    public void actualizarEntity(Producto existing, ProductoUpdateDTO dto) {
    if (dto.getNombre() != null && !dto.getNombre().isBlank()) existing.setNombre(dto.getNombre());
    if (dto.getDescripcion() != null && !dto.getDescripcion().isBlank()) existing.setDescripcion(dto.getDescripcion());
    if (dto.getPrecio() != null) existing.setPrecio(dto.getPrecio());
    if (dto.getStock() != null) existing.setStock(dto.getStock());
    if (dto.getEnOferta() != null) existing.setEnOferta(dto.getEnOferta());
    if (dto.getPorcOferta() != null) existing.setPorcOferta(dto.getPorcOferta());

    if (dto.getCodigoCategoria() != null && !dto.getCodigoCategoria().isBlank()) {
        Categoria categoria = categoriaRepository.findByCodigoCategoria(dto.getCodigoCategoria());
        if (categoria == null) {
            throw new IllegalArgumentException("No existe la categoría " + dto.getCodigoCategoria());
        }
        existing.setCategoria(categoria);
    }
}

}