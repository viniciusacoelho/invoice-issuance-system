package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateDTO(

        @Size(min = 3, max = 50, message = "O nome deve ter entre {min} e {max} caracteres.")
        String name,

        @Size(min = 3, max = 1000, message = "A descrição deve ter entre 3 e 1000 caracteres.")
        String description,

        @Positive(message = "O preço deve ser maior que zero.")
        BigDecimal price,

        @PositiveOrZero(message = "O estoque deve ser maior ou igual a zero.")
        Integer stock,

        @Size(min = 3, max = 50, message = "A categoria deve ter entre {min} e {max} caracteres.")
        String category

) {

}
