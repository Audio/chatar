package Client;

import Connection.Input;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;


public class GUI {

    private static TabContainer tabContainer;
    private static MainWindow window;
    private static GInput input;
    private static GMenuBar menuBar;
    private static GServers servers;
    private static GConfig options;

    /**
     * Vytvori novy formular. Umisti do nej menu a panely pro vlozeni obsahu.
     */
    public static void prepareForm() {
        window = new MainWindow();
        prepareMenuBar();
        prepareContent();
        input.getTextField().requestFocusInWindow();
    }

    /**
     * Vytvori zakladni layout formulare.
     * Umisti do nej panel pro obsah (tabbed panel)
     * a textove vstupni pole. Tyto dva panely budou zobrazeny stale.
     */
    private static void prepareContent() {

        // horni a spodni panely
        final int WIDTH = 650;

        tabContainer = new TabContainer(WIDTH, 400);
        input = new GInput(WIDTH, 100);

        // umisteni panelu ve formulari
        JPanel content_panel = (JPanel) window.getContentPane();
        SpringLayout layout = new SpringLayout();
        content_panel.setLayout(layout);
        content_panel.add(tabContainer);
        content_panel.add(input);

        // Tab umisten nad Input
        layout.putConstraint(SpringLayout.WEST, input, 0, SpringLayout.WEST, tabContainer);
        layout.putConstraint(SpringLayout.NORTH, input, 0, SpringLayout.SOUTH, tabContainer);
        // Roztahovani vertikalni - tab
        layout.putConstraint(SpringLayout.EAST, content_panel, 0, SpringLayout.EAST, tabContainer);
        layout.putConstraint(SpringLayout.SOUTH, content_panel, 40, SpringLayout.SOUTH, tabContainer);
        // Roztahovani hirizontalni - tab, input
        layout.putConstraint(SpringLayout.EAST, content_panel, 0, SpringLayout.EAST, input);
        layout.putConstraint(SpringLayout.EAST, input, 0, SpringLayout.EAST, tabContainer);

        tabContainer.setVisible(true);
        input.setVisible(true);

    }

    private static void prepareMenuBar() {

        menuBar = new GMenuBar();
        window.setJMenuBar( menuBar );
        menuBar.setVisible(true);

    }

    /**
     * Pridani obsahu. Typ obsahu je urcen tridou GTab.
     *
     * @param type GTab
     * @param address
     * @throws ClientException
     */
    public static void addTab(int type, String address) throws ClientException {
        tabContainer.addTab(type, address);
    }

    /**
     * Odebrani zvoleneho obsahu (zalozky).
     * Neexistuje-li dalsi zalozka, skryje tlačítka na odpojení od serverů.
     *
     * @param c
     * @throws ClientException
     */
    public static void removeTab(Component c) throws ClientException {

        tabContainer.removeTab(c);

        if ( tabContainer.getTabCount() == 0 ) {
            GUI.getMenuBar().toggleDisconectFromAll(false);
            GUI.getMenuBar().toggleDisconectFromServer(false);
            GUI.getMenuBar().toggleUserMenuBar(false);
            GUI.getMenuBar().toggleClosePanel(false);
        }

    }

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
     *
     * @param show
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
     *
     * @param show
     */
    public static void showOptionsDialog(boolean show) {

        if (options == null)
            options = new GConfig();

        options.setLocationRelativeTo( getWindow() );
        options.setVisible(show);

    }

    /**
     * Okno pro nastaveni duvodu nepritomnosti.
     */
    public static void showSetAwayDialog() {

        MessageDialog question = new MessageDialog(MessageDialog.GROUP_INPUT, MessageDialog.TYPE_QUESTION, "Nastavení vlastní zprávy", "Nastavte důvod své nepřítomnosti, nebo nechte pole prázdné.");
        String ans = question.strConfirm;

        if (ans == null) // cancel
            return;

        ans = ans.trim();
        if (ans.length() == 0)
            ans = "I'll be back soon.";

        Input.getCurrentServer().getQuery().away(ans);

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

        Input.getCurrentServer().getQuery().nick(ans);

    }

    /**
     * Zruseni nepritomnosti. Bez grafickeho vykresleni.
     */
    public static void setBack() {
        Input.getCurrentServer().getQuery().away(null);
    }

    public static MainWindow getWindow() {
        return window;
    }

    public static GInput getInput() {
        return input;
    }

    public static GMenuBar getMenuBar() {
        return menuBar;
    }

    public static TabContainer getTabContainer() {
        return tabContainer;
    }

    /**
     * Vytvoří JEditorPane, nastaví mu zobrazení HTML a CSS styl.
     *
     * @return
     */
    public static JEditorPane createHTMLPane() {

        JEditorPane chat = new JEditorPane();
        chat.setContentType("text/html");
        chat.setEditable(false);

        // CSS styl: font-family a size
        Font font = UIManager.getFont("Label.font");
        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
                "font-size: 13pt; }";
        ((HTMLDocument) chat.getDocument()).getStyleSheet().addRule(bodyRule);

        return chat;

    }

    public static void focusInput() {
        input.getTextField().requestFocus();
    }

}
