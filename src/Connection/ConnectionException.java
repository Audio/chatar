package Connection;


public class ConnectionException extends Exception {

    public ConnectionException(Throwable cause) {
        super(cause);
    }

    public ConnectionException(String str) {
        super(str);
    }

}
