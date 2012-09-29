package Client;

/**
 * Spustí aplikaci.
 *
 * @author Martin Fouček
 */
public class Main {

    /**
     * Spustí aplikaci Chatař - IRC klient.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Client.run();
        }
        catch (Exception e) {
            ClientLogger.log("Chyba při běhu programu: " + e.getMessage(), ClientLogger.ERROR);
        }
    }

}
