/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import java.awt.Component;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class GTabTest {

    /**
     * Test.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAddTab() throws Exception {
        System.out.println("addTab");
        GTab instance = new GTab(200, 100);
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testRemoveTab() {
        System.out.println("removeTab");
        Component c = null;
        GTab instance = new GTab(200, 100);
        instance.removeTab(c);
    }

}