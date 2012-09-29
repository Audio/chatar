package Client;

import Config.Config;
import java.awt.event.WindowEvent;
import javax.swing.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class GConfigTest {

    /**
     * Test.
     */
    @Test
    public void testSetButtonSize() {
        System.out.println("setButtonSize");
        JButton button = new JButton();
        GConfig.setButtonSize(button);
    }

    /**
     * Test.
     */
    @Test
    public void testSetTextFieldSize() {
        System.out.println("setTextFieldSize");
        JTextField textField = new JTextField("text");
        GConfig.setTextFieldSize(textField);
    }

    /**
     * Test.
     */
    @Test
    public void testGetConfig() {
        System.out.println("getConfig");
        Config result = GConfig.getConfig();
        assertNotNull(result);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowClosing() {
        System.out.println("windowClosing");
        WindowEvent e = null;
        GConfig instance = new GConfig();
        instance.windowClosing(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowOpened() {
        System.out.println("windowOpened");
        WindowEvent e = null;
        GConfig instance = new GConfig();
        instance.windowOpened(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowClosed() {
        System.out.println("windowClosed");
        WindowEvent e = null;
        GConfig instance = new GConfig();
        instance.windowClosed(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowIconified() {
        System.out.println("windowIconified");
        WindowEvent e = null;
        GConfig instance = new GConfig();
        instance.windowIconified(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowDeiconified() {
        System.out.println("windowDeiconified");
        WindowEvent e = null;
        GConfig instance = new GConfig();
        instance.windowDeiconified(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowActivated() {
        System.out.println("windowActivated");
        WindowEvent e = null;
        GConfig instance = new GConfig();
        instance.windowActivated(e);
    }

    /**
     * Test.
     */
    @Test
    public void testWindowDeactivated() {
        System.out.println("windowDeactivated");
        WindowEvent e = null;
        GConfig instance = new GConfig();
        instance.windowDeactivated(e);
    }

}