package Client;

import Connection.CommandQuery;
import Connection.Connection;
import Connection.Input;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class GTabPrivateChatTest {

    /**
     * Test.
     */
    @Test
    public void testGetConnection() {
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
        GTabPrivateChat instance = new GTabPrivateChat("chat");
        instance.addText(str);
    }

    /**
     * Test.
     */
    @Test
    public void testSetOtherUserInfo() {
        System.out.println("setOtherUserInfo");
        String info = "";
        GTabPrivateChat instance = new GTabPrivateChat("chat");
        instance.setOtherUserInfo(info);
    }

    /**
     * Test.
     */
    @Test
    public void testGetTabName() {
        System.out.println("getTabName");
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        String expResult = "nick";
        String result = instance.getTabName();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testSetTabName() {
        System.out.println("setTabName");
        String new_name = "new_nick";
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        instance.setTabName(new_name);
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
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        instance.adapt(channel);
    }

    /**
     * Test.
     */
    @Test
    public void testDie_0args() {
        System.out.println("die");
        Input.currentTab = new GTabServer("adresa");
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        instance.adapt("nick");
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
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        instance.adapt("nick");
        instance.die(reason);
    }

    /**
     * Test.
     */
    @Test
    public void testSetFocus() {
        System.out.println("setFocus");
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testClearContent() {
        System.out.println("clearContent");
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        instance.clearContent();
    }

    /**
     * Test.
     */
    @Test
    public void testUpdateUserInfo() {
        System.out.println("updateUserInfo");
        String str = "";
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        instance.updateUserInfo(str);
    }

    /**
     * Test.
     */
    @Test
    public void testClearUserInfo() {
        System.out.println("clearUserInfo");
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        instance.clearUserInfo();
    }

    /**
     * Test.
     */
    @Test
    public void testSetToRead() {
        System.out.println("setToRead");
        GTabPrivateChat instance = new GTabPrivateChat("nick");
        assertNotNull(instance);
    }

}