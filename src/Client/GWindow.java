package Client;

import javax.swing.JOptionPane;

/**
 * Použití třídy z důvodu snažšího přístupu k vytvoření jednoduchých dialogových
 * oken (nejčastěji pouze informačních). Třída umožnuje vytvářet jak informační
 * dialogová okna, tak i dialogová okna, která od uživatele očekávají vstup.
 * Vstup je následně uložen v atributech třídy a je tak přístupný zvenčí.
 *
 * @author Martin Fouček
 */
public class GWindow {

    // typy oznamovacich oken
    /**
     * Obyčejná zpráva.
     */
    final public static int GROUP_MESSAGE = 1;
    /**
     * Zpráva vyžadující potvrzení. Nelze stornovat.
     */
    final public static int GROUP_CONFIRM = 2;
    /**
     * Zpráva vyžadující potvrzení. Lze stornovat.
     */
    final public static int GROUP_CONFIRM_CANCEL = 3;
    /**
     * Zpráva očekávající vstup od uživatele.
     */
    final public static int GROUP_INPUT = 4;

    // typy zprav
    /**
     * Typ zprávy - SDĚLOVACÍ.
     */
    final public static int TYPE_PLAIN  = 10;
    /**
     * Typ zprávy - INFORMAČNÍ.
     */
    final public static int TYPE_INFORM = 11;
    /**
     * Typ zprávy - VAROVACÍ.
     */
    final public static int TYPE_WARN   = 12;
    /**
     * Typ zprávy - CHYBOVÁ.
     */
    final public static int TYPE_ERROR  = 13;
    /**
     * Typ zprávy - DOTAZOVACÍ.
     */
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
     *
     * @param group
     * @param message
     */
    public GWindow (int group, String message) {
        this(group, GWindow.TYPE_INFORM, "Informace", message);
    }

    /**
     * Group oznacuje typ okna (informace, potvrzeni...).
     * Type oznacuje typ zpravy (varovani, chyba...).
     *
     * @param group
     * @param title
     * @param message
     */
    public GWindow (int group, String title, String message) {
        this(group, GWindow.TYPE_INFORM, title, message);
    }

    /**
     * Group oznacuje typ okna (informace, potvrzeni...).
     * Type oznacuje typ zpravy (varovani, chyba...).
     *
     * @param group
     * @param type
     * @param title
     * @param message
     */
    public GWindow (int group, int type, String title, String message) {

        int swing_type = JOptionPane.INFORMATION_MESSAGE;

        switch (type) {
            case GWindow.TYPE_PLAIN: { swing_type = JOptionPane.PLAIN_MESSAGE; break; }
            case GWindow.TYPE_INFORM: { swing_type = JOptionPane.INFORMATION_MESSAGE; break; }
            case GWindow.TYPE_WARN: { swing_type = JOptionPane.WARNING_MESSAGE; break; }
            case GWindow.TYPE_ERROR: { swing_type = JOptionPane.ERROR_MESSAGE; break; }
            case GWindow.TYPE_QUESTION: { swing_type = JOptionPane.QUESTION_MESSAGE; break; }
        }

        // oznamovaci okno
        if (group == GWindow.GROUP_MESSAGE) {
            JOptionPane.showMessageDialog(null, message, title, swing_type);
        }
        // dotazovaci okno
        else if (group == GWindow.GROUP_CONFIRM || group == GWindow.GROUP_CONFIRM_CANCEL) {

            Object[] options = {"Ano", "Ne"};
            Object[] options_c = {"Ano", "Ne", "Storno"};
            int option_type = JOptionPane.YES_NO_OPTION;

            // prida tlacitko Storno
            if (group == GWindow.GROUP_CONFIRM_CANCEL) {
                options = options_c;
                option_type = JOptionPane.YES_NO_CANCEL_OPTION;
            }

            confirm = JOptionPane.showOptionDialog(null, message, title, option_type, swing_type, null, options, options[0]);

        }
        else if (group == GWindow.GROUP_INPUT) {
            strConfirm = JOptionPane.showInputDialog(null, message, title, swing_type);
        }

    }

}
