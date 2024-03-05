package net.octoberserver.ordersystem;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ErrorResponse> handleResponseStatusException(Exception e) {
        if (e instanceof ResponseStatusException rse) {
            final var status = rse.getStatusCode();
            return new ResponseEntity<>(new ErrorResponse(status.value(), rse.getReason()), status);
        }

        final var status = HttpStatus.INTERNAL_SERVER_ERROR;
        final var message = "Internal Server Error, Exception type: " + e.getClass().getName();
        return new ResponseEntity<>(new ErrorResponse(status.value(), message), status);
    }
}
