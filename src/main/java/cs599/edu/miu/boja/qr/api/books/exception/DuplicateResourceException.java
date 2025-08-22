package cs599.edu.miu.boja.qr.api.books.exception;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 8/17/25
 */

public class DuplicateResourceException extends BaseApiException {
    public DuplicateResourceException(String message) {
        super(message, 409, "DUPLICATE_RESOURCE");
    }

    public DuplicateResourceException(String message, String errorCode) {
        super(message, 409, "DUPLICATE_RESOURCE");
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause, 409, "DUPLICATE_RESOURCE");
    }
}
