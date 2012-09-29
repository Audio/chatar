package Client;

import java.awt.Component;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import org.junit.Test;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class GServersTest {

    /**
     * Test.
     */
    @Test
    public void testSetButtonSize() {
        System.out.println("setButtonSize");
        Component c = new JButton();
        GServers.setButtonSize(c);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowClosing() {
        System.out.println("windowClosing");
        WindowEvent e = null;
        GServers instance = new GServers();
        instance.windowClosing(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowOpened() {
        System.out.println("windowOpened");
        WindowEvent e = null;
        GServers instance = new GServers();
        instance.windowOpened(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowClosed() {
        System.out.println("windowClosed");
        WindowEvent e = null;
        GServers instance = new GServers();
        instance.windowClosed(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowIconified() {
        System.out.println("windowIconified");
        WindowEvent e = null;
        GServers instance = new GServers();
        instance.windowIconified(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowDeiconified() {
        System.out.println("windowDeiconified");
        WindowEvent e = null;
        GServers instance = new GServers();
        instance.windowDeiconified(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowActivated() {
        System.out.println("windowActivated");
        WindowEvent e = null;
        GServers instance = new GServers();
        instance.windowActivated(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowDeactivated() {
        System.out.println("windowDeactivated");
        WindowEvent e = null;
        GServers instance = new GServers();
        instance.windowDeactivated(e);
    }

}