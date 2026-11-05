package com.productos.mapper;

import java.util.Base64;

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

        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());

        if(dto.getImagenUrl() != null && !dto.getImagenUrl().isEmpty()){
            try {
                String base64Data = dto.getImagenUrl();
                byte[] imagenBytes = Base64.getDecoder().decode(base64Data);
                producto.setImagenProducto(imagenBytes);
            } catch (Exception e) {
                producto.setImagenProducto(null);
            }
        }

        Categoria categoria = categoriaRepository.findByCodigoCategoria(dto.getCodigoCategoria());
        producto.setCategoria(categoria);
        
        if(dto.getEnOferta() !=null && !dto.getEnOferta().isEmpty()){
            if(dto.getEnOferta().equalsIgnoreCase("si")){
                producto.setEnOferta(true);
            }else{
                producto.setEnOferta(false);
            }
        } 

        producto.setPorcOferta(dto.getPorcOferta()); 

        return producto;
    } 
}