package cs599.edu.miu.boja.qr.api.books.exception;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 7/5/25
 */

public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(String message) {
        super(message);
    }

    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
