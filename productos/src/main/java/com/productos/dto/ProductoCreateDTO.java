package com.productos.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class ProductoCreateDTO {

    @NotBlank(message = "El producto debe tener un nombre")
    private String nombre;

    @NotBlank(message = "El producto debe tener una descripción")
    private String descripcion;

    @NotNull(message = "El producto debe tener un precio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio; 

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "Es obligatorio agregar una imagen al producto")
    private MultipartFile imagen;

    private Boolean enOferta;

    @Min(0) @Max(100)
    private Integer porcOferta;

    @NotBlank(message = "El producto debe tener una categoría")
    private String codigoCategoria;
}
