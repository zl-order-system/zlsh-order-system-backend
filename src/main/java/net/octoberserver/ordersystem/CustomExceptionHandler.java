package net.octoberserver.ordersystem;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class CustomExceptionHandler {

    public record ErrorResponse(int status, String message, long timestamp) {
        public ErrorResponse(int status, String message) {
            this(status, message, System.currentTimeMillis());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(Exception ex) {
        if (ex instanceof ResponseStatusException e) {
            final var status = e.getStatusCode();
            return new ResponseEntity<>(new ErrorResponse(status.value(), e.getReason()), status);
        }

        if (ex instanceof BindException e1 && ex instanceof org.springframework.web.ErrorResponse e2) {
            return new ResponseEntity<>(new ErrorResponse(e2.getStatusCode().value(), e1.getMessage() + " Exception Classpath: " + ex.getClass().getName()), e2.getStatusCode());
        }

        final var status = HttpStatus.INTERNAL_SERVER_ERROR;
        final var message = "Internal Server Error, Exception type: " + ex.getClass().getName();
        return new ResponseEntity<>(new ErrorResponse(status.value(), message), status);
    }
}
