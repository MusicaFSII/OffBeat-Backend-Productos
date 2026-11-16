package com.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.productos.dto.CategoriaDTO;
import com.productos.model.Categoria;

@Repository 
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{

    @Query("SELECT new com.productos.dto.CategoriaDTO(c.codigoCategoria, c.nombreCategoria) FROM Categoria c ORDER BY c.idCategoria")
    List<CategoriaDTO> findAllByDtos();
    /*Filtrar categorías por código */
    Categoria findByCodigoCategoria(String codigoCategoria);
    Categoria deleteByCodigoCategoria(String codigoCategoria);
}
