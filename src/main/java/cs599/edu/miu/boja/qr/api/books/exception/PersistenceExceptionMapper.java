package cs599.edu.miu.boja.qr.api.books.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {
    @Override
    public Response toResponse(PersistenceException exception) {
        Throwable cause = exception.getCause();
        if (cause instanceof ConstraintViolationException) {
            log.error("Persistence constraint violation: {}", cause.getMessage(), cause);
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
        log.error("Persistence exception: {}", exception.getMessage(), exception);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Persistence error")
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .errorCode("PERSISTENCE_ERROR")
                .resource("Book")
                .build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}