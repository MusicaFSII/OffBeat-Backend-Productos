package com.productos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productos.dto.ProdAgregarDTO;
import com.productos.dto.ProductoDTO;
import com.productos.mapper.ProductoMapper;
import com.productos.model.Producto;
import com.productos.repository.ProductoRepository;

@Service 
public class ProductoService {

    @Autowired 
    private ProductoRepository productoRepo;

    @Autowired 
    private ProductoMapper productoMapper;

    /*##-------------------------------METODOS PARA OBTENER PRODUCTOS (GET)-------------------------------##*/

    /*------------Método para obtener todos los productos------------*/
    public List<ProductoDTO> obtenerProductos(){
        return productoRepo.findAllDtos();
    }

    /*------------Método para obtener productos por el código------------*/
    public Producto obtenerProductosByCodigo(String codigo){
        return productoRepo.findByCodigo(codigo);
    }

    /*------------Método para obtener productos por id de categoría------------*/
    public List<Producto> obtenerProductosByCodigoCategoria(String codigoCategoria){
        return productoRepo.findByCodigoCategoria(codigoCategoria);
    }

    /*------------Método para obtener productos que tiene ofertas activas------------*/
    public List<Producto> obtenerProductosOfertas(){
        return productoRepo.findProductosOfertas();
    }


    /*##-------------------------------METODOS PARA AGREGAR PRODUCTOS (POST)-------------------------------*/

    /*------------Método para agregar nuevos productos------------*/
    public Producto registrarProducto(ProductoDTO dto){
        Producto producto = productoMapper.toEntity(dto);
        Producto guardado = productoRepo.save(producto);

        guardado.setCodigo("PR-"+guardado.getIdProducto());
        return productoRepo.save(guardado);
    }

    /*------------Método para agregar productos a compras-----------*/
    public List<ProdAgregarDTO> obtenerPorCodigos(List<String> codigos) {
        List<Producto> prods = productoRepo.findByCodigoIn(codigos);
        return prods.stream()
                .map(p -> new ProdAgregarDTO(
                    p.getCodigo(),
                    p.getNombre(),
                    p.getPrecio()
                ))
                .collect(Collectors.toList());
    }

    /*##-------------------------------METODOS PARA ACTUALIZAR PRODUCTOS (PUT)-------------------------------##*/

    /*------------Método para actualizar productos------------*/
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

    /*##-------------------------------METODOS PARA ELIMINAR PRODUCTOS (DELETE)-------------------------------##*/

    /*------------Método para eliminar productos------------*/
    public String eliminarProducto(String codigo){
        productoRepo.deleteByCodigo(codigo);
        return "El producto " + codigo + " ha sido eliminado";
    }
}
