package Client;

import javax.swing.UIManager;


/*
 *  RFC 1459 (Internet Relay Chat: Protocol)
 *  RFC 2810 (Internet Relay Chat: Architecture)
 *  RFC 2811 (Internet Relay Chat: Channel Management)
 *  RFC 2812 (Internet Relay Chat: Client Protocol)
 *  RFC 2813 (Internet Relay Chat: Server Protocol)
 */
public class Client {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            Client.run();
        } catch (Exception e) {
            ClientLogger.log("Chyba při běhu programu: " + e.getMessage(), ClientLogger.ERROR);
        }
    }

    public static void run() {
        MainWindow.getInstance().validate();
    }

    public static void terminate(int returnCode) {

        if (returnCode > 0) {
            ClientLogger.log("Program byl násilně ukončen.", ClientLogger.ERROR);
            ClientLogger.quit();
            System.exit(returnCode);
        } else {
            boolean close = MessageDialog.confirmQuestion("Ukončit aplikaci",
                                                "Opravdu chcete ukončit aplikaci?");
            if (close) {
                ClientLogger.quit();
                System.exit(0);
            }
        }

    }

}
