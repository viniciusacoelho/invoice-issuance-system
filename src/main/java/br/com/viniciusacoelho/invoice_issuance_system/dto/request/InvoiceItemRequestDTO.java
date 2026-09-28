package br.com.viniciusacoelho.invoice_issuance_system.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InvoiceItemRequestDTO(

        @NotNull(message = "O produto é obrigatório.")
        Long productId,

        @NotNull(message = "A quantidade do produto é obrigatória.")
        @Positive(message = "A quantidade do produto deve ser maior que zero.")
        int productQuantity

) {

}
