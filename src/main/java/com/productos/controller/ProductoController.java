package com.productos.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productos.dto.ProdAgregarDTO;
import com.productos.dto.ProductoCreateDTO;
import com.productos.dto.ProductoDTO;
import com.productos.dto.ProductoUpdateDTO;
import com.productos.model.Producto;
import com.productos.service.ProductoService;

import org.springframework.http.MediaType;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController 
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    /*##-------------------------------METODOS GET-------------------------------##*/

    /*------------Método para obtener todos los productos (EndPoint base)------------*/
    @GetMapping 
    public ResponseEntity<List<ProductoDTO>> obtenerProductos(){
        List<ProductoDTO> prod = productoService.obtenerProductos();
        return ResponseEntity.ok(prod);
    }

    /*------------Método para obtener producto por su código------------*/
    @GetMapping("/{codigo}")
    public ResponseEntity<ProductoDTO> obtenerProductoByCodigo(@PathVariable("codigo") String codigo){
        ProductoDTO prod = productoService.obtenerProductosByCodigo(codigo);
        return prod == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(prod);
    }

    /*------------Método para obtener todos los productos de una categoría------------*/
    @GetMapping("/categoria/{codigoCategoria}")
    public ResponseEntity<List<Producto>> obtenerProductosByIdCategoria(@PathVariable("codigoCategoria") String codigoCategoria){
        List<Producto> prod = productoService.obtenerProductosByCodigoCategoria(codigoCategoria);
        return ResponseEntity.ok(prod);
    }

    /*------------Método para obtener todos los productos de una categoría------------*/
    @GetMapping("/ofertas")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosEnOferta(){
        List<ProductoDTO> prod = productoService.obtenerProductosOfertas();
        return ResponseEntity.ok(prod);
    }

    /*##-------------------------------METODOS POST-------------------------------##*/

    /*------------Método para guardar nuevos productos------------*/
    @PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDTO> guardarProducto(@Valid @ModelAttribute ProductoCreateDTO dto) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registrarProducto(dto));
    }

    /*------------Método para agregar productos a la API COMPRAS------------*/
    @PostMapping("/lista")
    public ResponseEntity<List<ProdAgregarDTO>> listaProductosCompra(@RequestBody List<String> codigos){
        List<ProdAgregarDTO> prods = productoService.obtenerPorCodigos(codigos);
        return ResponseEntity.ok(prods);
    }

    /*##-------------------------------METODOS PUT-------------------------------##*/

    /*------------Método para guardar nuevos productos------------*/
    @PutMapping(value = "/actualizar/{codigo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable("codigo") String codigo, @Valid @ModelAttribute ProductoUpdateDTO dto) throws IOException {
        ProductoDTO prod = productoService.actualizarProducto(codigo, dto);
        return prod == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(prod);
    }

    /*##-------------------------------METODOS DELETE-------------------------------##*/
    @DeleteMapping("/eliminar/{codigo}")
    public ResponseEntity<Producto> eliminarProducto(@PathVariable("codigo") String codigo){
        productoService.eliminarProducto(codigo);
        return ResponseEntity.noContent().build();
    }
}
