package Connection;

/**
 * Vlastní typ výjimky, vyhozený při chybném spojení. Standardní zpracování.
 *
 * @author Martin Fouček
 */
public class ConnectionException extends Exception {

    /**
     * Zpracování všeho, co se dá vyhodit.
     *
     * @param cause
     */
    public ConnectionException (Throwable cause) {
        super(cause);
    }

    /**
     * Zpracování výjimky.
     *
     * @param str
     */
    public ConnectionException (String str) {
        super(str);
    }

}
