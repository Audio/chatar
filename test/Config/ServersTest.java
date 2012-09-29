package Config;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class ServersTest {

    /**
     * Test.
     */
    @Test
    public void testAddServer() {
        System.out.println("addServer");
        String address = "";
        Servers.addServer(address);
    }

    /**
     * Test.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        Servers.reset();
    }

    /**
     * Test.
     */
    @Test
    public void testLoadFile() {
        System.out.println("loadFile");
        String[] result = Servers.loadFile();
        assertTrue(result.toString().length() >= 0);
    }

    /**
     * Test.
     */
    @Test
    public void testSaveFile() {
        System.out.println("saveFile");
        Servers.saveFile();
    }

}