package Client;

import Connection.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;


public class PrivateChatTab extends AbstractTab {

    private JEditorPane infobox;
    private JEditorPane chat;
    private String tabName;
    private ServerTab server;
    private Color originalColor;
    private Color unreadMessageColor;


    public PrivateChatTab(String nickname) {

        // Konstrukce panelu
        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        // Info panel - vypis dodatecnych informaci o uzivateli,
        // se kterym komunikujeme
        JPanel toppanel = new JPanel();
        toppanel.setLayout (new BoxLayout(toppanel, BoxLayout.LINE_AXIS) );
        GUI.setPreferredSize(toppanel, 695, 100);
        toppanel.setMaximumSize( new Dimension(3200, 120) );
        toppanel.setBorder( BorderFactory.createEtchedBorder() );

        JScrollPane infoscroll = new JScrollPane();
        infoscroll.setBorder( BorderFactory.createEmptyBorder() );
        infoscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        infoscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        infoscroll.setAutoscrolls(true);

        infobox = new JEditorPane();
        infobox.setContentType("text/html");
        infobox.setEditable(false);

        infoscroll.setViewportView(infobox);
        toppanel.add(infoscroll);

        // Obsahovy panel - vypis chatu
        JPanel bottompanel = new JPanel();
        bottompanel.setLayout( new BoxLayout(bottompanel, BoxLayout.LINE_AXIS) );
        GUI.setPreferredSize(bottompanel, 695, 305);
        bottompanel.setBorder( BorderFactory.createEmptyBorder() );

        JScrollPane chatscroll = new JScrollPane();
        chatscroll.setBorder( BorderFactory.createEmptyBorder() );
        chatscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chatscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatscroll.setAutoscrolls(true);

        chat = GUI.createHTMLPane();

        chatscroll.setViewportView(chat);
        bottompanel.add(chatscroll);

        // Uskladneni objektu to hlavniho panelu
        add(toppanel);
        add(bottompanel);
        setBackground(Color.WHITE);

        layout.putConstraint(SpringLayout.NORTH, bottompanel, 5, SpringLayout.SOUTH, toppanel);
        layout.putConstraint(SpringLayout.WEST, toppanel, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, bottompanel, 0, SpringLayout.WEST, toppanel);
        layout.putConstraint(SpringLayout.EAST, toppanel, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, bottompanel, 0, SpringLayout.EAST, toppanel);
        layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, bottompanel);

        // Priradi nazev zalozky
        tabName = nickname;

        // Nastavení barev pro singalizaci příchodu nové zprávy
        unreadMessageColor = new Color(255, 128, 128);

    }

    @Override
    public DeprecatedConnection getConnection() {
        return server.getConnection();
    }

    @Override
    public ServerTab getServerTab() {
        return server;
    }

    @Override
    public void addText(String str) {
        EditorKit kit = chat.getEditorKit();
        Document doc = chat.getDocument();
        try {
            Reader reader = new StringReader(str);
            kit.read(reader, doc, doc.getLength());
            chat.setCaretPosition( doc.getLength() );
        }
        catch (Exception e) { }
    }

    /**
     * Nastavuje obsah horni casti - vypis informaci o uzivateli.
     */
    public void setOtherUserInfo(String info) {
        infobox.setText(info);
    }

    @Override
    public String getTabName() {
        return tabName;
    }

    /**
     * Nastaveni nazvu (uzivatele mohou sve prezdivky menit).
     */
    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    /**
     * Kanál se přilepí na svůj server (instance ServerTab).
     * Také načte informace o druhém uživateli.
     */
    @Override
    public void adapt(String nickname) {
        if (Input.getCurrentServer() == null) {
            Input.showNoConnectionError();
        }
        else {
            server = Input.getCurrentServer();
            Input.getCurrentServer().privateChats.add(this);
            getConnection().setTab(this);
            // getQuery().whois(tabName);
        }
    }

    @Override
    public void die() {
        server.privateChats.remove(this);
    }

    @Override
    public void die(String reason) {
        die();
    }

    @Override
    public void setFocus() {
        changeNickname();
        setToRead(false);
        GUI.getTabContainer().setSelectedComponent(this);
        GUI.getMenuBar().toggleDisconectFromServer(true);
        GUI.focusInput();
    }

    @Override
    public void clearContent() {
        chat.setText(null);
    }

    /**
     * Výpis informací o uživateli (příkaz WHOIS).
     */
    public void updateUserInfo(String str) {
        EditorKit kit = infobox.getEditorKit();
        Document doc = infobox.getDocument();
        try {
            Reader reader = new StringReader(str);
            kit.read(reader, doc, doc.getLength());
            infobox.setCaretPosition( doc.getLength() );
        }
        catch (Exception e) { }
    }

    public void clearUserInfo() {
        infobox.setText(null);
    }

    /**
     * Získá původní barvu pozadí záložky v tabbed panelu.
     */
    private void setupOriginalColor() {
        if (originalColor != null)
            return;

        int index = GUI.getTabContainer().indexOfComponent(this);
        originalColor = GUI.getTabContainer().getBackgroundAt(index);
    }

    /**
     * Při příchodu nové zprávy se panel označí červenou barvou záložky
     * v tabbed panelu.
     */
    public void setToRead(boolean toRead) {
        setupOriginalColor();

        int index = GUI.getTabContainer().indexOfComponent(this);

        if (toRead)
            GUI.getTabContainer().setBackgroundAt(index, unreadMessageColor);
        else
            GUI.getTabContainer().setBackgroundAt(index, originalColor);
    }

}
