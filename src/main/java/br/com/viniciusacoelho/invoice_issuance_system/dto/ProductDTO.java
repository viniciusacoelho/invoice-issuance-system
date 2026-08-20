package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDTO(

        @NotNull(message = "Nome não deve ser vazio.")
        @Size(min = 3, max = 50, message = "Nome deve ter no mínimo {min} e no máximo {max} caracteres.")
        String name,

        @NotNull(message = "Descrição não deve ser vazia.")
        @Size(min = 3, max = 50, message = "Descrição deve ter no mínimo {min} e no máximo {max} caracteres.")
        String description,

        @NotNull(message = "Estoque não deve ser vazio.")
        @Positive(message = "Estoque não deve ser negativo.")
        Integer stock

) {

}
