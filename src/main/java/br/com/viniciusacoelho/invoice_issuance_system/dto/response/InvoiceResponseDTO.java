package br.com.viniciusacoelho.invoice_issuance_system.dto.response;

import br.com.viniciusacoelho.invoice_issuance_system.enums.InvoiceStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceResponseDTO {

    private Long id;

    private InvoiceStatus status;

    private String customerName;

    private List<InvoiceItemResponseDTO> invoiceItems;

    private BigDecimal totalPrice;

    private int totalQuantity;

    private String userName;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime issuedAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    public InvoiceResponseDTO(Long id, InvoiceStatus status, String customerName, BigDecimal totalPrice, int totalQuantity, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime issuedAt, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime createdAt, String userName) {
        this.id = id;
        this.status = status;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.issuedAt = issuedAt;
        this.createdAt = createdAt;
        this.userName = userName;
    }

}
