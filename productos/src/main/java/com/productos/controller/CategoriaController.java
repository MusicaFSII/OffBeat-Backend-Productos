package com.productos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productos.dto.CategoriaDTO;
import com.productos.model.Categoria;
import com.productos.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController 
@RequestMapping("/api/v1/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    /*##-------------------------------METODOS GET-------------------------------##*/

    /*------------Método para obtener todas las categorías (EndPoint base)------------*/
    
    @Operation(summary = "Listar categorías", description = "Obtiene todas las categorías registradas")
    @ApiResponse(responseCode = "200", description = "Categorías obtenidas correctamente")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerProductos(){
        List<CategoriaDTO> cate = categoriaService.obtenerCategorias();
        return ResponseEntity.ok(cate);
    }

    /*##-------------------------------METODOS POST-------------------------------##*/

    /*------------Método para agregar nuevas categorías------------*/
    
    @Operation(summary = "Crear categoría", description = "Registra nueva categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoría creada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o JSON incorrecto")
    })
    @PostMapping("/guardar")
    public ResponseEntity<Categoria> guardarCategorias(@RequestBody CategoriaDTO dto){
        Categoria cateNueva = categoriaService.agregarCategorias(dto);
        return ResponseEntity.ok(cateNueva);
    }

    /*##-------------------------------METODOS PUT-------------------------------##*/

    /*------------Método para guardar nuevas categorías------------*/
    
    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría con su código")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o JSON incorrecto"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/actualizar/{codigoCategoria}")
    public ResponseEntity<Categoria> actualizarCategorias(@PathVariable("codigoCategoria") String codigoCategoria, @RequestBody CategoriaDTO dto){
        Categoria cateActu = categoriaService.actualizarCategorias(codigoCategoria, dto);
        return ResponseEntity.ok(cateActu);
    }

    /*##-------------------------------METODOS PUT-------------------------------##*/

    /*------------Método para eliminar categorías------------*/
    
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría con su código")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría eliminada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/eliminar/{codigoCategoria}")
    public String eliminarCategorias(@PathVariable("codigoCategoria") String codigoCategoria){
        categoriaService.eliminarCategorias(codigoCategoria);
        return "Categoria " + codigoCategoria + "eliminada.";
    }
}
