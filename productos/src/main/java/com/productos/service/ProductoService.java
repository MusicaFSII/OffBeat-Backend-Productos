package com.productos.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productos.dto.ProdAgregarDTO;
import com.productos.dto.ProductoCreateDTO;
import com.productos.dto.ProductoDTO;
import com.productos.dto.ProductoUpdateDTO;
import com.productos.mapper.ProductoMapper;
import com.productos.model.Producto;
import com.productos.repository.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class ProductoService {

    @Autowired 
    private ProductoRepository productoRepo;

    @Autowired 
    private ProductoMapper productoMapper;

    @Autowired
    private FileStorageService storage;

    /*##-------------------------------METODOS PARA OBTENER PRODUCTOS (GET)-------------------------------##*/

    /*------------Método para obtener todos los productos------------*/
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductos(){
        return productoRepo.findAll().stream().map(productoMapper::toDTO).toList();
    }

    /*------------Método para obtener productos por el código------------*/
    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductosByCodigo(String codigo){
        return productoMapper.toDTO(productoRepo.findByCodigo(codigo));
    }

    /*------------Método para obtener productos por id de categoría------------*/
    public List<Producto> obtenerProductosByCodigoCategoria(String codigoCategoria){
        return productoRepo.findByCodigoCategoria(codigoCategoria);
    }

    /*------------Método para obtener productos que tiene ofertas activas------------*/
    public List<ProductoDTO> obtenerProductosOfertas(){
        return productoRepo.findProductosOfertas().stream().map(productoMapper::toDTO).toList();
    }


    /*##-------------------------------METODOS PARA AGREGAR PRODUCTOS (POST)-------------------------------*/

    /*------------Método para agregar nuevos productos------------*/
    @Transactional
    public ProductoDTO registrarProducto(ProductoCreateDTO dto) throws IOException {
        Producto producto = productoMapper.toEntity(dto);
        producto.setImagenProducto(storage.guardar(dto.getImagen()));
        producto = productoRepo.save(producto);
        producto.setCodigo("PR-" + producto.getIdProducto());
        return productoMapper.toDTO(producto);
    }

    /*------------Método para agregar productos a compras-----------*/
    public List<ProdAgregarDTO> obtenerPorCodigos(List<String> codigos) {
        List<Producto> prods = productoRepo.findByCodigoIn(codigos);
        return prods.stream()
                .map(p -> new ProdAgregarDTO(
                    p.getCodigo(),
                    p.getNombre(),
                    p.isEnOferta() 
                    ? p.getPrecio() * (1 - p.getPorcOferta() / 100.0) : p.getPrecio()
                ))
                .collect(Collectors.toList());
    }

    /*##-------------------------------METODOS PARA ACTUALIZAR PRODUCTOS (PUT)-------------------------------##*/

    /*------------Método para actualizar productos------------*/
    @Transactional
    public ProductoDTO actualizarProducto(String codigo, ProductoUpdateDTO dto) throws IOException {
    Producto existing = productoRepo.findByCodigo(codigo);
    if (existing == null) {
        return null;
    }

    productoMapper.actualizarEntity(existing, dto);

    if (dto.getImagen() != null && !dto.getImagen().isEmpty()) {
        String anterior = existing.getImagenProducto();
        existing.setImagenProducto(storage.guardar(dto.getImagen()));
        storage.eliminar(anterior);
    }

    return productoMapper.toDTO(productoRepo.save(existing));
}

    /*##-------------------------------METODOS PARA ELIMINAR PRODUCTOS (DELETE)-------------------------------##*/

    /*------------Método para eliminar productos------------*/

    @Transactional 
    public String eliminarProducto(String codigo){
        productoRepo.deleteByCodigo(codigo);
        return "El producto " + codigo + " ha sido eliminado";
    }
}
