package Client;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class ClientLoggerTest {

    /**
     * Test.
     */
    @Test
    public void testEnable() {
        System.out.println("enable");
        ClientLogger.enable();
    }

    /**
     * Test.
     */
    @Test
    public void testDisable() {
        System.out.println("disable");
        ClientLogger.disable();
    }

    /**
     * Test.
     */
    @Test
    public void testIsEnabled() {
        System.out.println("isEnabled");
        boolean expResult = false;
        boolean result = ClientLogger.isEnabled();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testLog_String_int() {
        System.out.println("log");
        String message = "test";
        int type = ClientLogger.INFO;
        ClientLogger.log(message, type);
    }

    /**
     * Test.
     */
    @Test
    public void testLog_String() {
        System.out.println("log");
        String message = "logovani";
        ClientLogger.log(message);
    }

    /**
     * Test.
     */
    @Test
    public void testQuit() {
        System.out.println("quit");
        ClientLogger.quit();
    }

}