package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductUpdateDTO(

        @Size(min = 3, max = 50, message = "Nome deve ter no mínimo {min} e no máximo {max} caracteres.")
        String name,

        @Size(min = 3, max = 50, message = "Descrição deve ter no mínimo {min} e no máximo {max} caracteres.")
        String description,

        @Positive(message = "Saldo não deve ser negativo.")
        Integer stock

) {

}
