package com.productos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name="Productos")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Producto {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_producto", nullable =false)
    private Integer idProducto;

    @Column(name="codigo", unique = true)
    private String codigo;

    @Column(name="nombre", unique = true, nullable = false)
    private String nombre;

    @Column(name="descripcion", nullable = false)
    private String descripcion;

    @Column(name="precio", nullable = false)
    private Double precio;    

    @Column(name="stock", nullable = false)
    private int stock = 0;

    @Column(name="imagen_producto", nullable = false)
    private String imagenProducto;

    @Column(name="en_oferta", nullable = false)
    private boolean enOferta;

    @Column(name="porc_oferta")
    private int porcOferta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_categoria", referencedColumnName = "codigo_categoria", nullable = false)
    private Categoria categoria;
}
