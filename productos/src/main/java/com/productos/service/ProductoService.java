package com.productos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productos.dto.ProductoDTO;
import com.productos.mapper.ProductoMapper;
import com.productos.model.Producto;
import com.productos.repository.ProductoRepository;

@Service 
public class ProductoService {

    @Autowired 
    private ProductoRepository productoRepo;
    private ProductoMapper productoMapper;

    /*##-------------------------------METODOS PARA OBTENER PRODUCTOS (GET)-------------------------------*/

    /*------------Método para obtener todos los productos------------*/
    public List<Producto> obtenerProductos(){
        return productoRepo.findAll();
    }

    /*------------Método para obtener productos por el código------------*/
    public Producto obtenerProductosByCodigo(String codigo){
        return productoRepo.findByCodigo(codigo);
    }

    /*------------Método para obtener productos por id de categoría------------*/
    public List<Producto> obtenerProductosByIdCategoria(int idCategoria){
        return productoRepo.findByIdCategoria(idCategoria);
    }

    /*------------Método para obtener productos que tiene ofertas activas------------*/
    public List<Producto> obtenerProductosOfertas(){
        return productoRepo.findProductosOfertas();
    }

    /*##-------------------------------METODOS PARA AGREGAR PRODUCTOS (POST)-------------------------------*/

    /*------------Método para agregar nuevos productos------------*/
    public Producto registrarProducto(ProductoDTO dto){
        Producto producto = productoMapper.toEntity(dto);
        return productoRepo.save(producto);
    }

    /*##-------------------------------METODOS PARA ACTUALIZAR PRODUCTOS (PUT)-------------------------------*/

    public Producto actualizarProducto(String codigo, ProductoDTO dto){
        Producto existing = productoRepo.findByCodigo(codigo);
        if(existing == null){
            return null;
        }
        Producto productoActualizar = productoMapper.toEntity(dto);
        productoActualizar.setIdProducto(existing.getIdProducto());
        productoActualizar.setCodigo(codigo);

        if (dto.getImagenUrl() == null || dto.getImagenUrl().isEmpty()) {
        productoActualizar.setImagenProducto(existing.getImagenProducto());
        }
        return productoRepo.save(productoActualizar);
    }
}
