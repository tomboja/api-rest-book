package cs599.edu.miu.boja.qr.api.books.exception;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@With
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private String message;
    private int status;
    private String resource;
    private String errorCode;

    @Builder.Default
    private Instant timestamp = Instant.now();
}