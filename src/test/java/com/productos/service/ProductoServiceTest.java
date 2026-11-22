package com.productos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.productos.dto.ProdAgregarDTO;
import com.productos.dto.ProductoCreateDTO;
import com.productos.dto.ProductoDTO;
import com.productos.dto.ProductoUpdateDTO;
import com.productos.exception.ResourceNotFoundException;
import com.productos.mapper.ProductoMapper;
import com.productos.model.Producto;
import com.productos.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepo;

    @Mock
    private ProductoMapper productoMapper;

    @Mock
    private FileStorageService storage;

    @InjectMocks
    private ProductoService productoService;

    /*---------------------- obtenerProductosByCodigo ----------------------*/

    @Test
    @DisplayName("obtenerProductosByCodigo: si existe devuelve el DTO del mapper")
    void obtenerProductosByCodigo_existente_devuelveDTO() {
        Producto producto = new Producto();
        producto.setCodigo("PR-1");
        ProductoDTO dtoEsperado = new ProductoDTO();
        dtoEsperado.setCodigo("PR-1");

        when(productoRepo.findByCodigo("PR-1")).thenReturn(producto);
        when(productoMapper.toDTO(producto)).thenReturn(dtoEsperado);

        ProductoDTO resultado = productoService.obtenerProductosByCodigo("PR-1");

        assertSame(dtoEsperado, resultado);
    }

    @Test
    @DisplayName("obtenerProductosByCodigo: si no existe lanza ResourceNotFoundException")
    void obtenerProductosByCodigo_noExiste_lanzaExcepcion() {
        when(productoRepo.findByCodigo("PR-99")).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> productoService.obtenerProductosByCodigo("PR-99"));

        assertEquals("No existe el producto PR-99", ex.getMessage());
        verify(productoMapper, never()).toDTO(any(Producto.class));
    }

    /*---------------------- registrarProducto ----------------------*/

    @Test
    @DisplayName("registrarProducto: guarda la imagen, genera el código PR-{id} y devuelve el DTO")
    void registrarProducto_valido_asignaImagenYCodigo() throws Exception {
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", "foto.png", "image/png", new byte[] {1, 2, 3});
        ProductoCreateDTO dto = new ProductoCreateDTO();
        dto.setNombre("Coca-Cola");
        dto.setImagen(imagen);

        Producto producto = new Producto();
        ProductoDTO dtoEsperado = new ProductoDTO();
        dtoEsperado.setCodigo("PR-7");

        when(productoMapper.toEntity(dto)).thenReturn(producto);
        when(storage.guardar(imagen)).thenReturn("abc123.png");
        // Simula la BD: al guardar le asigna el id 7
        when(productoRepo.save(producto)).thenAnswer(inv -> {
            Producto p = inv.getArgument(0);
            p.setIdProducto(7);
            return p;
        });
        when(productoMapper.toDTO(producto)).thenReturn(dtoEsperado);

        ProductoDTO resultado = productoService.registrarProducto(dto);

        assertEquals("PR-7", producto.getCodigo());
        assertEquals("abc123.png", producto.getImagenProducto());
        assertSame(dtoEsperado, resultado);
        verify(productoRepo).save(producto);
    }

    @Test
    @DisplayName("registrarProducto: si la categoría no existe lanza error y no guarda imagen ni producto")
    void registrarProducto_categoriaInexistente_noGuardaNada() throws Exception {
        ProductoCreateDTO dto = new ProductoCreateDTO();
        dto.setCodigoCategoria("CAT-99");

        when(productoMapper.toEntity(dto))
                .thenThrow(new IllegalArgumentException("No existe la categoría CAT-99"));

        assertThrows(IllegalArgumentException.class,
                () -> productoService.registrarProducto(dto));

        // Ni se subió la imagen ni se guardó el producto
        verifyNoInteractions(storage);
        verifyNoInteractions(productoRepo);
    }

    /*---------------------- actualizarProducto ----------------------*/

    @Test
    @DisplayName("actualizarProducto: si no existe lanza ResourceNotFoundException y no modifica nada")
    void actualizarProducto_noExiste_lanzaExcepcionYNoGuarda() throws Exception {
        when(productoRepo.findByCodigo("PR-99")).thenReturn(null);

        ProductoUpdateDTO dto = new ProductoUpdateDTO();

        assertThrows(ResourceNotFoundException.class,
                () -> productoService.actualizarProducto("PR-99", dto));

        verify(productoMapper, never()).actualizarEntity(any(Producto.class), any(ProductoUpdateDTO.class));
        verify(productoRepo, never()).save(any(Producto.class));
        verifyNoInteractions(storage);
    }

    @Test
    @DisplayName("actualizarProducto: con imagen nueva guarda la nueva y elimina la anterior")
    void actualizarProducto_conImagenNueva_reemplazaLaAnterior() throws Exception {
        Producto existente = new Producto();
        existente.setCodigo("PR-1");
        existente.setImagenProducto("vieja.png");

        MockMultipartFile imagenNueva = new MockMultipartFile(
                "imagen", "nueva.png", "image/png", new byte[] {1, 2, 3});
        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setImagen(imagenNueva);

        ProductoDTO dtoEsperado = new ProductoDTO();
        dtoEsperado.setCodigo("PR-1");

        when(productoRepo.findByCodigo("PR-1")).thenReturn(existente);
        when(storage.guardar(imagenNueva)).thenReturn("nueva-uuid.png");
        when(productoRepo.save(existente)).thenReturn(existente);
        when(productoMapper.toDTO(existente)).thenReturn(dtoEsperado);

        ProductoDTO resultado = productoService.actualizarProducto("PR-1", dto);

        assertEquals("nueva-uuid.png", existente.getImagenProducto());
        assertSame(dtoEsperado, resultado);
        verify(productoMapper).actualizarEntity(existente, dto);
        verify(storage).eliminar("vieja.png");
    }

    /*---------------------- obtenerPorCodigos (API Compras) ----------------------*/

    @Test
    @DisplayName("obtenerPorCodigos: aplica el descuento solo a los productos en oferta")
    void obtenerPorCodigos_calculaPrecioFinalSegunOferta() {
        Producto enOferta = new Producto();
        enOferta.setCodigo("PR-1");
        enOferta.setNombre("Producto en oferta");
        enOferta.setPrecio(10000.0);
        enOferta.setEnOferta(true);
        enOferta.setPorcOferta(20);

        Producto normal = new Producto();
        normal.setCodigo("PR-2");
        normal.setNombre("Producto normal");
        normal.setPrecio(5000.0);
        normal.setEnOferta(false);
        normal.setPorcOferta(0);

        List<String> codigos = List.of("PR-1", "PR-2");
        when(productoRepo.findByCodigoIn(codigos)).thenReturn(List.of(enOferta, normal));

        List<ProdAgregarDTO> resultado = productoService.obtenerPorCodigos(codigos);

        assertEquals(2, resultado.size());
        assertEquals(8000.0, resultado.get(0).getPrecioFinal(), 0.01);  // 10000 con 20% de descuento
        assertEquals(5000.0, resultado.get(1).getPrecioFinal(), 0.01);  // sin oferta, precio normal
    }
}