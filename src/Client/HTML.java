package Client;

import java.awt.Color;


public class HTML {

    public static String bold(String str) {
        return "<strong>" + str + "</strong>";
    }

    public static String italic(String str) {
        return "<em>" + str + "</em>";
    }

    public static String red(String str) {
        return color(str, new Color(255, 0, 0) );
    }

    private static String color(String str, Color color) {
        return "<span style=\"color: rgb(" + color.getRed() + ","
                + color.getGreen() + "," + color.getBlue()+ ")\">" + str + "</span>";
    }

    public static String mType(String type) {
        return "[<small>" + type.toUpperCase() + "</small>] ";
    }

}
