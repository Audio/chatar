package Client;

import Connection.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.*;
import org.jibble.pircbot.IrcException;


public class ServerTab extends AbstractTab implements ServerEventsListener {

    private JLabel addressLabel;
    private JLabel channelsLabel;
    private JEditorPane text;
    private String tabName;
    // TODO zadny public
    public HashSet channels;
    public HashSet privateChats;


    public ServerTab(String address) {
        // TODO sem musi rovnout prijit adresa a port
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

        JLabel text_address = new JLabel("Server:");
        addressLabel = new JLabel("irc://" + server + "/");

        Box r1 = Box.createHorizontalBox();
        r1.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 0) );
        r1.add(text_address);
        r1.add( Box.createRigidArea( new Dimension(10, 0)) );
        r1.add(addressLabel);

        JLabel text_channels = new JLabel("Počet místností:");
        channelsLabel = new JLabel("-");

        Box r2 = Box.createHorizontalBox();
        r2.setBorder( BorderFactory.createEmptyBorder() );
        r2.add(text_channels);
        r2.add( Box.createRigidArea( new Dimension(10, 0)) );
        r2.add(channelsLabel);

        top.add(r1);
        top.add(r2);

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
        channels = new HashSet<>();
        privateChats = new HashSet<>();

        try {
            connection = new Connection(server, port);
            connection.addServerEventListener(this);
        } catch (IOException | IrcException e) {
            new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Chyba připojení", "K vybranému serveru se nelze připojit.");
            // die("Spojení nelze uskutečnit."); // TODO volani z konstruktoru :-/
        }

        // TODO po pripojeni
        /*
        GUI.getMenuBar().toggleDisconectFromAll(true);
        GUI.getMenuBar().toggleUserMenuBar(true);
        */
    }

    /**
     * Vraci referenci na kanal, ktery ma dany nazev
     * a je pripojen pres tento server.
     * Rozlišuje velikost písmen.
     */
    public ChannelTab getChannelTabByName(String name) {
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
     * Ukonci cinnost panelu, ale nezavre ho.
     */
    public void die(String reason) {

        if (reason != null)
            addText( Output.HTML.mType("error") + reason);

        // TODO disconnect asi
        // if ( connection.isConnected() )
            // getQuery().disconnect();

        // connection.interrupt(); // TODO Thread asi

    }

    @Override
    public String getTabName () {
        return tabName;
    }

    @Override
    public ServerTab getServerTab() {
        return this;
    }

    @Override
    public void setFocus() {
        changeNickname();
        GUI.getTabContainer().setSelectedComponent(this);
        GUI.getMenuBar().toggleDisconectFromServer(true);
        GUI.focusInput();
    }

    public void setChannelsCount(String num) {
        channelsLabel.setText(num);
    }

    @Override
    public void clearContent() {
        text.setText(null);
    }

    @Override
    public void messageReceived(String message) {
        addText(message);
    }

}
