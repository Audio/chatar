package Client;

import Connection.CommandQuery;
import Connection.Connection;
import Connection.Input;
import java.awt.Color;
import java.awt.Dimension;
import java.io.Reader;
import java.io.StringReader;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

/**
 * Panel umožnující odesílat zprávy pouze konkrétnímu uživateli.
 * Výpis zpráv zohledněn přes grafické rozhraní.
 * Panel obsahuje informační box s výpisem WHOIS cílového uživatele.
 *
 * @author Martin Fouček
 */
public class GTabPrivateChat extends GTabWindow {

    private JEditorPane infobox;
    private JEditorPane chat;
    /**
     * Název záložky v tabbed panelu a zároveň přezdívka cílového uživatele.
     */
    public  String tabName;
    private GTabServer server;
    private Color originalColor;
    private Color unreadMessageColor;

    /**
     * Konstruktor. Vytváří GUI a lepí se na objekt GTabServer.
     *
     * @param nickname
     */
    public GTabPrivateChat (String nickname) {

        // Konstrukce panelu
        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        // Info panel - vypis dodatecnych informaci o uzivateli,
        // se kterym komunikujeme
        JPanel toppanel = new JPanel();
        toppanel.setLayout (new BoxLayout(toppanel, BoxLayout.LINE_AXIS) );
        GUI.setMySize(toppanel, 695, 100);
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
        GUI.setMySize(bottompanel, 695, 305);
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

    /**
     * Vraci referenci na objekt Connection,
     * pres ktery komunikuje.
     *
     * @return
     */
    @Override
    public Connection getConnection () {
        return server.getConnection();
    }

    /**
     * Vraci referenci na server (GTabServer), pres ktery je kanal napojen.
     * V pripade serverove mistnosti vraci ref. na sebe.
     *
     * @return
     */
    @Override
    public GTabServer getServer () {
        return server;
    }

    /**
     * Zobrazuje vystupni text - pridava jej na konec.
     *
     * @param str
     */
    @Override
    public void addText (String str) {

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
     *
     * @param info
     */
    public void setOtherUserInfo (String info) {
        infobox.setText(info);
    }

    /**
     * Vraci svuj nazev.
     *
     * @return
     */
    @Override
    public String getTabName() {
        return tabName;
    }

    /**
     * Nastaveni nazvu (uzivatele mohou sve prezdivky menit).
     *
     * @param new_name
     */
    public void setTabName (String new_name) {
        tabName = new_name;
    }

    /**
     * Vraci referenci na objekt CommandQuery,
     * pres ktery komunikuje.
     *
     * @return
     */
    @Override
    public CommandQuery getQuery() {
        return getConnection().getQuery();
    }

    /**
     * Kanál se přilepí na svůj server (instance GTabServer).
     * Také načte informace o druhém uživateli.
     * 
     * @param nickname
     */
    @Override
    public void adapt(String nickname) {

        if (Input.getCurrentServer() == null) {
            Input.connectionError();
        }
        else {
            server = Input.getCurrentServer();
            Input.getCurrentServer().privateChats.add(this);
            getConnection().setTab(this);
            getQuery().whois(tabName);
        }

    }

    /**
     * Ukonci cinnost panelu, ale nezavre ho.
     */
    @Override
    public void die() {
        server.privateChats.remove(this);
    }

    /**
     * Ukonci cinnost panelu, ale nezavre ho.
     * Uvedeno s duvodem.
     *
     * @param reason
     */
    @Override
    public void die(String reason) {
        die();
    }

    /**
     * Urci sama sebe jako aktualni panel.
     * Pouzito mj. pri zpracovani vstupu od uzivatele.
     */
    @Override
    public void setFocus() {

        Input.currentTab = this;
        getConnection().setTab(this);
        changeNickname();
        setToRead(false);
        GUI.getTab().setSelectedComponent(this);
        GUI.getMenuBar().toggleDisconectFromServer(true);
        GUI.focusInput();

    }

    /**
     * Vymaže obsah chatu.
     */
    @Override
    public void clearContent() {
        chat.setText(null);
    }

    /**
     * Výpis informací o uživateli (příkaz WHOIS).
     *
     * @param str
     */
    public void updateUserInfo (String str) {

        EditorKit kit = infobox.getEditorKit();
        Document doc = infobox.getDocument();
        try {
            Reader reader = new StringReader(str);
            kit.read(reader, doc, doc.getLength());
            infobox.setCaretPosition( doc.getLength() );
        }
        catch (Exception e) { }

    }

    /**
     * Vymaže informace o uživateli z panelu.
     */
    public void clearUserInfo () {
        infobox.setText(null);
    }

    /**
     * Získá původní barvu pozadí záložky v tabbed panelu.
     */
    private void setupOriginalColor () {

        if (originalColor != null)
            return;

        int index = GUI.getTab().indexOfComponent(this);
        originalColor = GUI.getTab().getBackgroundAt(index);

    }

    /**
     * Při příchodu nové zprávy se panel označí červenou barvou záložky
     * v tabbed panelu.
     *
     * @param toRead 
     */
    public void setToRead (boolean toRead) {

        setupOriginalColor();

        int index = GUI.getTab().indexOfComponent(this);

        if (toRead)
            GUI.getTab().setBackgroundAt(index, unreadMessageColor);
        else
            GUI.getTab().setBackgroundAt(index, originalColor);

    }

}
