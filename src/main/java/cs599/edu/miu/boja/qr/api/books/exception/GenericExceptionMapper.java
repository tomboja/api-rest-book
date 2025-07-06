package cs599.edu.miu.boja.qr.api.books.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 7/5/25
 */

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred",
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                exception.getClass().getSimpleName()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}
