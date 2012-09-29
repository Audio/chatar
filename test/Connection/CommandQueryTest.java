package Connection;

import Config.Config;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class CommandQueryTest {

    /**
     * Test.
     */
    @Test
    public void testGetConfig() {
        System.out.println("getConfig");
        Config instance = new Config();
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testSetBusy_0args() {
        System.out.println("setBusy");
        CommandQuery instance = new CommandQuery(null);
        instance.setBusy();
    }

    /**
     * Test.
     */
    @Test
    public void testSetBusy_boolean() {
        System.out.println("setBusy");
        boolean busy = false;
        CommandQuery instance = new CommandQuery(null);
        instance.setBusy(busy);
    }

    /**
     * Test.
     */
    @Test
    public void testIsBusy() {
        System.out.println("isBusy");
        CommandQuery instance = new CommandQuery(null);
        boolean expResult = false;
        boolean result = instance.isBusy();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testGoOn() {
        System.out.println("goOn");
        CommandQuery instance = new CommandQuery(null);
        instance.goOn();
    }

    /**
     * Test.
     */
    @Test
    public void testAddCommand() {
        System.out.println("addCommand");
        String command = "";
        CommandQuery instance = new CommandQuery(null);
        instance.addCommand(command);
    }

    /**
     * Test.
     */
    @Test
    public void testLogin() {
        System.out.println("login");
        CommandQuery instance = new CommandQuery(null);
        assertNotNull(instance);
    }

    /**
     * Test.
     */
    @Test
    public void testDisconnect_0args() {
        System.out.println("disconnect");
        CommandQuery instance = new CommandQuery(null);
        instance.disconnect();
    }

    /**
     * Test.
     */
    @Test
    public void testDisconnect_String() {
        System.out.println("disconnect");
        String reason = "";
        CommandQuery instance = new CommandQuery(null);
        instance.disconnect(reason);
    }

    /**
     * Test.
     */
    @Test
    public void testOper() {
        System.out.println("oper");
        String params = "";
        CommandQuery instance = new CommandQuery(null);
        instance.oper(params);
    }

    /**
     * Test.
     */
    @Test
    public void testJoin_String() {
        System.out.println("join");
        String channel = "";
        CommandQuery instance = new CommandQuery(null);
        instance.join(channel);
    }

    /**
     * Test.
     */
    @Test
    public void testJoin_String_String() {
        System.out.println("join");
        String channel = "";
        String key = "";
        CommandQuery instance = new CommandQuery(null);
        instance.join(channel, key);
    }

    /**
     * Test.
     */
    @Test
    public void testLeave() {
        System.out.println("leave");
        String channel = "";
        CommandQuery instance = new CommandQuery(null);
        instance.leave(channel);
    }

    /**
     * Test.
     */
    @Test
    public void testTopic_String() {
        System.out.println("topic");
        String channel = "";
        CommandQuery instance = new CommandQuery(null);
        instance.topic(channel);
    }

    /**
     * Test.
     */
    @Test
    public void testTopic_String_String() {
        System.out.println("topic");
        String channel = "";
        String topic = "";
        CommandQuery instance = new CommandQuery(null);
        instance.topic(channel, topic);
    }

    /**
     * Test.
     */
    @Test
    public void testPong() {
        System.out.println("pong");
        String target = "";
        CommandQuery instance = new CommandQuery(null);
        instance.pong(target);
    }

    /**
     * Test.
     */
    @Test
    public void testPrivMsg() {
        System.out.println("privMsg");
        String target = "";
        String msg = "";
        CommandQuery instance = new CommandQuery(null);
        instance.privMsg(target, msg);
    }

    /**
     * Test.
     */
    @Test
    public void testNick() {
        System.out.println("nick");
        String nick = "";
        CommandQuery instance = new CommandQuery(null);
        instance.nick(nick);
    }

    /**
     * Test.
     */
    @Test
    public void testNames() {
        System.out.println("names");
        String channels = "";
        CommandQuery instance = new CommandQuery(null);
        instance.names(channels);
    }

    /**
     * Test.
     */
    @Test
    public void testMode() {
        System.out.println("mode");
        String params = "";
        CommandQuery instance = new CommandQuery(null);
        instance.mode(params);
    }

    /**
     * Test.
     */
    @Test
    public void testKick() {
        System.out.println("kick");
        String params = "";
        CommandQuery instance = new CommandQuery(null);
        instance.kick(params);
    }

    /**
     * Test.
     */
    @Test
    public void testAway() {
        System.out.println("away");
        String reason = "";
        CommandQuery instance = new CommandQuery(null);
        instance.away(reason);
    }

    /**
     * Test.
     */
    @Test
    public void testWhois() {
        System.out.println("whois");
        String who = "";
        CommandQuery instance = new CommandQuery(null);
        instance.whois(who);
    }

    /**
     * Test.
     */
    @Test
    public void testMe() {
        System.out.println("me");
        String channel = "";
        String action = "";
        CommandQuery instance = new CommandQuery(null);
        instance.me(channel, action);
    }

}