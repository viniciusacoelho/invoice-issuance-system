package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;

@Builder
public record SessionDTO(

        @NotNull(message = "O login é obrigatório.")
        String login,

        @NotNull(message = "O token é obrigatório.")
        String token

) {

}
