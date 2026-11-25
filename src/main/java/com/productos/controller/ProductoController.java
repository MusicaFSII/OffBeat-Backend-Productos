package com.productos.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.productos.dto.ItemStockDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    /*##-------------------------------METODOS GET-------------------------------##*/

    /*------------Método para obtener todos los productos (EndPoint base)------------*/
    
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados")
    @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente")
    @GetMapping 
    public ResponseEntity<List<ProductoDTO>> obtenerProductos(){
        List<ProductoDTO> prod = productoService.obtenerProductos();
        return ResponseEntity.ok(prod);
    }

    /*------------Método para obtener producto por su código------------*/
    
    @Operation(summary = "Obtener producto por código", description = "Busca un producto con su código")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<ProductoDTO> obtenerProductoByCodigo(@PathVariable("codigo") String codigo){
        ProductoDTO prod = productoService.obtenerProductosByCodigo(codigo);
        return ResponseEntity.ok(prod);
    }

    /*------------Método para obtener todos los productos de una categoría------------*/
    
    @Operation(summary = "Listar productos por categoría", description = "Obtiene todos los productos de una categoría con su código")
    @ApiResponse(responseCode = "200", description = "Productos de categoría obtenidos correctamente")
    @GetMapping("/categoria/{codigoCategoria}")
    public ResponseEntity<List<Producto>> obtenerProductosByIdCategoria(@PathVariable("codigoCategoria") String codigoCategoria){
        List<Producto> prod = productoService.obtenerProductosByCodigoCategoria(codigoCategoria);
        return ResponseEntity.ok(prod);
    }

    /*------------Método para obtener todos los productos de una categoría------------*/
    
    @Operation(summary = "Listar productos en oferta", description = "Obtiene todos los productos que están en oferta")
    @ApiResponse(responseCode = "200", description = "Ofertas obtenidas correctamente")
    @GetMapping("/ofertas")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosEnOferta(){
        List<ProductoDTO> prod = productoService.obtenerProductosOfertas();
        return ResponseEntity.ok(prod);
    }

    /*##-------------------------------METODOS POST-------------------------------##*/

    /*------------Método para guardar nuevos productos------------*/
    
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto. Se utiliza multipart/form-data (permite adjuntar imagen)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDTO> guardarProducto(@Valid @ModelAttribute ProductoCreateDTO dto) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registrarProducto(dto));
    }

    /*------------Método para agregar productos a la API COMPRAS------------*/
    
    @Operation(summary = "Obtener datos de productos para su compra", description = "Recibe una lista de códigos y devuelve los datos de productos hacia API de Compras")
    @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente")
    @PostMapping("/lista")
    public ResponseEntity<List<ProdAgregarDTO>> listaProductosCompra(@RequestBody List<String> codigos){
        List<ProdAgregarDTO> prods = productoService.obtenerPorCodigos(codigos);
        return ResponseEntity.ok(prods);
    }

    @Operation(summary = "Descontar stock", description = "Descuenta stock de varios productos en una sola transacción")
    @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Stock descontado"),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    @ApiResponse(responseCode = "409", description = "Stock insuficiente")
    })
    @PostMapping("/descontar-stock")
    public ResponseEntity<Void> descontarStock(@Valid @RequestBody List<ItemStockDTO> items) {
        productoService.descontarStock(items);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reponer-stock")
    public ResponseEntity<Void> reponerStock(@Valid @RequestBody List<ItemStockDTO> items) {
        productoService.reponerStock(items);
        return ResponseEntity.noContent().build();
    }
    /*##-------------------------------METODOS PUT-------------------------------##*/

    /*------------Método para guardar nuevos productos------------*/
    
    @Operation(summary = "Actualizar producto", description = "Actualiza producto con su código. Se envía como multipart/form-data")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping(value = "/actualizar/{codigo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable("codigo") String codigo, @Valid @ModelAttribute ProductoUpdateDTO dto) throws IOException {
        ProductoDTO prod = productoService.actualizarProducto(codigo, dto);
        return ResponseEntity.ok(prod);
    }

    /*##-------------------------------METODOS DELETE-------------------------------##*/
    
    @Operation(summary = "Eliminar producto", description = "Elimina un producto con su código")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/eliminar/{codigo}")
    public ResponseEntity<Producto> eliminarProducto(@PathVariable("codigo") String codigo){
        productoService.eliminarProducto(codigo);
        return ResponseEntity.noContent().build();
    }
}
