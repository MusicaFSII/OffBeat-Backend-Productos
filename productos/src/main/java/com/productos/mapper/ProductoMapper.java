package com.productos.mapper;

import org.springframework.stereotype.Component;

import com.productos.dto.ProductoDTO;
import com.productos.model.Categoria;
import com.productos.model.Producto;
import com.productos.repository.CategoriaRepository;

@Component 
public class ProductoMapper {

    private final CategoriaRepository categoriaRepository;

    public ProductoMapper(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Producto toEntity(ProductoDTO dto){
        if(dto == null){
            return null;
        }

        Producto producto = new Producto();

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagenProducto(dto.getImagenUrl());

        Categoria categoria = categoriaRepository.findByCodigoCategoria(dto.getCodigoCategoria());
        
        producto.setCategoria(categoria);
        producto.setEnOferta(dto.getEnOferta());
        producto.setPorcOferta(dto.getPorcOferta() != null ? dto.getPorcOferta() : 0); 

        return producto;
    } 
}