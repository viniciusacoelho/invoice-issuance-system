package br.com.viniciusacoelho.invoice_issuance_system.model;

import br.com.viniciusacoelho.invoice_issuance_system.enums.IdempotencyKeyStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_key")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdempotencyKey {

    @Id
    private String key;

    @Enumerated(EnumType.STRING)
    private IdempotencyKeyStatus status;

    private Long invoiceId;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

}
