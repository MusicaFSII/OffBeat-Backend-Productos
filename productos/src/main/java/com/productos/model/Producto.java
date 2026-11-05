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
    @Column(name="id_producto")
    private int idProducto;

    @Column(name="codigo")
    private String codigo;

    @Column(name="nombre")
    private String nombre;

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="precio")
    private Double precio;    

    @Column(name="stock")
    private int stock = 0;

    @Column(name="imagen_producto", columnDefinition = "BLOB")
    private byte[] imagenProducto;

    @Column(name="en_oferta")
    private boolean enOferta;

    @Column(name="porc_oferta")
    private int porcOferta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_categoria", referencedColumnName = "codigo_categoria", nullable = false)
    private Categoria categoria;
}
