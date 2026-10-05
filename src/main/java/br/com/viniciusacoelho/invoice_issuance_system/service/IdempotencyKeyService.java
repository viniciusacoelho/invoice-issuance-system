package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.enums.IdempotencyKeyStatus;
import br.com.viniciusacoelho.invoice_issuance_system.exception.OperationAlreadyInProgressException;
import br.com.viniciusacoelho.invoice_issuance_system.model.IdempotencyKey;
import br.com.viniciusacoelho.invoice_issuance_system.repository.IdempotencyKeyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class IdempotencyKeyService {

    @Autowired
    private IdempotencyKeyRepository idempotencyKeyRepository;

    public IdempotencyKey create(IdempotencyKey idempotencyKey) {
        return idempotencyKeyRepository.save(idempotencyKey);
    }

    public IdempotencyKey update(IdempotencyKey idempotencyKey, Long invoiceId) {
        idempotencyKey.setStatus(IdempotencyKeyStatus.COMPLETED);
        idempotencyKey.setInvoiceId(invoiceId);
        return idempotencyKeyRepository.save(idempotencyKey);
    }

    public Optional<IdempotencyKey> findById(String key) {
        return idempotencyKeyRepository.findById(key);
    }

    @Transactional
    public IdempotencyKey reserve(String key) {
        IdempotencyKey idempotencyKey = IdempotencyKey.builder()
                .key(key)
                .status(IdempotencyKeyStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .build();
        try {
            idempotencyKeyRepository.saveAndFlush(idempotencyKey);
        } catch (DataIntegrityViolationException e) {
            throw new OperationAlreadyInProgressException();
        }
        return idempotencyKey;
    }

    public static boolean isStatusProcessing(IdempotencyKeyStatus status) {
        return status == IdempotencyKeyStatus.PROCESSING;
    }

    public static boolean isStatusCompleted(IdempotencyKeyStatus status) {
        return status == IdempotencyKeyStatus.COMPLETED;
    }

}
