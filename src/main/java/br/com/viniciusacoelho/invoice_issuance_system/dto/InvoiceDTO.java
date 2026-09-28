package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InvoiceDTO(

        @NotNull(message = "O usuário é obrigatório.")
        Long userId,

        @NotNull(message = "O cliente é obrigatório.")
        Long customerId,

        @NotNull(message = "O produto é obrigatório.")
        List<InvoiceItemDTO> invoiceItemsDTO

) {

}
