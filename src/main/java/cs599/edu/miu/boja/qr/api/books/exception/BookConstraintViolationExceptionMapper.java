package cs599.edu.miu.boja.qr.api.books.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BookConstraintViolationExceptionMapper implements ExceptionMapper<BookConstraintViolationException> {
    @Override
    public Response toResponse(BookConstraintViolationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Constraint"
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }
}