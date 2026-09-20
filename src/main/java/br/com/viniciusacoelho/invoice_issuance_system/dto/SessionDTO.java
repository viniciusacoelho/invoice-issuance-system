package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

// Classe que representa uma sessão do sistema contendo o token gerado.
@Data
@Builder
public class SessionDTO {

    @NotNull(message = "O login é obrigatório.")
    private String login;

    @NotNull(message = "O token é obrigatório.")
    private String token;

}
