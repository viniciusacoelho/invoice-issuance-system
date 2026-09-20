package br.com.viniciusacoelho.invoice_issuance_system.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Credenciais inválidas!");
    }

}
