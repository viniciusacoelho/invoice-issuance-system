package br.com.viniciusacoelho.invoice_issuance_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public record CustomerUpdateDTO(

        @Size(min = 3, max = 50, message = "O nome deve ter entre {min} e {max} caracteres.")
        String name,

        @Size(min = 3, max = 50, message = "O e-mail deve ter entre {min} e {max} caracteres.")
        @Email(message = "O e-mail deve ser válido.")
        String email,

        String phone,

        @CPF(message = "O CPF deve ser válido.")
        String cpf,

        @CNPJ(message = "O CNPJ deve ser válido.")
        String cnpj,

        String cep

) {

}
