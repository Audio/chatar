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


    public static class User {

        public static final String PREFIX_OWNER = "~",
                                   PREFIX_OPERATOR = "@",
                                   PREFIX_HALF_OPERATOR = "%",
                                   PREFIX_VOICE = "+",
                                   PREFIX_BOT = "&";


        public static Component addPrefixBasedIcon(Component c, String nick) {
            String prefix = nick.substring(0, 1);
            switch (prefix) {
                case PREFIX_OWNER:          setOwnerIcon(c); break;
                case PREFIX_OPERATOR:       setOperatorIcon(c); break;
                case PREFIX_HALF_OPERATOR:  setHalfOperatorIcon(c); break;
                case PREFIX_VOICE:          setVoiceIcon(c); break;
                case PREFIX_BOT:            setBotIcon(c); break;
                default:                    setCommonUserIcon(c);
            }

            nick = removePrefix(nick);
            ((JLabel) c).setText(nick);

            return c;
        }

        public static boolean isOwner(String nick) {
            return nick.startsWith(PREFIX_OWNER);
        }

        public static boolean isOperator(String nick) {
            return nick.startsWith(PREFIX_OPERATOR);
        }

        public static boolean isHalfOperator(String nick) {
            return nick.startsWith(PREFIX_HALF_OPERATOR);
        }

        public static boolean isVoice(String nick) {
            return nick.startsWith(PREFIX_VOICE);
        }

        public static boolean isBot(String nick) {
            return nick.startsWith(PREFIX_BOT);
        }

        private static Component setCommonUserIcon(Component c) {
            ((JLabel) c).setIcon( new ImageIcon("img/user.png") );
            return c;
        }

        private static Component setOwnerIcon(Component c) {
            // TODO owner icon
            ((JLabel) c).setIcon( new ImageIcon("img/operator.png") );
            return c;
        }

        private static Component setOperatorIcon(Component c) {
            ((JLabel) c).setIcon( new ImageIcon("img/operator.png") );
            return c;
        }

        private static Component setHalfOperatorIcon(Component c) {
            ((JLabel) c).setIcon( new ImageIcon("img/halfoperator.png") );
            return c;
        }

        private static Component setVoiceIcon(Component c) {
            ((JLabel) c).setIcon( new ImageIcon("img/voice.png") );
            return c;
        }

        private static Component setBotIcon(Component c) {
            ((JLabel) c).setIcon( new ImageIcon("img/bot.png") );
            return c;
        }

        public static String removePrefix(String nick) {
            if ( isOwner(nick) || isOperator(nick) || isHalfOperator(nick) || isVoice(nick) || isBot(nick) )
                return nick.substring(1);

            return nick;
        }

    }

}
