package Client;

/**
 * Vlastní typ výjimky, vyhozený při chybě programu. Standardní zpracování.
 *
 * @author Martin Fouček
 */
public class ClientException extends Exception {

    /**
     * Zpracování výjimky.
     *
     * @param str
     */
    public ClientException (String str) {
        super(str);
        ClientLogger.log("ClientException: " + str, ClientLogger.ERROR);
    }

    /**
     * Zpracování všeho, co se dá vyhodit.
     *
     * @param obj
     */
    public ClientException (Throwable obj) {
        super(obj);
        ClientLogger.log("ClientException: " + obj.toString(), ClientLogger.ERROR);
    }

}
