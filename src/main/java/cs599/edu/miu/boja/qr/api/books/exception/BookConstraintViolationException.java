package cs599.edu.miu.boja.qr.api.books.exception;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 7/5/25
 */

public class BookConstraintViolationException extends RuntimeException {

    public BookConstraintViolationException(String message) {
        super(message);
    }

    public BookConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookConstraintViolationException(Throwable cause) {
        super(cause);
    }
}
