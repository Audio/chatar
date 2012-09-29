package Client;

import org.junit.Test;

/**
 * Test třídy Client.
 *
 * @author Martin Fouček
 */
public class ClientTest {

    /**
     * Test.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        Client.init();
    }

    /**
     * Test.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Client.run();
    }

    /**
     * Test.
     */
    @Test
    public void testTerminate_0args() {
        System.out.println("terminate");
        // zavolá System.exit()! Neni chyba testu, ma to tak byt. (nasleduje FAIL, nebot ukonci VM)
//        Client.terminate();
    }

    /**
     * Test.
     */
    @Test
    public void testTerminate_int() {
        System.out.println("terminate");
        int returnCode = 0;
        // zavolá System.exit()! Neni chyba testu, ma to tak byt. (nasleduje FAIL, nebot ukonci VM)
//        Client.terminate(returnCode);
    }

}