package com.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.productos.model.Producto;

@Repository 
public interface ProductoRepository extends JpaRepository<Producto, Integer>{

    /*Filtrar productos por código */
    Producto findByCodigo(String codigo);
    Producto deleteByCodigo(String codigo);

    /*Filtrar productos por el código de la categoría */
    @Query("SELECT p FROM Producto p WHERE p.categoria.codigoCategoria = :codigoCategoria")
    List<Producto> findByCodigoCategoria(@Param("codigoCategoria") String codigoCategoria);

    /*Filtrar productos si es que tienen ofertas activas */
    @Query("SELECT p FROM Producto p WHERE p.enOferta = true")
    List<Producto> findProductosOfertas();

    
}
