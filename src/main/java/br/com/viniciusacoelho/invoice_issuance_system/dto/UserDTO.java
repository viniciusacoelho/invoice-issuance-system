package br.com.viniciusacoelho.invoice_issuance_system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserDTO(

        @NotNull(message = "O nome é obrigatório.")
        @Size(min = 3, max = 50, message = "O nome deve ter entre {min} e {max} caracteres.")
        String name,

        @NotNull(message = "O e-mail é obrigatório.")
        @Size(min = 3, max = 50, message = "O e-mail deve ter entre {min} e {max} caracteres.")
        @Email(message = "O e-mail deve ser válido.")
        String email,

        @NotNull(message = "O usuário é obrigatório.")
        @Size(min = 3, max = 50, message = "O usuário deve ter entre {min} e {max} caracteres.")
        String username,

        @NotNull(message = "O CPF é obrigatório.")
        @CPF(message = "O CPF deve ser válido.")
        String cpf,

        @NotNull(message = "O CEP é obrigatório.")
        String cep,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthDate,

        @NotNull(message = "A senha é obrigatória.")
        @Size(min = 8, max = 50, message = "A senha deve ter entre {min} e {max} caracteres.")
        String password

) {

}
