package br.com.viniciusacoelho.invoice_issuance_system.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponseDTO(

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime timestamp,

        Integer statusCode,

        String httpStatus,

        String error,

        String path

) {

}
