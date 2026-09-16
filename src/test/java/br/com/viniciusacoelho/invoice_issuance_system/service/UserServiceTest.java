package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;

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
class UserServiceTest {

//    @Mock
//    private UserRepository userRepository;

//    @InjectMocks
//    private UserService userService;

    @Disabled("Teste ainda não implementado")
    @DisplayName("Teste que valida se o usuário foi logado")
    @BeforeAll
    static void shouldLogin() {
        // login();
    }

    @Disabled("Teste ainda não implementado")
    @BeforeEach
    void shouldBeforeEach() {

    }

    @Test
    @DisplayName("Teste que valida se o usuário foi criado")
    @Order(1)
    void shouldCreateInvoice() {

    }

    @Test
    @DisplayName("Teste que valida se os usuários foram listados")
    @Order(2)
    void shouldReadInvoices() {

    }

    @Test
    @DisplayName("Teste que valida se o usuário foi atualizado")
    @Order(3)
    void shouldUpdateInvoice() {

    }

    @Test
    @DisplayName("Teste que valida se o usuário foi deletado")
    @Order(4)
    void shouldDeleteInvoice() {

    }

    @Disabled("Teste ainda não implementado")
    @AfterEach
    void shouldAfterEach() {

    }

    @Disabled("Teste ainda não implementado")
    @DisplayName("Teste que valida se o usuário foi deslogado")
    @AfterAll
    static void shouldLogout() {
        // logout();
    }

}
