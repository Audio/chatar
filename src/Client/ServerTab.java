package Client;

import Connection.*;
import java.awt.Color;
import java.awt.Dimension;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

/**
 * Panel s výpisem informací o/ze serveru.
 * Základní grafická komponenta při připojování na server.
 *
 * @author Martin Fouček
 */
public class ServerTab extends AbstractTab {

    private JLabel label_address;
    private JLabel label_channels;
    private JEditorPane text;
    private String tabName;
    private Connection connection;
    /**
     * Přehled všech místností, ve kterých se uživatel nachází.
     */
    public  HashSet channels;
    /**
     * Přehled všech soukromých chatů, které má uživatel otevřené.
     */
    public  HashSet privateChats;

    /**
     * Konstruktor. Vytváří GUI panelu serveru.
     *
     * @param address
     */
    public ServerTab(String address) {

        // Pripojeni na server - analyza
        int upto;
        String server = address;
        int port = 6667;
        if ( address.matches(".+:\\d{3,}$") ) {
            upto = address.lastIndexOf(":");
            server = address.substring(0, upto);
            port = Integer.parseInt( address.substring(upto + 1) );
        }

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        JPanel top = new JPanel();
        top.setLayout( new BoxLayout(top, BoxLayout.PAGE_AXIS) );
        top.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.RAISED) );
        GUI.setPreferredSize(top, 700, 50);

        // Nazev serveru a jeho adresa
        JLabel text_address = new JLabel("Server:");
        label_address = new JLabel("irc://" + server + "/");

        Box r1 = Box.createHorizontalBox();
        r1.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 0) );
        r1.add(text_address);
        r1.add( Box.createRigidArea( new Dimension(10, 0)) );
        r1.add(label_address);

        // Pocet vytvorenych mistnosti (kanalu) - modifikováno v průběhu připojování
        JLabel text_channels = new JLabel("Počet místností:");
        label_channels = new JLabel("-");

        Box r2 = Box.createHorizontalBox();
        r2.setBorder( BorderFactory.createEmptyBorder() );
        r2.add(text_channels);
        r2.add( Box.createRigidArea( new Dimension(10, 0)) );
        r2.add(label_channels);

        top.add(r1);
        top.add(r2);

        // Panel s textareou
        JPanel textpanel = new JPanel();
        textpanel.setLayout( new BoxLayout(textpanel, BoxLayout.LINE_AXIS) );
        GUI.setPreferredSize(textpanel, 650, 320);
        textpanel.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
        textpanel.setBackground( new Color(255, 255, 255) );

        JScrollPane scrollpanel = new JScrollPane();
        scrollpanel.setBorder( BorderFactory.createEmptyBorder() );
        scrollpanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollpanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpanel.setAutoscrolls(true);

        text = GUI.createHTMLPane();
        text.setBackground( new Color(255, 255, 255) );

        scrollpanel.setViewportView(text);
        textpanel.add(scrollpanel);

        // Celkove usporadani
        add(top);
        add(textpanel);

        layout.putConstraint(SpringLayout.NORTH, textpanel, 0, SpringLayout.SOUTH, top);
        layout.putConstraint(SpringLayout.WEST, top, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, textpanel, 0, SpringLayout.WEST, top);
        layout.putConstraint(SpringLayout.EAST, top, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, textpanel, 0, SpringLayout.EAST, top);
        layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, textpanel);

        // Pripojeni na server
        tabName = server;
        connection = new Connection(server, port);
        connection.setTab(this);

        channels = new HashSet<ChannelTab>();
        privateChats = new HashSet<PrivateChatTab>();

    }

    /**
     * Vraci referenci na objekt Connection,
     * pres ktery komunikuje.
     *
     * @return pridruzene spojeni
     */
    @Override
    public DeprecatedConnection getConnection () {
        return null;
    }

    /**
     * Vraci referenci na kanal, ktery ma dany nazev
     * a je pripojen pres tento server.
     * Rozlišuje velikost písmen.
     *
     * @param name
     * @return
     */
    public ChannelTab getChannelByName (String name) {

        ChannelTab channel = null;
        name = name.toLowerCase();

        Iterator it = channels.iterator();
        while ( it.hasNext() ) {
            channel = (ChannelTab) it.next();
            if ( channel.getTabName().toLowerCase().equals(name) )
                break;
            else
                channel = null;
        }

        return channel;

    }

    /**
     * Vrati referenci na soukromy chat s uzivatelem, ktery ma zvolenou
     * prezdivku a je pripojen pres tento server.
     * Rozlišuje velikost písmen.
     *
     * @param nickname 
     * @return
     */
    public PrivateChatTab getPrivateChatByName (String nickname) {

        PrivateChatTab chat = null;
        nickname = nickname.toLowerCase();

        Iterator it = privateChats.iterator();
        while ( it.hasNext() ) {
            chat = (PrivateChatTab) it.next();
            if ( chat.getTabName().toLowerCase().equals(nickname) )
                break;
            else
                chat = null;
        }

        return chat;

    }

    /**
     * Zobrazuje vystupni text - pridava jej na konec.
     *
     * @param str
     */
    @Override
    public void addText(String str) {
        EditorKit kit = text.getEditorKit();
        Document doc = text.getDocument();
        try {
            Reader reader = new StringReader(str);
            kit.read(reader, doc, doc.getLength() );
            text.setCaretPosition( doc.getLength() );
        }
        catch (Exception e) { }
    }

    /**
     * Pripoji se na server. Pro pripojeni vytvori nove vlakno.
     * Aktuvuje tlačítko v menu - pro odpojení od serveru.
     *
     * @param foo
     */
    @Override
    public void adapt (String foo) {

        try {
            /*
            connection.connect();
            connection.start();
            */
            GUI.getMenuBar().toggleDisconectFromAll(true);
            GUI.getMenuBar().toggleUserMenuBar(true);
        }
        catch (Exception e) { /* System.err.println( e.getMessage() ); */ }

    }

    /**
     * Ukonci cinnost panelu, ale nezavre ho.
     *
     * @param reason
     */
    @Override
    public void die(String reason) {

        if (reason != null)
            addText( Output.HTML.mType("error") + reason);

        // TODO disconnect asi
        // if ( connection.isConnected() )
            // getQuery().disconnect();

        // connection.interrupt(); // TODO Thread asi

    }

    /**
     * Vraci nazev serveru pro pojmenovani zalozky v hl. panelu.
     * 
     * @return
     */
    @Override
    public String getTabName () {
        return tabName;
    }

    /**
     * Urci sama sebe jako aktualni panel.
     * Pouzito mj. pri zpracovani vstupu od uzivatele.
     */
    @Override
    public void setFocus () {

        Input.currentTab = this;
        connection.setTab(this);
        changeNickname();
        GUI.getTabContainer().setSelectedComponent(this);
        GUI.getMenuBar().toggleDisconectFromServer(true);
        GUI.focusInput();

    }

    /**
     * Zobrazuje počet místností (kanálů) na serveru.
     *
     * @param num
     */
    public void setChannelsCount (String num) {
        label_channels.setText(num);
    }

    /**
     * Vymaže obsah chatu.
     */
    @Override
    public void clearContent() {
        text.setText(null);
    }


}
