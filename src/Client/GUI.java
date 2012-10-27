package Client;

import java.awt.*;


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

    public static String showSetNicknameDialog() {
        MessageDialog question = new MessageDialog(MessageDialog.GROUP_INPUT, MessageDialog.TYPE_QUESTION, "Nastavení přezdívky", "Zvolte novou přezdívku");
        return question.strConfirm;
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

    public static void focusInput() {
        getInput().getTextField().requestFocus();
    }

}
