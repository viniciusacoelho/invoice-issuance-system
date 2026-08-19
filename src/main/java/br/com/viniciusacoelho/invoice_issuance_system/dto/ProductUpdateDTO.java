package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateDTO(

        @Positive(message = "Código não deve ser negativo.")
        Long code,

        @Size(min = 3, max = 50, message = "Nome deve ter no mínimo {min} e no máximo {max} caracteres.")
        String name,

        @Size(min = 3, max = 50, message = "Descrição deve ter no mínimo {min} e no máximo {max} caracteres.")
        String description,

        @Positive(message = "Quantidade não deve ser negativa.")
        Long quantity,

        @Positive(message = "Saldo não deve ser negativo.")
        BigDecimal balance

) {

}
