package Connection;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test třídy.
 *
 * @author Martin Fouček
 */
public class OutputTest {

    /**
     * Test.
     */
    @Test
    public void boldTest () {
        String str = "strink";
        String expected = "<strong>" + str + "</strong>";
        str = Output.HTML.bold(str);
        assertEquals(expected, str);
    }

    /**
     * Test.
     */
    @Test
    public void italicTest () {
        String str = "strink";
        String expected = "<em>" + str + "</em>";
        str = Output.HTML.italic(str);
        assertEquals(expected, str);
    }

    /**
     * Test.
     */
    @Test
    public void colorTest () {
        String str = "shrink";
        String expected = "<span style=\"color: rgb(255,0,0)\">" + str + "</span>";
        str = Output.HTML.color(str, Output.HTML.RED);
        assertEquals(expected, str);
    }

}