package com.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.productos.dto.ProductoDTO;
import com.productos.model.Producto;

@Repository 
public interface ProductoRepository extends JpaRepository<Producto, Integer>{

    @Query("""
        SELECT new com.productos.dto.ProductoDTO(
            p.codigo,
            p.nombre,
            p.descripcion,
            p.precio,
            p.stock,
            p.imagenProducto,
            p.enOferta,
            p.porcOferta,
            c.codigoCategoria,
            c.nombreCategoria
        )
        FROM Producto p
        JOIN p.categoria c
        """)
    List<ProductoDTO> findAllDtos();
    /*Filtrar productos por código */
    Producto findByCodigo(String codigo);
    Producto deleteByCodigo(String codigo);
    List<Producto> findByCodigoIn(List<String> codigos);

    /*Filtrar productos por el código de la categoría */
    @Query("SELECT p FROM Producto p WHERE p.categoria.codigoCategoria = :codigoCategoria")
    List<Producto> findByCodigoCategoria(@Param("codigoCategoria") String codigoCategoria);

    /*Filtrar productos si es que tienen ofertas activas */
    @Query("SELECT p FROM Producto p WHERE p.enOferta = true")
    List<Producto> findProductosOfertas();

    
}
