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
public class GTabWindowTest {

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
    public void testSetFocus() {
        System.out.println("setFocus");
        GTabChannel instance = new GTabChannel("kanal");
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testChangeNickname() {
        System.out.println("changeNickname");
        GTabChannel instance = new GTabChannel("kanal");
        assertNotNull(instance);
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
    public void testKillMyself() {
        System.out.println("killMyself");
        GTabServer instance = new GTabServer("adddrreesss");
        instance.killMyself();
    }

    /**
     * Test.
     */
    @Test
    public void testClearContent() {
        System.out.println("clearContent");
        GTabServer instance = new GTabServer("adddrreesss");
        instance.clearContent();
    }

}