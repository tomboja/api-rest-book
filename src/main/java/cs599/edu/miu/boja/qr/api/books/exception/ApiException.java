package cs599.edu.miu.boja.qr.api.books.exception;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 8/16/25
 */

// Base exception class
public interface ApiException {
    int getStatusCode();
    String getErrorCode();
}
