package cs599.edu.miu.boja.qr.api.books.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 8/17/25
 */

@Slf4j
@Provider
public class DuplicateResourceExceptionMapper implements ExceptionMapper<DuplicateResourceException> {

    @Override
    public Response toResponse(DuplicateResourceException e) {
        log.error("Duplicate resource exception occurred: {}", e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(Response.Status.CONFLICT.getStatusCode())
                .message(e.getMessage())
                .errorCode(e.getErrorCode())
                .resource("Book")
                .build();
        return Response.status(Response.Status.CONFLICT)
                .entity(errorResponse)
                .build();
    }
}
