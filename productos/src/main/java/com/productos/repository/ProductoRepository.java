package com.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.productos.model.Producto;

@Repository 
public interface ProductoRepository extends JpaRepository<Producto, Integer>{

    /*Filtrar productos por código */
    Producto findByCodigo(String codigo);

    /*Filtrar productos por el id de la categoría */
    List<Producto> findByIdCategoria(int idCategoria);

    /*Filtrar productos si es que tienen ofertas activas */
    @Query("SELECT * FROM productos WHERE en_oferta = true")
    List<Producto> findProductosOfertas();
}
