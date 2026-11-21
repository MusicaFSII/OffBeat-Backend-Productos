package com.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class ProdAgregarDTO {
    private String codigo;
    private String nombre;
    private Double precioFinal;
}
