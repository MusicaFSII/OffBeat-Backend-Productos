package com.productos.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class ProductoUpdateDTO {
    private String nombre;
    private String descripcion;
    @Positive private Double precio;
    @Min(0) private Integer stock;
    private MultipartFile imagen;
    private Boolean enOferta;
    @Min(0) @Max(100) private Integer porcOferta;
    private String codigoCategoria;
}
