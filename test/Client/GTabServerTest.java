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
public class GTabServerTest {

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
    public void testGetChannelByName() {
        System.out.println("getChannelByName");
        GTabServer instance = new GTabServer("adresa");
        Input.currentTab = instance;

        String name = "kanal";
        GTabChannel expResult = new GTabChannel(name);
        expResult.adapt(name);

        GTabChannel result = instance.getChannelByName("#" + name);
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testGetPrivateChatByName() {
        System.out.println("getPrivateChatByName");
        String nickname = "";
        GTabServer instance = new GTabServer("adresa");
        GTabPrivateChat expResult = null;
        GTabPrivateChat result = instance.getPrivateChatByName(nickname);
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testAddText() {
        System.out.println("addText");
        String str = "";
        GTabServer instance = new GTabServer("adresa");
        instance.addText(str);
    }

    /**
     * Test.
     */
    @Test
    public void testAdapt() {
        System.out.println("adapt");
        String channel = "kanal";
        GTabServer instance = new GTabServer("adresa");
        instance.adapt(channel);
    }

    /**
     * Test.
     */
    @Test
    public void testDie_0args() {
        System.out.println("die");
        GTabServer instance = new GTabServer("adresa");
        instance.adapt("adresa");
        instance.die();
    }

    /**
     * Test.
     */
    @Test
    public void testDie_String() {
        System.out.println("die");
        String reason = "duvod";
        GTabServer instance = new GTabServer("adresa");
        instance.adapt("adresa");
        instance.die(reason);
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
    public void testGetTabName() {
        System.out.println("getTabName");
        GTabServer instance = new GTabServer("adresa");
        String expResult = "adresa";
        String result = instance.getTabName();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testGetServer() {
        System.out.println("getServer");
        GTabServer instance = new GTabServer("adresa");
        GTabServer expResult = instance;
        GTabServer result = instance.getServer();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testSetFocus() {
        System.out.println("setFocus");
        GTabServer instance = new GTabServer("adresa");
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testSetChannelsCount() {
        System.out.println("setChannelsCount");
        String num = "";
        GTabServer instance = new GTabServer("adresa");
        instance.setChannelsCount(num);
    }

    /**
     * Test.
     */
    @Test
    public void testClearContent() {
        System.out.println("clearContent");
        GTabServer instance = new GTabServer("adresa");
        instance.clearContent();
    }

}