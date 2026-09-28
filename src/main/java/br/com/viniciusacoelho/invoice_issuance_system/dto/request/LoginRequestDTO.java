package br.com.viniciusacoelho.invoice_issuance_system.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(

        @NotNull(message = "O usuário é obrigatório.")
        String username,

        @NotNull(message = "A senha é obrigatória.")
        String password

) {

}
