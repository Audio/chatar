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
         *
         * @param str
         * @return
         */
        public static String bold (String str) {
            return "<strong>" + str + "</strong>";
        }

        /**
         * Upravi rez pisma na kurzivu.
         *
         * @param str
         * @return
         */
        public static String italic (String str) {
            return "<em>" + str + "</em>";
        }

        /**
         * Upravi barvu pisma na zvolenou hodnotu.
         *
         * @param str
         * @param color
         * @return
         */
        public static String color (String str, Color color) {
            return "<span style=\"color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue()+ ")\">" + str + "</span>";
        }

        /**
         * Upraví typ zprávy před vypsáním na obrazovku.
         *
         * @param type
         * @return
         */
        public static String mType (String type) {
            return "[<small>" + type.toUpperCase() + "</small>] ";
        }

    }

    /**
     * Manipulace s textem, který představuje přezdívku jednoho z uživatelů.
     */
    public static class User {

        /**
         * Přiřadí ikonu k textu na základě oprávnění uživatele
         * (prefixu přezdívky).
         * 
         * @param c
         * @param nick
         * @return
         */
        public static Component addIcon (Component c, String nick) {

            // Nastaví ikonku
            if ( isOperator(nick) )
                flagOperator(c);
            else if ( isHalfOperator(nick) )
                flagHalfOperator(c);
            else if ( isVoice(nick) )
                flagVoice(c);
            else if ( isBot(nick) )
                flagBot(c);
            else
                flagUser(c);

            // Nastaví přezdívku
            nick = removePrefix(nick);

            ((JLabel) c).setText(nick);

            return c;

        }

        /**
         * Vrací informaci, zda je daný uživatel operátor.
         *
         * @param nick
         * @return
         */
        public static boolean isOperator (String nick) {
            return nick.startsWith("@");
        }

        /**
         * Vrací informaci, zda je daný uživatel half-operátor.
         *
         * @param nick
         * @return
         */
        public static boolean isHalfOperator (String nick) {
            return nick.startsWith("%");
        }

        /**
         * Vrací informaci, zda je daný uživatel voice.
         *
         * @param nick
         * @return
         */
        public static boolean isVoice (String nick) {
            return nick.startsWith("+");
        }

        /**
         * Vrací informaci, zda je daný uživatel bot.
         *
         * @param nick
         * @return
         */
        public static boolean isBot (String nick) {
            return nick.startsWith("&");
        }

        /**
         * Nastaví ikonku uživatele.
         *
         * @param c
         * @return
         */
        private static Component flagUser (Component c) {
            ((JLabel) c).setIcon( new ImageIcon( "img/user.png" ) );
            return c;
        }

        /**
         * Nastaví ikonku operátora.
         *
         * @param c
         * @return
         */
        private static Component flagOperator (Component c) {
            ((JLabel) c).setIcon( new ImageIcon( "img/operator.png" ) );
            return c;
        }

        /**
         * Nastaví ikonku half-operátora.
         *
         * @param c
         * @return
         */
        private static Component flagHalfOperator (Component c) {
            ((JLabel) c).setIcon( new ImageIcon( "img/halfoperator.png" ) );
            return c;
        }

        /**
         * Nastaví ikonku voice.
         *
         * @param c
         * @return
         */
        private static Component flagVoice (Component c) {
            ((JLabel) c).setIcon( new ImageIcon( "img/voice.png" ) );
            return c;
        }

        /**
         * Nastaví ikonku bota.
         *
         * @param c
         * @return
         */
        private static Component flagBot (Component c) {
            ((JLabel) c).setIcon( new ImageIcon( "img/bot.png" ) );
            return c;
        }

        /**
         * Odebere z nicku prefix (pokud existuje).
         *
         * @param nick
         * @return
         */
        public static String removePrefix (String nick) {
            
            if ( isOperator(nick) || isHalfOperator(nick) || isVoice(nick) || isBot(nick) )
                return nick.substring(1);

            return nick;

        }

    }

}
