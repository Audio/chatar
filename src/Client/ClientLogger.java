package Client;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Staticka trida ClientLogger zaznamenava udalosti do logu.
 * Pro logovani (zaznam zprav) vyuziva API JDK. Zpravy uklada
 * na souborovy system do souboru log.txt.
 * 
 * Rozlisuje tri typy zprav: informace, varovani a chyby,
 * k nimz doslo pri behu programu.
 *
 * @author Martin Fouček
 */
public class ClientLogger {

    private static Logger logger;
    private static Handler handler;
    private static boolean enabled;

    // typy zprav
    /**
     * Informace.
     */
    public static final int INFO    = 1;
    /**
     * Varování.
     */
    public static final int WARNING = 2;
    /**
     * Chyba.
     */
    public static final int ERROR   = 3;

    /**
     * Nastavi logger a handler (pred prvnim pouzitim).
     */
    static {

        try {
            logger = Logger.getLogger("Client");
            handler = new FileHandler("log.txt", true);
            handler.setFormatter( new SimpleFormatter() );
            logger.addHandler(handler);
        }
        catch (IOException e) { /* Neni kam dal oznamit vyjimku. */ }

    }

    /**
     * Nastavuje logovani.
     */
    public static void enable () {
        enabled = true;
    }

    /**
     * Vypina logovani.
     */
    public static void disable () {
        enabled = false;
    }

    /**
     * Singalizace, zda je logovani povoleno.
     *
     * @return
     */
    public static boolean isEnabled () {
        return enabled;
    }

    /**
     * Zaznamenava zpravy.
     * Varovani: logovani musi byt povoleno metodou enable.
     *
     * @param message text zpravy
     * @param type typ zpravy (konstanta)
     */
    public static void log (String message, int type) {

        if ( !isEnabled() )
            return;

        Level realType;

        switch (type) {
            case INFO:    { realType = Level.INFO; break; }
            case WARNING: { realType = Level.WARNING; break; }
            case ERROR:   { realType = Level.SEVERE; break; }
            default: realType = Level.INFO;
        }

        logger.log(realType, message);

    }

    /**
     * Logovani - bez udani typu. Za typ se defaultne povazuje INFO.
     *
     * @param message
     */
    public static void log (String message) {
        log(message, INFO);
    }

    /**
     * Uzavira handler pri ukonceni programu (~ finalize).
     */
    public static void quit () {
        handler.close();
    }

}
