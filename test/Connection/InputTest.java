package Connection;

import Client.Client;
import Client.ClientLogger;
import Client.GInput;
import Client.GTabServer;
import Client.GUI;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class InputTest {

    /**
     * Test.
     */
    @Test
    public void testGetCurrentServer() {
        System.out.println("getCurrentServer");
        Input.currentTab = new GTabServer("adresa");
        GTabServer result = Input.getCurrentServer();
        assertNotNull(result);
    }

    /**
     * Test.
     */
    @Test
    public void testIsActiveChannel() {
        System.out.println("isActiveChannel");
        boolean expResult = false;
        boolean result = Input.isActiveChannel();
        assertEquals(expResult, result);
    }

    /**
     * Test.
     */
    @Test
    public void testOutput() {
        System.out.println("output");
        String str = "";
        Input.output(str);
    }

    /**
     * Test.
     */
    @Test
    public void testClearText() {
        System.out.println("clearText");
        GInput g = new GInput(10, 10);
        assertNotNull(g);
    }

    /**
     * Test.
     */
    @Test
    public void testIsConnected() {
        System.out.println("isConnected");
        boolean expResult = false;
        boolean result = Input.isConnected();
        assertEquals(expResult, result);
    }
/*
    @Test
    public void testConnectionError() {
        System.out.println("connectionError");
        Input.connectionError();
    }

    @Test
    public void testActiveChannelError() {
        System.out.println("activeChannelError");
        Input.activeChannelError();
    }
*/

    /**
     * Test.
     */
    @Test
    public void testHandlers() {
        System.out.println("handleQuit");
        Client.run();
        ClientLogger.disable();
        GUI.prepareForm();
        GUI.getForm().validate();
        // Pripojeni na server
        Input.handleServer("irc.mmoirc.com:6667");
        // Pripojeni do mistnosti
        Input.handleJoin("mujTestKanal");
        // Napsani do mistnosti
        Input.handlePrivMessage("#mujTestKanal zprava");
        // Zmeni prezdivku
        Input.handleNick("MujNickNejm");
        // Nastavi tema v kanale a na serveru
        Input.handleTopic("Nove tema, hahaha");
        // Necha si vypsat jmena
        Input.handleNames("#mujTestKanal");
        // Nastavi priznaky AFK - away from keyboard
        Input.handleAway("Jsem pryc..");
        Input.handleBack();
        // Emote
        Input.handleMe("slapnul do *****.");
        // Zmena modu kanalu a uzivatele
        Input.handleMode("#mujTestKanal +p");
        Input.handleMode("-o MujNickNejm");
        // Zazada o prava operatora
        Input.handleOper("MujNickNejm hesloooo");
        // Zjistim o sobe informace
        Input.handleWhois("MujNickNejm");

        // Test Reply - simulace zprav ze serveru
        Connection con = Input.currentTab.getConnection();

        Reply.create(":Odesilatel!Martin@Bohnice FALESNYPRIKAZ :Ohohoooo", con);
        Reply.create(":Odesilatel!Martin@Bohnice 221 MujNickNejm +v", con);
        Reply.create(":Odesilatel!Martin@Bohnice 251 :Je zde opravdu hodne uzivatelu!", con);
        Reply.create(":Odesilatel!Martin@Bohnice 252 :A take hodne operatoru", con);
        Reply.create(":Odesilatel!Martin@Bohnice 253 :185 neznámých připojení", con);
        Reply.create(":Odesilatel!Martin@Bohnice 254 666 :channels formed", con);
        Reply.create(":Odesilatel!Martin@Bohnice 255 :I have 5 clients and 805 servers", con);
        Reply.create(":Odesilatel!Martin@Bohnice 301 Uzivatel :Jsem pryc, takze smula.", con);
        Reply.create(":Odesilatel!Martin@Bohnice 305 :AFK zruseno!", con);
        Reply.create(":Odesilatel!Martin@Bohnice 306 :AFK nastaveno!", con);
        Reply.create(":Odesilatel!Martin@Bohnice 311 Uzivatel :Nejaky trapny clovek lalala", con);
        Reply.create(":Odesilatel!Martin@Bohnice 312 Uzivatel :SERVERXXX", con);
        Reply.create(":Odesilatel!Martin@Bohnice 313 Uzivatel :vubec, nikde!", con);
        Reply.create(":Odesilatel!Martin@Bohnice 317 Uzivatel :55 sekund je pryc!", con);
        Reply.create(":Odesilatel!Martin@Bohnice 318 Uzivatel :kaňec filma", con);
        Reply.create(":Odesilatel!Martin@Bohnice 319 Uzivatel :Valhalla, Youtube", con);
//        Reply.create(":Odesilatel!Martin@Bohnice 332 #mujTestKanal :Zadne tema - taky tema!", con);
        Reply.create(":Odesilatel!Martin@Bohnice 353 = mujTestKanal :Franta Pepa Jana", con);
        Reply.create(":Odesilatel!Martin@Bohnice 372 :Mesič of d dej", con);
        Reply.create(":Odesilatel!Martin@Bohnice 375 :SERVERXXX: MOTD:", con);
        Reply.create(":Odesilatel!Martin@Bohnice 376 :Konec MOTD:", con);
        Reply.create(":Odesilatel!Martin@Bohnice 401 Uzivatel :Tu neni", con);
        Reply.create(":Odesilatel!Martin@Bohnice 404 #mujTestKanal :Nemas prava, heh", con);
        Reply.create(":Odesilatel!Martin@Bohnice 432 losernick :Spatna prezdivka ty nulo", con);
        Reply.create(":Odesilatel!Martin@Bohnice 433 losernick :Prezdivku jiz nekdo pouziva. Nechtel bych!", con);
        Reply.create(":Odesilatel!Martin@Bohnice 482 #mujTestKanal :Nejsi operator, eh", con);

        Reply.create(":Mrkev PRIVMSG #mujTestKanal :ACTION odesel nakoupit.", con);
        Reply.create(":Mrkev PRIVMSG MujNickNejm :PING Mrkev", con);
        Reply.create(":Mrkev PRIVMSG MujNickNejm :Nazdaaaar!!!", con);

        Reply.create(":Mrkev!Martin@Bohnice NICK VelkaMrkev", con);
        Reply.create(":Angel MODE Angel +i", con);
        Reply.create(":Angel MODE #mujTestKanal +i", con);
        Reply.create(":Mrkev!Martin@Bohnice KICK #mujTestKanal NeznamyVojin", con);

        // Kicknu se, pripojim a opustim opustim kanal
        Input.handlePart("#mujTestKanal");
        Input.handleJoin("#mujTestKanal");
        Input.handleKick("#mujTestKanal MujNickNejm :Jsi blbec!");

        Reply.create(":Odesilatel!Martin@Bohnice 451 :Nepustim te!", con);

        try {
            wait(1000);
        } catch (Exception e) { }

        // Odpojeni od serveru
        //Input.handleQuit("Odchazim");
    }

/*
    @Test
    public void testHandleQuit_GTabWindow_String() {
        System.out.println("handleQuit");
        GInput g = new GInput(10, 10);
        assertNotNull(g);
    }

    @Test
    public void testHandleShutdown() {
        System.out.println("handleShutdown");
        Input.handleShutdown();
    }

*/
    /**
     * Test.
     */
    @Test
    public void testShowError() {
        System.out.println("showError");
        String invalidCommand = "";
        Input.showError(invalidCommand);
    }

    /**
     * Test.
     */
    @Test
    public void testMType() {
        System.out.println("mType");
        String result = Input.mType("");
        assertTrue(result.length() > 0);
    }

}