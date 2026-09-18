package br.com.viniciusacoelho.invoice_issuance_system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserUpdateDTO(

        @Size(min = 3, max = 50, message = "O nome deve ter entre {min} e {max} caracteres.")
        String name,

        @Size(min = 3, max = 50, message = "O e-mail deve ter entre {min} e {max} caracteres.")
        String email,

        @Size(min = 3, max = 50, message = "O usuário deve ter entre {min} e {max} caracteres.")
        String username,

        @CPF(message = "")
        String cpf,

        @NotNull(message = "O CEP é obrigatório.")
        String cep,

        @PastOrPresent(message = "A data deve estar no passado ou presente.")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthDate,

        @Size(min = 8, max = 50, message = "A senha deve ter entre {min} e {max} caracteres.")
        String password

) {

}
