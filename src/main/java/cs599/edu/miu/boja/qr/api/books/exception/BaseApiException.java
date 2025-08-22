package cs599.edu.miu.boja.qr.api.books.exception;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 8/16/25
 */

public abstract class BaseApiException extends RuntimeException implements ApiException {
    private final int statusCode;
    private final String errorCode;

    public BaseApiException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public BaseApiException(String message, Throwable cause, int statusCode, String errorCode) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
