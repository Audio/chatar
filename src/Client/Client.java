package Client;

/**
 * Aplikace se řídí standardy:
 *  RFC 1459 (Internet Relay Chat Protocol)
 *  RFC 2810 (Internet Relay Chat: Architecture)
 *  RFC 2811 (Internet Relay Chat: Channel Management)
 *  RFC 2812 (Internet Relay Chat: Client Protocol)
 *  RFC 2813 (Internet Relay Chat: Server Protocol)
 *
 */
public class Client {

    public static void main(String[] args) {

        try {
            Client.run();
        } catch (Exception e) {
            ClientLogger.log("Chyba při běhu programu: " + e.getMessage(), ClientLogger.ERROR);
        }

    }

    public static void init() {
        ClientLogger.enable();
    }

    /**
     * Vytvoří grafické rozhraní. Spustí aplikaci.
     * Překreslí formulář kvůli správnému zobrazení.
     */
    public static void run() {
        init();
        GUI.prepareForm();
        GUI.getForm().validate();
    }

    public static void terminate() {
        Client.terminate(1);
    }

    public static void terminate(int returnCode) {

        if (returnCode > 0) {
            ClientLogger.log("Program byl násilně ukončen.", ClientLogger.ERROR);
            ClientLogger.quit();
            System.exit(returnCode);
        }
        else {
            GWindow window = new GWindow(GWindow.GROUP_CONFIRM, GWindow.TYPE_QUESTION,
                    "Ukončit aplikaci", "Opravdu chcete ukončit aplikaci?");
            if (window.confirm == 0) {
                ClientLogger.quit();
                System.exit(0);
            }
        }

    }

}
