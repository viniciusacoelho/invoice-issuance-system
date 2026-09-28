package br.com.viniciusacoelho.invoice_issuance_system.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InvoiceRequestDTO(

        @NotNull(message = "O usuário é obrigatório.")
        Long userId,

        @NotNull(message = "O cliente é obrigatório.")
        Long customerId,

        @NotNull(message = "O produto é obrigatório.")
        List<InvoiceItemRequestDTO> invoiceItemsDTO

) {

}
