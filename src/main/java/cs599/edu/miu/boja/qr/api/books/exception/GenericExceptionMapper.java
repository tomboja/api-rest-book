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
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(exception.getMessage())
                .message("An unexpected error occurred")
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .resource("Generic Exception")
                .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}
