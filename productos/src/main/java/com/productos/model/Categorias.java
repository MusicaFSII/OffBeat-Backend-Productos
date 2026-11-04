package com.productos.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

@Entity 
@Table(name="Categorias")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Categorias {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name="id_categoria")
    private int idCategoria;

    @Column(name="nombre_categoria")
    private String nombreCategoria;

    @OneToMany(mappedBy = "Categorias", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos;
}
