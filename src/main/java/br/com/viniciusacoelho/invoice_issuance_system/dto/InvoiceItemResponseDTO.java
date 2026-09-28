package br.com.viniciusacoelho.invoice_issuance_system.dto;

import java.math.BigDecimal;

public record InvoiceItemResponseDTO(Long id, String name, BigDecimal price, int quantity) {

}
