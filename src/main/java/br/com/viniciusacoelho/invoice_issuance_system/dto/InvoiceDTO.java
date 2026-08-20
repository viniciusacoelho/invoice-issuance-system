package br.com.viniciusacoelho.invoice_issuance_system.dto;

import br.com.viniciusacoelho.invoice_issuance_system.model.Product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record InvoiceDTO(

        @NotNull(message = "Número sequencial não deve ser vazio.")
        @Positive(message = "Número sequencial não deve ser negativo.")
        long sequentialNumber,

        @NotNull(message = "Produtos não devem ser vazios.")
        List<Product> products

) {

}
