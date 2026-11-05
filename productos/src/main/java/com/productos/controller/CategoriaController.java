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

import com.productos.dto.CategoriaDTO;
import com.productos.model.Categoria;
import com.productos.service.CategoriaService;

@RestController 
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    /*##-------------------------------METODOS GET-------------------------------##*/

    /*------------Método para obtener todas las categorías (EndPoint base)------------*/
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerProductos(){
        List<Categoria> cate = categoriaService.obtenerCategorias();
        return ResponseEntity.ok(cate);
    }

    /*##-------------------------------METODOS POST-------------------------------##*/

    /*------------Método para agregar nuevas categorías------------*/
    @PostMapping("/guardar")
    public ResponseEntity<Categoria> guardarCategorias(@RequestBody CategoriaDTO dto){
        Categoria cateNueva = categoriaService.agregarCategorias(dto);
        return ResponseEntity.ok(cateNueva);
    }

    /*##-------------------------------METODOS PUT-------------------------------##*/

    /*------------Método para guardar nuevas categorías------------*/
    @PutMapping("/actualizar/{codigoCategoria}")
    public ResponseEntity<Categoria> actualizarCategorias(@PathVariable("codigoCategoria") String codigoCategoria, @RequestBody CategoriaDTO dto){
        Categoria cateActu = categoriaService.actualizarCategorias(codigoCategoria, dto);
        return ResponseEntity.ok(cateActu);
    }

    /*##-------------------------------METODOS PUT-------------------------------##*/

    /*------------Método para eliminar categorías------------*/
    @DeleteMapping("/eliminar/{codigoCategoria}")
    public String eliminarCategorias(@PathVariable("codigoCategoria") String codigoCategoria){
        categoriaService.eliminarCategorias(codigoCategoria);
        return "Categoria " + codigoCategoria + "eliminada.";
    }
}
