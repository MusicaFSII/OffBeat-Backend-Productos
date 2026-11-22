package com.productos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.productos.dto.CategoriaDTO;
import com.productos.exception.ResourceNotFoundException;
import com.productos.model.Categoria;
import com.productos.repository.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepo;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    @DisplayName("agregarCategorias: genera el código CAT-{id} y guarda dos veces")
    void agregarCategorias_generaCodigoConElId() {
        CategoriaDTO dto = new CategoriaDTO(null, "Bebidas");

        // Simula la BD: la primera vez que se guarda, le asigna el id 5
        when(categoriaRepo.save(any(Categoria.class))).thenAnswer(inv -> {
            Categoria c = inv.getArgument(0);
            if (c.getIdCategoria() == null) {
                c.setIdCategoria(5);
            }
            return c;
        });

        Categoria resultado = categoriaService.agregarCategorias(dto);

        assertEquals("CAT-5", resultado.getCodigoCategoria());
        assertEquals("Bebidas", resultado.getNombreCategoria());
        // 1° save para obtener el id, 2° save para guardar el código
        verify(categoriaRepo, times(2)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("actualizarCategorias: si no existe lanza ResourceNotFoundException y no guarda")
    void actualizarCategorias_noExiste_lanzaExcepcionYNoGuarda() {
        when(categoriaRepo.findByCodigoCategoria("CAT-99")).thenReturn(null);

        CategoriaDTO dto = new CategoriaDTO(null, "Nuevo nombre");

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> categoriaService.actualizarCategorias("CAT-99", dto));

        assertEquals("No existe la categoría de código CAT-99", ex.getMessage());
        verify(categoriaRepo, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("eliminarCategorias: si existe, la elimina del repositorio")
    void eliminarCategorias_existente_llamaADelete() {
        Categoria existente = new Categoria(1, "CAT-1", "Snacks", null);
        when(categoriaRepo.findByCodigoCategoria("CAT-1")).thenReturn(existente);

        categoriaService.eliminarCategorias("CAT-1");

        verify(categoriaRepo).delete(existente);
    }

    @Test
    @DisplayName("eliminarCategorias: si no existe lanza ResourceNotFoundException y no elimina")
    void eliminarCategorias_noExiste_lanzaExcepcionYNoElimina() {
        when(categoriaRepo.findByCodigoCategoria("CAT-99")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> categoriaService.eliminarCategorias("CAT-99"));

        verify(categoriaRepo, never()).delete(any(Categoria.class));
    }
}