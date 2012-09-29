package Connection;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class PrefixTest {

    /**
     * Test.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Prefix instance = new Prefix("xyx:ssd");
        String expResult = "xyx:ssd";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

}