package com.productos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productos.dto.CategoriaDTO;
import com.productos.model.Categoria;
import com.productos.repository.CategoriaRepository;

@Service 
public class CategoriaService {

    @Autowired 

    private CategoriaRepository categoriaRepo;

    /*------------Método para obtener todos las categorías------------*/
    public List<Categoria> obtenerCategorias(){
        return categoriaRepo.findAll();
    }

    /*------------Método para agregar nuevas categorías------------*/
    public Categoria agregarCategorias(CategoriaDTO dto){
        Categoria cate = new Categoria();
        cate.setIdCategoria(cate.getIdCategoria());
        cate.setCodigoCategoria("CAT-"+ String.valueOf(cate.getIdCategoria()));
        cate.setNombreCategoria(dto.getNombre());
        return categoriaRepo.save(cate);
    }

    /*------------Método para actualizar categorías------------*/
    public Categoria actualizarCategorias(String codigoCategoria, CategoriaDTO dto){
        Categoria existing = categoriaRepo.findByCodigoCategoria(codigoCategoria);
        if(existing == null){
            return null;
        }
        existing.setIdCategoria(existing.getIdCategoria());
        existing.setCodigoCategoria(existing.getCodigoCategoria());
        existing.setNombreCategoria(dto.getNombre());
        return categoriaRepo.save(existing);
    }

    /*------------Método para eliminar categorías------------*/

    public Categoria eliminarCategorias(String codigoCategoria){
        return categoriaRepo.deleteByCodigoCategoria(codigoCategoria);
    }
}
