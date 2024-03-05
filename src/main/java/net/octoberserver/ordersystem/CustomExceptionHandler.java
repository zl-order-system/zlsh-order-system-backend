package net.octoberserver.ordersystem;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        if (ex instanceof MethodArgumentNotValidException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        final var status = HttpStatus.INTERNAL_SERVER_ERROR;
        final var message = "Internal Server Error, Exception type: " + ex.getClass().getName();
        return new ResponseEntity<>(new ErrorResponse(status.value(), message), status);
    }
}
