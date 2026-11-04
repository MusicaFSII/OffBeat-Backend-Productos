package com.productos.mapper;

import java.util.Base64;

import org.springframework.stereotype.Component;

import com.productos.dto.ProductoDTO;
import com.productos.model.Producto;

@Component 
public class ProductoMapper {

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

        producto.setCategoriaId(dto.getCategoriaId());
        
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