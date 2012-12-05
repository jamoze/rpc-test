package net.jamosa.ixtens.test.core.exceptions;

public class ServerException extends Exception {

    private static final long serialVersionUID = -6296326607544459638L;

    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }
}
