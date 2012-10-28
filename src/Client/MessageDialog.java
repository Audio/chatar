package Client;

import javax.swing.JOptionPane;


public class MessageDialog {

    final public static int GROUP_MESSAGE = 1;
    final public static int GROUP_CONFIRM = 2;
    final public static int GROUP_CONFIRM_CANCEL = 3;
    final public static int GROUP_INPUT = 4;

    final public static int TYPE_PLAIN  = 10;
    final public static int TYPE_INFORM = 11;
    final public static int TYPE_WARN   = 12;
    final public static int TYPE_ERROR  = 13;
    final public static int TYPE_QUESTION  = 14;

    /**
     * Vrací typ odpovědi. Pouze u GROUP_CONFIRM.
     */
    public int confirm;

    /**
     * Vrací odpověd (pouze u GROUP_INPUT).
     */
    public String strConfirm;

    /**
     * Group oznacuje typ okna (informace, potvrzeni...).
     * Type oznacuje typ zpravy (varovani, chyba...).
     */
    public MessageDialog(int group, String message) {
        this(group, MessageDialog.TYPE_INFORM, "Informace", message);
    }

    /**
     * Group oznacuje typ okna (informace, potvrzeni...).
     * Type oznacuje typ zpravy (varovani, chyba...).
     */
    public MessageDialog(int group, String title, String message) {
        this(group, MessageDialog.TYPE_INFORM, title, message);
    }

    /**
     * Group oznacuje typ okna (informace, potvrzeni...).
     * Type oznacuje typ zpravy (varovani, chyba...).
     */
    public MessageDialog(int group, int type, String title, String message) {

        int swing_type = JOptionPane.INFORMATION_MESSAGE;

        switch (type) {
            case MessageDialog.TYPE_PLAIN: { swing_type = JOptionPane.PLAIN_MESSAGE; break; }
            case MessageDialog.TYPE_INFORM: { swing_type = JOptionPane.INFORMATION_MESSAGE; break; }
            case MessageDialog.TYPE_WARN: { swing_type = JOptionPane.WARNING_MESSAGE; break; }
            case MessageDialog.TYPE_ERROR: { swing_type = JOptionPane.ERROR_MESSAGE; break; }
            case MessageDialog.TYPE_QUESTION: { swing_type = JOptionPane.QUESTION_MESSAGE; break; }
        }

        // oznamovaci okno
        if (group == MessageDialog.GROUP_MESSAGE) {
            JOptionPane.showMessageDialog(null, message, title, swing_type);
        }
        // dotazovaci okno
        else if (group == MessageDialog.GROUP_CONFIRM || group == MessageDialog.GROUP_CONFIRM_CANCEL) {

            Object[] options = {"Ano", "Ne"};
            Object[] options_c = {"Ano", "Ne", "Storno"};
            int option_type = JOptionPane.YES_NO_OPTION;

            // prida tlacitko Storno
            if (group == MessageDialog.GROUP_CONFIRM_CANCEL) {
                options = options_c;
                option_type = JOptionPane.YES_NO_CANCEL_OPTION;
            }

            confirm = JOptionPane.showOptionDialog(null, message, title, option_type, swing_type, null, options, options[0]);

        }
        else if (group == MessageDialog.GROUP_INPUT) {
            strConfirm = JOptionPane.showInputDialog(null, message, title, swing_type);
        }

    }

    public static String showSetAwayDialog() {
        MessageDialog question = new MessageDialog(MessageDialog.GROUP_INPUT, MessageDialog.TYPE_QUESTION, "Nastavení vlastní zprávy", "Nastavte důvod své nepřítomnosti, nebo nechte pole prázdné.");
        return question.strConfirm;
    }

    public static String showSetNicknameDialog() {
        MessageDialog question = new MessageDialog(MessageDialog.GROUP_INPUT, MessageDialog.TYPE_QUESTION, "Nastavení přezdívky", "Zvolte novou přezdívku");
        return question.strConfirm;
    }

}
