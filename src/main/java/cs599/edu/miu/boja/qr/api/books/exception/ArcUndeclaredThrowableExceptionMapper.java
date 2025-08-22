package cs599.edu.miu.boja.qr.api.books.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import io.quarkus.arc.ArcUndeclaredThrowableException;
import org.hibernate.exception.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class ArcUndeclaredThrowableExceptionMapper implements ExceptionMapper<ArcUndeclaredThrowableException> {
    @Override
    public Response toResponse(ArcUndeclaredThrowableException exception) {
        Throwable cause = exception.getCause();
        while (cause != null) {
            if (cause instanceof ConstraintViolationException) {
                log.error("ArcUndeclaredThrowableException constraint violation: {}", cause.getMessage(), cause);
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .message("Duplicate ISBN or other constraint violation")
                        .status(Response.Status.CONFLICT.getStatusCode())
                        .errorCode("CONSTRAINT_VIOLATION")
                        .resource("Book")
                        .build();
                return Response.status(Response.Status.CONFLICT)
                        .entity(errorResponse)
                        .build();
            }
            cause = cause.getCause();
        }
        log.error("ArcUndeclaredThrowableException: {}", exception.getMessage(), exception);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Unexpected error")
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .errorCode("UNDECLARED_THROWABLE")
                .resource("Book")
                .build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}