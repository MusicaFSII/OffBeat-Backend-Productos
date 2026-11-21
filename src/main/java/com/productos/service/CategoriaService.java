package com.productos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productos.dto.CategoriaDTO;
import com.productos.exception.ResourceNotFoundException;
import com.productos.model.Categoria;
import com.productos.repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service 
public class CategoriaService {

    @Autowired 

    private CategoriaRepository categoriaRepo;

    public CategoriaService(CategoriaRepository categoriaRepo) {
        this.categoriaRepo = categoriaRepo;
    }

    /*------------Método para obtener todos las categorías------------*/
    public List<CategoriaDTO> obtenerCategorias(){
        return categoriaRepo.findAllByDtos();
    }

    /*------------Método para agregar nuevas categorías------------*/
    @Transactional
    public Categoria agregarCategorias(CategoriaDTO dto){
        Categoria cate = new Categoria();
        cate.setNombreCategoria(dto.getNombre());

        Categoria guardada = categoriaRepo.save(cate);   

        guardada.setCodigoCategoria("CAT-" + guardada.getIdCategoria());
        return categoriaRepo.save(guardada);
    }

    /*------------Método para actualizar categorías------------*/
    public Categoria actualizarCategorias(String codigoCategoria, CategoriaDTO dto){
        Categoria existing = categoriaRepo.findByCodigoCategoria(codigoCategoria);
        if(existing == null){
            throw new ResourceNotFoundException(
                "No existe la categoría de código " + codigoCategoria);
        }
        existing.setIdCategoria(existing.getIdCategoria());
        existing.setCodigoCategoria(existing.getCodigoCategoria());
        existing.setNombreCategoria(dto.getNombre());
        return categoriaRepo.save(existing);
    }

    /*------------Método para eliminar categorías------------*/

    @Transactional 
    public void eliminarCategorias(String codigoCategoria) {
        Categoria existing = categoriaRepo.findByCodigoCategoria(codigoCategoria);
        if (existing == null) {
            throw new ResourceNotFoundException(
                    "No existe la categoría con código " + codigoCategoria);
        }
        categoriaRepo.delete(existing);
    }
}
