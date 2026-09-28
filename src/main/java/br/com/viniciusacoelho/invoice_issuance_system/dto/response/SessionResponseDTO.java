package br.com.viniciusacoelho.invoice_issuance_system.dto.response;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;

@Builder
public record SessionResponseDTO(

        @NotNull(message = "O login é obrigatório.")
        String login,

        @NotNull(message = "O token é obrigatório.")
        String token

) {

}

// TODO:
//package br.com.viniciusacoelho.invoice_issuance_system.dto;
//
//import jakarta.validation.constraints.NotNull;
//
//import lombok.Builder;
//
//@Builder
//public record SessionResponseDTO(String login, String token) {
//
//}