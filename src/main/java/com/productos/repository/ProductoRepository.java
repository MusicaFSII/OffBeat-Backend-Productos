package com.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;

import com.productos.dto.ProductoDTO;
import com.productos.model.Producto;

@Repository 
public interface ProductoRepository extends JpaRepository<Producto, Integer>{

    @Override
    @EntityGraph(attributePaths = "categoria")
    List<Producto> findAll();

    /*Filtrar productos por código */
    Producto findByCodigo(String codigo);
    Producto deleteByCodigo(String codigo);
    List<Producto> findByCodigoIn(List<String> codigos);

    /*Filtrar productos por el código de la categoría */
    @Query("SELECT p FROM Producto p WHERE p.categoria.codigoCategoria = :codigoCategoria")
    List<Producto> findByCodigoCategoria(@Param("codigoCategoria") String codigoCategoria);

    /*Filtrar productos si es que tienen ofertas activas */
    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria WHERE p.enOferta = true")
    List<Producto> findProductosOfertas();

    /*Descontar stock de forma atómica (devuelve 0 si no alcanza) */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Producto p SET p.stock = p.stock - :cant " +
        "WHERE p.codigo = :codigo AND p.stock >= :cant")
    int descontarStock(@Param("codigo") String codigo, @Param("cant") int cant);

    /*Reponer stock (compra fallida o anulada) */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Producto p SET p.stock = p.stock + :cant WHERE p.codigo = :codigo")
    int reponerStock(@Param("codigo") String codigo, @Param("cant") int cant);
 
}