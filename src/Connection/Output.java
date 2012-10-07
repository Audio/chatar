package Connection;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Manipulace s výstupním textem.
 *
 * @author Martin Fouček
 */
public class Output {

    /**
     * Obarvení textu pomocí značkovacího jazyka HTML.
     */
    public static class HTML {

        final static Color RED = new Color(255, 0, 0);

        final static private Color OPERATOR = new Color(255, 128, 0);

        /**
         * Upravi rez pisma na tucny.
         */
        public static String bold (String str) {
            return "<strong>" + str + "</strong>";
        }

        /**
         * Upravi rez pisma na kurzivu.
         */
        public static String italic (String str) {
            return "<em>" + str + "</em>";
        }

        /**
         * Upravi barvu pisma na zvolenou hodnotu.
         */
        public static String color (String str, Color color) {
            return "<span style=\"color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue()+ ")\">" + str + "</span>";
        }

        /**
         * Upraví typ zprávy před vypsáním na obrazovku.
         */
        public static String mType (String type) {
            return "[<small>" + type.toUpperCase() + "</small>] ";
        }

    }

}
