package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InvoiceDTO(

        @NotNull(message = "O produto é obrigatório.")
        List<InvoiceItemDTO> invoiceItemsDTO

) {

}
