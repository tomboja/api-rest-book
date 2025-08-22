package cs599.edu.miu.boja.qr.api.books.exception;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 8/17/25
 */

public class ConstraintViolationException extends BaseApiException {
    public ConstraintViolationException(String message, String errorCode) {
        super(message, 409, "CONSTRAINT_VIOLATION");
    }
}
