package Config;

import Client.ClientLogger;
import Client.MessageDialog;
import java.io.*;
import java.util.Properties;

/**
 * Práce se seznamem oblíbenách serverů. Modifikace seznamu se standardně
 * provádí přes grafické rozhraní ve třídě Client\GServers.
 *
 * @author Martin Fouček
 */
public class Servers {

    private final static String filename = "servers.ini";
    private static Properties props = new Properties();

    /**
     * Prida hodnotu z dialogoveho okna.
     * 
     * @param address
     */
    public static void addServer (String address) {
        String key = "server" + props.size();
        props.setProperty(key, address);
    }

    /**
     * Vycisti obsah objektu typu Properties.
     * Pouziva se pri novem vkladani hodnot pri ukladani souboru.
     */
    public static void reset () {
        props.clear();
    }

    /**
     * Nacte soubor se seznamem oblibenych serveru.
     *
     * @return
     */
    public static String[] loadFile () {

        try {
            FileInputStream in = new FileInputStream(filename);
            props.load(in);
            in.close();
        }
        catch (FileNotFoundException e) {
            // Soubor jeste nebyl vytvoren.
        }
        catch (IOException e) {
            ClientLogger.log(e.getMessage(), ClientLogger.ERROR);
            new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR,
                    "Chyba aplikace", "Konfigurační soubor není možné načíst.");
        }

        String[] array = (String[]) props.values().toArray( new String[ props.size() ] );
        return array;

    }

    /**
     * Soubor se vytvari automaticky, pokud neexistuje.
     */
    public static void saveFile () {

        try {
            FileOutputStream out = new FileOutputStream(filename);
            props.store(out, "Server list. Generated by IRC client application.\nDo not modify by hand!");
            out.close();
        }
        catch (IOException e) {
            ClientLogger.log(e.getMessage(), ClientLogger.ERROR);
            new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR,
                    "Chyba aplikace", "Konfigurační soubor není možné uložit.");
        }

    }

}
