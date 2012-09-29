package Client;

import javax.swing.JButton;
import javax.swing.JTextField;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class GInputTest {

    /**
     * Test.
     */
    @Test
    public void testGetButton() {
        System.out.println("getButton");
        GInput instance = new GInput(200, 100);
        JButton result = instance.getButton();
        assertNotNull(result);
    }

    /**
     * Test.
     */
    @Test
    public void testGetTextField() {
        System.out.println("getTextField");
        GInput instance = new GInput(200, 100);
        JTextField result = instance.getTextField();
        assertNotNull(result);
    }

    /**
     * Test.
     */
    @Test
    public void testSetNickname() {
        System.out.println("setNickname");
        String nick = "";
        GInput instance = new GInput(200, 100);
        instance.setNickname(nick);
    }

    /**
     * Test.
     */
    @Test
    public void testHandleTextAndCommand () {
        GInput g = new GInput(100, 20);
        g = null;
        Client.run();
        ClientLogger.disable();
        GUI.prepareForm();
        GUI.getForm().validate();
        // Pripojeni na server a do mistnosti
        GUI.getInput().getTextField().setText("/server irc.mmoirc.com");
        GUI.getInput().getTextField().postActionEvent();
        // Odeslani textu do mistnosti
        GUI.getInput().getTextField().setText("Ahoj lidi!!");
        GUI.getInput().getTextField().postActionEvent();
        GUI.getInput().getTextField().setText("/join mujTestKanaal");
        GUI.getInput().getTextField().postActionEvent();
        GUI.getInput().getTextField().setText("Ahoj lidi!!");
        GUI.getInput().getTextField().postActionEvent();
    }

}