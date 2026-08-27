package br.com.viniciusacoelho.invoice_issuance_system.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String field) {
        super(field + " não encontrado.");
    }

}
