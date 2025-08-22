package cs599.edu.miu.boja.qr.api.books.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {
    @Override
    public Response toResponse(ResourceNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(Response.Status.NOT_FOUND.getStatusCode())
                .errorCode(ex.getErrorCode())
                .build();
        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}