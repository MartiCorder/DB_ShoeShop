package cat.uvic.teknos.shoeshop.services.exception;

public class RequestRouterImplException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RequestRouterImplException() {
    }

    public RequestRouterImplException(String message) {
        super(message);
    }

    public RequestRouterImplException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestRouterImplException(Throwable cause) {
        super(cause);
    }

    public RequestRouterImplException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

