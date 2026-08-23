package br.com.viniciusacoelho.invoice_issuance_system.dto;

import br.com.viniciusacoelho.invoice_issuance_system.model.Product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record InvoiceDTO(

        @NotNull(message = "A quantidade do produto é obrigatória.")
        @Positive(message = "A quantidade do produto deve ser maior que zero.")
        Integer productQuantity,

        @NotNull(message = "A lista de produtos é obrigatória.")
        List<Product> products

) {

}
