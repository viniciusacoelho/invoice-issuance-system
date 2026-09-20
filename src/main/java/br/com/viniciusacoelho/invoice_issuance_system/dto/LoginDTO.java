package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

// Classe que receberá os dados para a realização do Login na aplicação
@Data
public class LoginDTO {

    @NotNull(message = "O usuário é obrigatório.")
    private String username;

    @NotNull(message = "A senha é obrigatória.")
    private String password;

}
