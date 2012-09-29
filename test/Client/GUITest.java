package Client;

import Connection.Input;
import java.awt.Component;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class GUITest {

    /**
     * Test.
     */
    @Test
    public void testPrepareForm() {
        System.out.println("prepareForm");
        GUI.prepareForm();
    }

    /**
     * Test.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAddTab() throws Exception {
        System.out.println("addTab");
        GTab t = new GTab(200, 100);
        assertNotNull(t);
    }

    /**
     * Test.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRemoveTab() throws Exception {
        System.out.println("removeTab");
        Component c = null;
        GUI.removeTab(c);
    }

    /**
     * Test.
     */
    @Test
    public void testSetExactSize() {
        System.out.println("setExactSize");
        Component c = new JPanel();
        int width = 0;
        int height = 0;
        GUI.setExactSize(c, width, height);
    }

    /**
     * Test.
     */
    @Test
    public void testSetMySize() {
        System.out.println("setMySize");
        Component c = new JPanel();
        int width = 0;
        int height = 0;
        GUI.setMySize(c, width, height);
    }

    /**
     * Test.
     */
    @Test
    public void testShowServersDialog() {
        System.out.println("showServersDialog");
        boolean show = false;
        GUI.showServersDialog(show);
    }

    /**
     * Test.
     */
    @Test
    public void testShowOptionsDialog() {
        System.out.println("showOptionsDialog");
        boolean show = false;
        GUI.showOptionsDialog(show);
    }
/*
    @Test
    public void testShowSetAwayDialog() {
        System.out.println("showSetAwayDialog");
        Input.currentTab = new GTabServer("adresa");
        GUI.showSetAwayDialog();
    }

    @Test
    public void testShowSetNicknameDialog() {
        System.out.println("showSetNicknameDialog");
        GUI.showSetNicknameDialog();
    }
*/
    /**
     * Test.
     */
    @Test
    public void testSetBack() {
        System.out.println("setBack");
        Input.currentTab = new GTabServer("adresa");
        GUI.setBack();
    }

    /**
     * Test.
     */
    @Test
    public void testGetForm() {
        System.out.println("getForm");
        GForm expResult = new GForm();
        assertNotNull(expResult);
    }

    /**
     * Test.
     */
    @Test
    public void testGetInput() {
        System.out.println("getInput");
        GInput expResult = new GInput(200, 100);
        assertNotNull(expResult);
    }

    /**
     * Test.
     */
    @Test
    public void testGetMenuBar() {
        System.out.println("getMenuBar");
        GMenuBar expResult = new GMenuBar();
        assertNotNull(expResult);
    }

    /**
     * Test.
     */
    @Test
    public void testGetTab() {
        System.out.println("getTab");
        GTab expResult = new GTab(200, 100);
        assertNotNull(expResult);
    }

    /**
     * Test.
     */
    @Test
    public void testCreateHTMLPane() {
        System.out.println("createHTMLPane");
        JEditorPane expResult = GUI.createHTMLPane();
        assertNotNull(expResult);
    }

    /**
     * Test.
     */
    @Test
    public void testFocusInput() {
        System.out.println("focusInput");
        GUI.focusInput();
    }

}