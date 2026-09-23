package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public record CustomerDTO(

        @NotNull(message = "O nome é obrigatório.")
        @Size(min = 3, max = 50, message = "O nome deve ter entre {min} e {max} caracteres.")
        String name,

        @NotNull(message = "O e-mail é obrigatório.")
        @Size(min = 3, max = 50, message = "O e-mail deve ter entre {min} e {max} caracteres.")
        @Email(message = "O e-mail deve ser válido.")
        String email,

        @NotNull(message = "O telefone é obrigatório.")
        String phone,

        @NotNull(message = "O CPF é obrigatório.")
        @CPF(message = "O CPF deve ser válido.")
        String cpf,

        @NotNull(message = "O CNPJ é obrigatório.")
        @CNPJ(message = "O CNPJ deve ser válido.")
        String cnpj,

        @NotNull(message = "O CEP é obrigatório.")
        String cep

) {

}
