package Client;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.*;


public final class ClientLogger {

    private static Logger logger;
    private static Handler handler;
    private static boolean loggingEnabled;

    public static final int INFO    = 1;
    public static final int WARNING = 2;
    public static final int ERROR   = 3;

    static {
        initialize();
    }

    private static void initialize() {
        Path logDir = FileSystems.getDefault().getPath("./log");
        try {
            if ( !Files.isDirectory(logDir) )
                Files.createDirectory(logDir);

            logger = Logger.getLogger("Client");
            handler = new FileHandler("log/error.log", true);
            handler.setFormatter( new SimpleFormatter() );
            logger.addHandler(handler);
            loggingEnabled = true;
        } catch (IOException e) {
            loggingEnabled = false;
            System.err.println("Logování do souboru je zakázáno.");
        }
    }

    public static void log(String message, int type) {
        if (!loggingEnabled)
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

    public static void log(String message) {
        log(message, INFO);
    }

    public static void quit() {
        if (loggingEnabled)
            handler.close();
    }

}
