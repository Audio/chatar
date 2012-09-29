package Client;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class GWindowTest {

    /**
     * Test.
     */
    @Test
    public void testWindows () {
        assertNotNull(GWindow.GROUP_CONFIRM);
        assertNotNull(GWindow.GROUP_CONFIRM_CANCEL);
        assertNotNull(GWindow.GROUP_INPUT);
        assertNotNull(GWindow.GROUP_MESSAGE);
        assertNotNull(GWindow.TYPE_ERROR);
        assertNotNull(GWindow.TYPE_INFORM);
        assertNotNull(GWindow.TYPE_PLAIN);
        assertNotNull(GWindow.TYPE_QUESTION);
        assertNotNull(GWindow.TYPE_WARN);

        new GWindow(GWindow.TYPE_PLAIN, "Test");
        new GWindow(GWindow.TYPE_WARN, "Test");
        new GWindow(GWindow.TYPE_ERROR, "Test");
        new GWindow(GWindow.TYPE_INFORM, "Test");
        new GWindow(GWindow.TYPE_QUESTION, "Test", "Test");

    }

}