package br.com.viniciusacoelho.invoice_issuance_system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserUpdateDTO(

        @Size(min = 3, max = 50, message = "O nome deve ter entre {min} e {max} caracteres.")
        String name,

        @Size(min = 3, max = 50, message = "O e-mail deve ter entre {min} e {max} caracteres.")
        @Email(message = "O e-mail deve ser válido.")
        String email,

        @Size(min = 3, max = 50, message = "O usuário deve ter entre {min} e {max} caracteres.")
        String username,

        @PastOrPresent(message = "A data deve estar no passado ou presente.")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthDate,

        @Size(min = 8, max = 50, message = "A senha deve ter entre {min} e {max} caracteres.")
        String password

) {

}
