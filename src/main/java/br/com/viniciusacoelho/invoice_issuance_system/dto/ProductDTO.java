package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductDTO(

        @NotNull(message = "O nome é obrigatório.")
        @Size(min = 3, max = 50, message = "O nome deve ter entre {min} e {max} caracteres.")
        String name,

        @NotNull(message = "A descrição é obrigatória.")
        @Size(min = 3, max = 1000, message = "A descrição deve ter entre 3 e 1000 caracteres.")
        String description,

        @NotNull(message = "O preço é obrigatório.")
        @Positive(message = "O preço deve ser maior que zero.")
        BigDecimal price,

        @NotNull(message = "O estoque é obrigatório.")
        @PositiveOrZero(message = "O estoque deve ser maior ou igual a zero.")
        Integer stock,

        @NotNull(message = "A categoria é obrigatória")
        @Size(min = 3, max = 50, message = "A categoria deve ter entre {min} e {max} caracteres.")
        String category

) {

}
