package br.com.viniciusacoelho.invoice_issuance_system.exception;

public class OperationAlreadyInProgressException extends RuntimeException {

    public OperationAlreadyInProgressException() {
        super("Operação já está sendo processada.");
    }

}
