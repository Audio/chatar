package Client;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;


public class GUI {

    private static GServers servers;
    private static GConfig options;


    public static void setExactSize(Component c, int width, int height) {
        c.setMinimumSize( new Dimension(width, height) );
        c.setPreferredSize( new Dimension(width, height) );
        c.setMaximumSize( new Dimension(width, height) );
    }

    public static void setPreferredSize(Component c, int width, int height) {
        c.setMinimumSize( new Dimension(width, height) );
        c.setPreferredSize( new Dimension(width, height) );
    }

    /**
     * Vytvori dialogove okno pro nastaveni serveru (pokud jiz neni vytvorene).
     *
     * Dale toto dialogove okno ukaze/skryje.
     */
    public static void showServersDialog(boolean show) {

        if (servers == null)
            servers = new GServers();

        servers.setLocationRelativeTo( getWindow() );
        servers.setVisible(show);

    }

    /**
     * Vytvori dialogove okno pro nastaveni osobnich udaju (pokud jiz neni vytvorene).
     *
     * Dale toto dialogove okno ukaze/skryje.
     */
    public static void showOptionsDialog(boolean show) {

        if (options == null)
            options = new GConfig();

        options.setLocationRelativeTo( getWindow() );
        options.setVisible(show);

    }

    public static String showSetAwayDialog() {
        MessageDialog question = new MessageDialog(MessageDialog.GROUP_INPUT, MessageDialog.TYPE_QUESTION, "Nastavení vlastní zprávy", "Nastavte důvod své nepřítomnosti, nebo nechte pole prázdné.");
        return question.strConfirm;
    }

    /**
     * Okno pro nastaveni nové přezdívky.
     */
    public static void showSetNicknameDialog() {

        MessageDialog question = new MessageDialog(MessageDialog.GROUP_INPUT, MessageDialog.TYPE_QUESTION, "Nastavení přezdívky", "Zvolte novou přezdívku");
        String ans = question.strConfirm;

        if (ans == null) // cancel
            return;

        ans = ans.trim();
        if (ans.length() == 0) // nevyplneno
            return;

        // Input.getCurrentServer().getQuery().nick(ans);

    }

    public static MainWindow getWindow() {
        return MainWindow.getInstance();
    }

    public static TabContainer getTabContainer() {
        return getWindow().getTabContainer();
    }

    public static GMenuBar getMenuBar() {
        return getWindow().getGMenuBar();
    }

    public static Input getInput() {
        return getWindow().getGInput();
    }

    public static JEditorPane createHTMLPane() {
        JEditorPane chat = new JEditorPane();
        chat.setContentType("text/html");
        chat.setEditable(false);

        Font font = UIManager.getFont("Label.font");
        String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: 13pt; }";
        ((HTMLDocument) chat.getDocument()).getStyleSheet().addRule(bodyRule);

        return chat;
    }

    public static void focusInput() {
        getInput().getTextField().requestFocus();
    }

}
