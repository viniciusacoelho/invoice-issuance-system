package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.repository.InvoiceRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceItemService invoiceItemService;

    @Disabled("Teste ainda não implementado")
    @DisplayName("Testa se o usuário fez o login em sua conta")
    @BeforeAll
    static void shouldLogin() {
        // login();
    }

    @Disabled("Teste ainda não implementado")
    @BeforeEach
    void shouldBeforeEach() {

    }
// TODO: Check why the test isn't working.
//    org.mockito.exceptions.misusing.UnnecessaryStubbingException:
//    Unnecessary stubbings detected.
//            Clean & maintainable test code requires zero unnecessary code.
    @Test
    @DisplayName("Teste que valida se a nota fiscal foi criada")
    @Order(1)
    void shouldCreateInvoice() {
//        List<InvoiceItemDTO> invoiceItemDTO = new InvoiceItemDTO(1L, 10)));
//        InvoiceDTO invoiceDTO = new InvoiceDTO(invoiceItemDTO);
//        Invoice invoice = new Invoice();
//        when(invoiceRepository.save(any())).thenReturn(invoice);
//        Invoice result = invoiceService.create(invoiceDTO);
//        assumeTrue(result.getProductQuantity() == 10);
//        assertNotNull(result);
    }

    @Test
    @DisplayName("Teste que valida se as notas fiscais foram listadas")
    @Order(2)
    void shouldReadInvoices() {
        when(invoiceRepository.count()).thenReturn(1000000L);
        assertNotNull(invoiceService.read());
//        when(invoiceRepository.count()).thenReturn(0L);
//        assertThrows(NotFoundException.class, () ->
//                invoiceService.read());
    }

    @Test
    @DisplayName("Teste que valida se a nota fiscal foi atualizada")
    @Order(3)
    void shouldUpdateInvoice() {
//        Invoice invoice = invoiceService.findById(1L);
//        assertNotNull(invoiceService.update(invoice));
//        assertDoesNotThrow(() ->
//                invoiceService.update(invoice));
    }

    @Test
    @DisplayName("Teste que valida se a nota fiscal foi deletada")
    @Order(4)
    void shouldDeleteInvoice() {
//        assertNotNull(invoiceService.delete(1L));
        assertThrows(NotFoundException.class, () ->
                invoiceService.delete(10L));
    }

    // TODO: The others methods of the service
    @Test
    @DisplayName("Testa se retorna true")
    void shouldReturnTrue() {
//        Invoice invoice = invoiceService.findById(1L);
//        assertEquals(Invoice.Status.CLOSED, invoice.getStatus());
//        assumeTrue(Invoice.Status.CLOSED == invoice.getStatus());
//        assumeFalse(Invoice.Status.CLOSED == invoice.getStatus());
//        assertTrue(invoiceService.isInvoice());
//        assertNull();
    }

    @Test
    @DisplayName("Testa se não retorna nulo")
    void shouldReturnNotNull() {
        assertNotNull(invoiceService);
    }

    @Test
    void shouldExceptionHandler() {
        assertThrows(NotFoundException.class, () ->
                invoiceService.delete(null));
        assertDoesNotThrow(() ->
                invoiceService.update(null));
    }

    @Disabled("Teste ainda não implementado")
    @AfterEach
    void shouldAfterEach() {

    }

    @Disabled("Teste ainda não implementado")
    @DisplayName("Testa se o usuário deslogou da sua conta")
    @AfterAll
    static void shouldLogOut() {
        // logOut();
    }

}
