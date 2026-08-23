package br.com.viniciusacoelho.invoice_issuance_system.handler;

import br.com.viniciusacoelho.invoice_issuance_system.exception.BadRequestException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneral(Exception ex, WebRequest request) {
        return handle(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return handle(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleInvalidProductQuantityException(BadRequestException ex, WebRequest request) {
        return handle(ex, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> handle(Exception ex, HttpStatus httpStatus, WebRequest request) {
        ResponseError body = responseError(ex.getMessage(), httpStatus, request.getDescription(false).replace("uri=", ""));
        return handleExceptionInternal(ex, body, headers(), httpStatus, request);
    }

    private ResponseError responseError(String message, HttpStatus status, String path) {
        return ResponseError.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(status.value())
                .httpStatus(status.name())
                .error(message)
                .path(path)
                .build();
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
