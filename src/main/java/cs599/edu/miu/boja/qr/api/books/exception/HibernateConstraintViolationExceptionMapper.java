package cs599.edu.miu.boja.qr.api.books.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class HibernateConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        log.error("Hibernate constraint violation: {}", exception.getMessage(), exception);

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
}