package Client;

import org.junit.Test;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class GMenuBarTest {

    /**
     * Test.
     */
    @Test
    public void testToggleDisconectFromAll() {
        System.out.println("toggleDisconectFromAll");
        boolean setVisible = false;
        GMenuBar instance = new GMenuBar();
        instance.toggleDisconectFromAll(setVisible);
    }

    /**
     * Test.
     */
    @Test
    public void testToggleDisconectFromServer() {
        System.out.println("toggleDisconectFromServer");
        boolean setVisible = false;
        GMenuBar instance = new GMenuBar();
        instance.toggleDisconectFromServer(setVisible);
    }

    /**
     * Test.
     */
    @Test
    public void testToggleUserMenuBar() {
        System.out.println("toggleUserMenuBar");
        boolean setVisible = false;
        GMenuBar instance = new GMenuBar();
        instance.toggleUserMenuBar(setVisible);
    }

    /**
     * Test.
     */
    @Test
    public void testToggleClosePanel() {
        System.out.println("toggleClosePanel");
        boolean setVisible = false;
        GMenuBar instance = new GMenuBar();
        instance.toggleClosePanel(setVisible);
    }

}