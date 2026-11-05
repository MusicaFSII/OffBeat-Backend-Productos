package com.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productos.model.Categoria;

@Repository 
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{

    /*Filtrar categorías por código */
    Categoria findByCodigoCategoria(String codigoCategoria);
    Categoria deleteByCodigoCategoria(String codigoCategoria);
}
