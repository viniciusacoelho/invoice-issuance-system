package br.com.viniciusacoelho.invoice_issuance_system.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String item) {
        super(item + " inválido.");
    }

}
