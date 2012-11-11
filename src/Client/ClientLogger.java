package Client;

import java.io.IOException;
import java.util.logging.*;


public final class ClientLogger {

    private static Logger logger;
    private static Handler handler;

    public static final int INFO    = 1;
    public static final int WARNING = 2;
    public static final int ERROR   = 3;

    static {

        try {
            logger = Logger.getLogger("Client");
            handler = new FileHandler("log/error.log", true);
            handler.setFormatter( new SimpleFormatter() );
            logger.addHandler(handler);
        } catch (IOException e) {
            System.err.println("Logování do souboru je zakázáno.");
        }

    }

    public static void log(String message, int type) {
        Level realType;

        switch (type) {
            case INFO:    { realType = Level.INFO; break; }
            case WARNING: { realType = Level.WARNING; break; }
            case ERROR:   { realType = Level.SEVERE; break; }
            default: realType = Level.INFO;
        }

        logger.log(realType, message);
    }

    public static void log(String message) {
        log(message, INFO);
    }

    public static void quit() {
        handler.close();
    }

}
