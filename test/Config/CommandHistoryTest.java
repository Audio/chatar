package Config;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class CommandHistoryTest {

    /**
     * Test.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        String command = "";
        CommandHistory.add(command);
    }

    /**
     * Test.
     */
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        int expResult = 1;
        int result = CommandHistory.getSize();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testGetOlder() {
        System.out.println("getOlder");
        String expResult = "";
        String result = CommandHistory.getOlder();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testGetNewer() {
        System.out.println("getNewer");
        String expResult = "";
        String result = CommandHistory.getNewer();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testGetString() {
        System.out.println("getString");
        String expResult = "[]";
        String result = CommandHistory.getString();
        assertEquals(expResult, result);
    }

}