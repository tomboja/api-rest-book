package cs599.edu.miu.boja.qr.api.books.exception;

public class ResourceNotFoundException extends BaseApiException {
    public ResourceNotFoundException(String message) {
        super(message, 404, "RESOURCE_NOT_FOUND");
    }
}