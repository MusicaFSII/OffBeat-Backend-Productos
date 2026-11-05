package com.productos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productos.dto.ProductoDTO;
import com.productos.model.Producto;
import com.productos.service.ProductoService;

@RestController 
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    /*##-------------------------------METODOS GET-------------------------------##*/

    /*------------Método para obtener todos los productos (EndPoint base)------------*/
    @GetMapping 
    public ResponseEntity<List<Producto>> obtenerProductos(){
        List<Producto> prod = productoService.obtenerProductos();
        return ResponseEntity.ok(prod);
    }

    /*------------Método para obtener producto por su código------------*/
    @GetMapping("/{codigo}")
    public ResponseEntity<Producto> obtenerProductoByCodigo(@PathVariable("codigo") String codigo){
        Producto prod = productoService.obtenerProductosByCodigo(codigo);
        return ResponseEntity.ok(prod);
    }

    /*------------Método para obtener todos los productos de una categoría------------*/
    @GetMapping("/{codigoCategoria}")
    public ResponseEntity<List<Producto>> obtenerProductosByIdCategoria(@PathVariable("codigoCategoria") String codigoCategoria){
        List<Producto> prod = productoService.obtenerProductosByCodigoCategoria(codigoCategoria);
        return ResponseEntity.ok(prod);
    }

    /*------------Método para obtener todos los productos de una categoría------------*/
    @GetMapping("/ofertas")
    public ResponseEntity<List<Producto>> obtenerProductosEnOferta(){
        List<Producto> prod = productoService.obtenerProductosOfertas();
        return ResponseEntity.ok(prod);
    }

    /*##-------------------------------METODOS POST-------------------------------##*/

    /*------------Método para guardar nuevos productos------------*/
    @PostMapping("/guardar")
    public ResponseEntity<Producto> guardarProducto(@RequestBody ProductoDTO dto){
        Producto prodNuevo = productoService.registrarProducto(dto);
        return ResponseEntity.ok(prodNuevo);
    }

    /*##-------------------------------METODOS PUT-------------------------------##*/

    /*------------Método para guardar nuevos productos------------*/
    @PutMapping("/actualizar/{codigo}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable("codigo") String codigo, @RequestBody ProductoDTO dto){
        Producto prod = productoService.actualizarProducto(codigo, dto);
        return ResponseEntity.ok(prod);
    }

    /*##-------------------------------METODOS DELETE-------------------------------##*/
    @DeleteMapping("/eliminar/{codigo}")
    public ResponseEntity<Producto> eliminarProducto(@PathVariable("codigo") String codigo){
        productoService.eliminarProducto(codigo);
        return ResponseEntity.noContent().build();
    }
}
