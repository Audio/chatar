package Config;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class ConfigTest {

    /**
     * Test.
     */
    @Test
    public void testRandom() {
        System.out.println("random");
        Config instance = new Config();
        int result = instance.random();
        assertTrue(result > 10 && result < 100);
    }

    /**
     * Test.
     */
    @Test
    public void testLoadFromFile() {
        System.out.println("loadFromFile");
        Config instance = new Config();
        instance.loadFromFile();
    }

    /**
     * Test.
     */
    @Test
    public void testSaveToFile() {
        System.out.println("saveToFile");
        Config instance = new Config();
        instance.saveToFile();
    }

}