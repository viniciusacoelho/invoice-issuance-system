package br.com.viniciusacoelho.invoice_issuance_system.dto.response;

import lombok.Builder;

@Builder
public record SessionResponseDTO(String login, String token) {

}
