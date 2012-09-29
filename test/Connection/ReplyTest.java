package Connection;

import org.junit.Test;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class ReplyTest {

    /**
     * Test.
     */
    @Test
    public void testCreate () {
        System.out.println("create");
        String str = "";
        Connection connection = null;
        Reply.create(str, connection);
    }

}