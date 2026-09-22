package br.com.viniciusacoelho.invoice_issuance_system.exception;

public class InvoiceCannotBeIssuedException extends RuntimeException {

    public InvoiceCannotBeIssuedException(String message) {
        super(message);
    }

}
