package Client;

import MainWindow.MainWindow;
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
            MainWindow.getInstance().setVisible(true);
        } catch (Exception e) {
            ClientLogger.log("Chyba při běhu programu: " + e.getMessage(), ClientLogger.ERROR);
        }
    }

}
