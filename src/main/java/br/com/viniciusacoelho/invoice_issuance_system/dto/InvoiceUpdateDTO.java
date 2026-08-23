package br.com.viniciusacoelho.invoice_issuance_system.dto;

import br.com.viniciusacoelho.invoice_issuance_system.model.Product;

import jakarta.validation.constraints.Positive;

import java.util.List;

public record InvoiceUpdateDTO(

        @Positive(message = "A quantidade do produto deve ser maior que zero.")
        Integer productQuantity,

        List<Product> products

) {

}
