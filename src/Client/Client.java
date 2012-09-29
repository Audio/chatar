package Client;

/**
 * Kostra aplikace. Dává pokyn k vytvoření grafického rozhraní a
 * inicializaci loggeru. Ukončuje program.
 *
 * Aplikace se řídí standardy:
 *  RFC 1459 (Internet Relay Chat Protocol)
 *  RFC 2810 (Internet Relay Chat: Architecture)
 *  RFC 2811 (Internet Relay Chat: Channel Management)
 *  RFC 2812 (Internet Relay Chat: Client Protocol)
 *  RFC 2813 (Internet Relay Chat: Server Protocol)
 *
 *
 * Aktuální (školní) revize: 30. Celková revize 58.
 * Generováno 19.5.2009 21:49.
 *
 *
 * @author Martin Fouček
 */
public class Client {

    /**
     * Počáteční nastavení - registrace loggeru.
     */
    public static void init () {
        ClientLogger.enable();
    }

    /**
     * Vytvoří grafické rozhraní. Spustí aplikaci.
     * Překreslí formulář kvůli správnému zobrazení.
     */
    public static void run () {

        init();
        GUI.prepareForm();
        GUI.getForm().validate();

    }

    /**
     * Okamžitě ukončí aplikaci.
     */
    public static void terminate () {
        Client.terminate(1);
    }

    /**
     * Po potvrzení dialogového okna ukončí aplikaci. Také ukončuje logovaní zpráv.
     *
     * @param returnCode
     */
    public static void terminate (int returnCode) {

        if (returnCode > 0) {
            ClientLogger.log("Program byl násilně ukončen.", ClientLogger.ERROR);
            ClientLogger.quit();
            System.exit(returnCode);
        }
        else {
            GWindow window = new GWindow(GWindow.GROUP_CONFIRM, GWindow.TYPE_QUESTION,
                    "Ukončit program", "Opravdu chcete ukončit tento nádherný program?");
            if (window.confirm == 0) {
                ClientLogger.quit();
                System.exit(0);
            }
        }

    }

}

