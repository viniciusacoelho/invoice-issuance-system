package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateDTO(

        @Size(min = 3, max = 50, message = "Nome deve ter no mínimo {min} e no máximo {max} caracteres.")
        String name,

        @Size(min = 3, max = 50, message = "Descrição deve ter no mínimo {min} e no máximo {max} caracteres.")
        String description,

        @Positive(message = "Preço não deve ser negativo.")
        BigDecimal price,

        @Positive(message = "Estoque não deve ser negativo.")
        Integer stock,

        @Size(min = 3, max = 50, message = "Categoria deve ter no mínimo {min} e no máximo {max} caracteres.")
        String category


) {

}
