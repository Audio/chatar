package Client;

import java.awt.*;


public class GUI {

    public static void setExactSize(Component c, int width, int height) {
        c.setMinimumSize( new Dimension(width, height) );
        c.setPreferredSize( new Dimension(width, height) );
        c.setMaximumSize( new Dimension(width, height) );
    }

    public static void setPreferredSize(Component c, int width, int height) {
        c.setMinimumSize( new Dimension(width, height) );
        c.setPreferredSize( new Dimension(width, height) );
    }

}
