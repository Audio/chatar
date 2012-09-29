package Connection;

import Client.GTabServer;
import Client.GTabWindow;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class ConnectionTest {

    /**
     * Test.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Connection instance = new Connection();
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testGetQuery() {
        System.out.println("getQuery");
        Connection instance = new Connection();
        CommandQuery result = new CommandQuery(instance);
        assertNotNull(result);
    }

    /**
     * Test.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        Connection instance = new Connection();
        assertNotNull(instance);
    }

    /**
     * Test.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testClose() throws Exception {
        System.out.println("close");
        Connection instance = new Connection();
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testIsConnected() {
        System.out.println("isConnected");
        Connection instance = new Connection();
        boolean expResult = false;
        boolean result = instance.isConnected();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testIsMe() {
        System.out.println("isMe");
        String nickname = "";
        Connection instance = new Connection();
        boolean expResult = false;
        boolean result = instance.isMe(nickname);
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testIsAuthenticated() {
        System.out.println("isAuthenticated");
        Connection instance = new Connection();
        boolean expResult = false;
        boolean result = instance.isAuthenticated();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testAuthenticate() {
        System.out.println("authenticate");
        Connection instance = new Connection();
        instance.authenticate();
    }

    /**
     * Test.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testSend() throws Exception {
        System.out.println("send");
        String str = "";
        Connection instance = new Connection();
        assertNotNull(instance);
    }

    /**
     * Test.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLoadReply() throws Exception {
        System.out.println("loadReply");
        Connection instance = new Connection();
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testSetPort() {
        System.out.println("setPort");
        int port = 0;
        Connection instance = new Connection();
        instance.setPort(port);
    }

    /**
     * Test.
     */
    @Test
    public void testSetServer() {
        System.out.println("setServer");
        String server = "";
        Connection instance = new Connection();
        instance.setServer(server);
    }

    /**
     * Test.
     */
    @Test
    public void testSetTab() {
        System.out.println("setTab");
        GTabWindow tab = null;
        Connection instance = new Connection();
        instance.setTab(tab);
    }

    /**
     * Test.
     */
    @Test
    public void testSetClosedByServer() {
        System.out.println("setClosedByServer");
        Connection instance = new Connection();
        instance.setClosedByServer();
    }

    /**
     * Test.
     */
    @Test
    public void testGetTab() {
        System.out.println("getTab");
        Connection instance = new Connection();
        GTabWindow expResult = null;
        GTabWindow result = instance.getTab();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testGetServerTab() {
        System.out.println("getServerTab");
        Connection instance = new Connection();
        GTabServer g = new GTabServer("adresa");
        instance.setTab(g);
        GTabServer result = instance.getServerTab();
        assertEquals(g, result);
    }

    /**
     * Test.
     */
    @Test
    public void testOutput() {
        System.out.println("output");
        String str = "";
        Connection instance = new Connection();
        GTabServer g = new GTabServer("adresa");
        instance.setTab(g);
        instance.output(str);
    }

}