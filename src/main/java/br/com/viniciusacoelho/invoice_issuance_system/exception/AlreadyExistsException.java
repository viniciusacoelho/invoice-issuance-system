package br.com.viniciusacoelho.invoice_issuance_system.exception;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String field) {
        super(field + " já existe.");
    }

}
