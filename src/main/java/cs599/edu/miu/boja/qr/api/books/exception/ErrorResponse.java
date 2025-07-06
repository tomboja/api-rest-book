package cs599.edu.miu.boja.qr.api.books.exception;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {
    private String message;
    private Instant timestamp;
    private int status;
    private String resource;

    public ErrorResponse(String message, int status, String resource) {
        this.message = message;
        this.timestamp = Instant.now();
        this.status = status;
        this.resource = resource;
    }
}