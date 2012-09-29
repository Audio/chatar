package Client;

import Connection.CommandQuery;
import Connection.Connection;
import Connection.Input;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class GTabChannelTest {

    /**
     * Test.
     */
    @Test
    public void testGetCellIndexFromListWhereClicked() {
        System.out.println("getCellIndexFromListWhereClicked");
        MouseEvent e = new MouseEvent(new JPanel(), 0, 0, 0, 20, 20, 1, false);
        GTabChannel instance = new GTabChannel("kanal");
        String result = instance.getCellIndexFromListWhereClicked(e);
        assertNull(result);
    }

    /**
     * Test.
     */
    @Test
    public void testOpenChatByClick_MouseEvent() {
        System.out.println("openChatByClick");
        MouseEvent e = new MouseEvent(new JPanel(), 0, 0, 0, 20, 20, 1, false);
        GTabChannel instance = new GTabChannel("kanal");
        instance.openChatByClick(e);
    }

    /**
     * Test.
     */
    @Test
    public void testOpenChatByClick_String() {
        System.out.println("openChatByClick");
        String nickname = null;
        GTabChannel instance = new GTabChannel("kanal");
        instance.openChatByClick(nickname);
    }

    /**
     * Test.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetConnection() throws Exception {
        System.out.println("getConnection");
        Connection con = new Connection();
        assertNotNull(con);
    }

    /**
     * Test.
     */
    @Test
    public void testGetServer() {
        System.out.println("getServer");
        GTabChannel instance = new GTabChannel("kanal");
        GTabServer result = instance.getServer();
        assertNull(result);
    }

    /**
     * Test.
     */
    @Test
    public void testAddText() {
        System.out.println("addText");
        String str = "";
        GTabChannel instance = new GTabChannel("kanal");
        instance.addText(str);
    }

    /**
     * Test.
     */
    @Test
    public void testSetUsers() {
        System.out.println("setUsers");
        String userlist = "";
        GTabChannel instance = new GTabChannel("kanal");
        instance.setUsers(userlist);
    }

    /**
     * Test.
     */
    @Test
    public void testSetTopic() {
        System.out.println("setTopic");
        String topic = "";
        GTabChannel instance = new GTabChannel("kanal");
        instance.setTopic(topic);
    }

    /**
     * Test.
     */
    @Test
    public void testGetTabName() {
        System.out.println("getTabName");
        GTabChannel instance = new GTabChannel("kanal");
        String expResult = "#kanal";
        String result = instance.getTabName();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testGetQuery() {
        System.out.println("getQuery");
        CommandQuery result = new CommandQuery(null);
        assertNotNull(result);
    }

    /**
     * Test.
     */
    @Test
    public void testAdapt() {
        System.out.println("adapt");
        String channel = "kanal";
        Input.currentTab = new GTabServer("adresa");
        GTabChannel instance = new GTabChannel("kanal");
        instance.adapt(channel);
    }

    /**
     * Test.
     */
    @Test
    public void testDie_0args() {
        System.out.println("die");
        Input.currentTab = new GTabServer("adresa");
        GTabChannel instance = new GTabChannel("kanal");
        instance.adapt("kanal");
        instance.die();
    }

    /**
     * Test.
     */
    @Test
    public void testDie_String() {
        System.out.println("die");
        Input.currentTab = new GTabServer("adresa");
        String reason = "duvod";
        GTabChannel instance = new GTabChannel("kanal");
        instance.adapt("kanal");
        instance.die(reason);
    }

    /**
     * Test.
     */
    @Test
    public void testSetFocus() {
        System.out.println("setFocus");
        GTabChannel instance = new GTabChannel("kanal");
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testClearContent() {
        System.out.println("clearContent");
        GTabChannel instance = new GTabChannel("kanal");
        instance.clearContent();
    }

    /**
     * Test.
     */
    @Test
    public void testHasNick() {
        System.out.println("hasNick");
        String user = "";
        GTabChannel instance = new GTabChannel("kanal");
        boolean expResult = false;
        boolean result = instance.hasNick(user);
        assertEquals(expResult, result);
    }

}