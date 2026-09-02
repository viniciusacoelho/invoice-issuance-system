package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.ProductDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.ProductUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.repository.ProductRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Teste que valida se o produto foi criado")
    @Order(1)
    void shouldCreateProduct() {
        ProductDTO productDTO = new ProductDTO("Nome do Produto", "Descrição do Produto", new BigDecimal(1000), 100, "Categoria do produto");
        Product product = Product.builder()
                .name(productDTO.name())
                .description(productDTO.description())
                .stock(productDTO.stock())
                .build();
        when(productRepository.save(any())).thenReturn(product);
        Product result = productService.create(productDTO);
        assertNotNull(result);
        assertEquals(productDTO.name(), result.getName());
        assertEquals(productDTO.description(), result.getDescription());
        assertEquals(productDTO.stock(), result.getStock());
//        doThrow(NotFoundException.class).when(productRepository).save(any());
    }

    @Test
    @DisplayName("Teste que valida se os produtos foram listados")
    @Order(2)
    void shouldReadProduct() {
        when(productRepository.count()).thenReturn(1000000L);
        assertNotNull(productService.read());
//        when(productRepository.count()).thenReturn(0L);
//        assertThrows(NotFoundException.class, () ->
//                productService.read());
    }

    @Test
    @DisplayName("Teste que valida se o produto foi atualizado")
    @Order(4)
    void shouldUpdateProduct() {
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO("Nome do Produto", "Descrição do Produto", new BigDecimal(1000), 100, "Categoria do produto");
        Product product = Product.builder()
                .name(productUpdateDTO.name())
                .description(productUpdateDTO.description())
                .stock(productUpdateDTO.stock())
                .build();
        when(productRepository.save(any())).thenReturn(product);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
        Product result = productService.update(1L, productUpdateDTO);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Teste que valida se o produto foi deletado")
    @Order(4)
    void shouldDeleteProduct() {
//        when(productRepository.deleteById(1L)).thenThrow(new NotFoundException("Produto"));
//        doThrow(NotFoundException.class).when(productService).delete(100L);
//        doThrow(NotFoundException.class).when(productService).hasProduct(100L);
//        when(productRepository.deleteById(100L)).thenThrow(new NotFoundException("Produto"));
//        when(productService.findById(100L)).thenThrow(NotFoundException.class);
        when(productRepository.existsById(1L)).thenThrow(new NotFoundException("Produto"));
        assertThrows(NotFoundException.class, ()
                -> productService.delete(1L));
    }

    @Test
    @DisplayName("Teste que valida se o produto foi encontrado pelo identificador")
    void shouldFindProductById() {
        when(productRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () ->
                productService.findById(100L)
        );
    }

}
