package Client;

import Connection.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;


public class ServerTab extends AbstractTab implements ServerEventsListener {

    private JLabel addressLabel;
    private JLabel channelsLabel;
    private Connection connection;
    private ArrayList<ChannelTab> channelTabs;
    private ArrayList<PrivateChatTab> privateChatTabs;


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

        scrollpanel.setViewportView(content);
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
        channelTabs = new ArrayList<>();
        privateChatTabs = new ArrayList<>();

        connection = new Connection(server, port);
        connection.setServerEventsListener(this);
        (new Thread(connection)).start();
    }

    public Connection getConnection() {
        return connection;
    }

    public ChannelTab createChannelTab(String channel) {
        ChannelTab tab = new ChannelTab(channel, this);
        channelTabs.add(tab);
        connection.addChannelEventsListener(tab);
        connection.joinChannel(channel);
        MainWindow.getInstance().getTabContainer().insertTab(tab);
        return tab;
    }

    public void removeChannelTab(ChannelTab tab) {
        MainWindow.getInstance().removeTab(tab);
        connection.removeChannelEventsListener(tab);
        channelTabs.remove(tab);
    }

    public PrivateChatTab createPrivateChatTab(String nickname) {
        PrivateChatTab tab = new PrivateChatTab(nickname, this);
        privateChatTabs.add(tab);
        connection.addPrivateMessagingListener(tab);
        connection.whois(nickname);
        MainWindow.getInstance().getTabContainer().insertTab(tab);
        return tab;
    }

    public void removePrivateChatTab(PrivateChatTab tab) {
        MainWindow.getInstance().removeTab(tab);
        connection.removePrivateMessagingListener(tab);
        privateChatTabs.remove(tab);
    }

    public void closeAllTabs() {
        while (privateChatTabs.size() > 0)
            removePrivateChatTab( privateChatTabs.get(0) );

        while (channelTabs.size() > 0)
            removeChannelTab( channelTabs.get(0) );

        connection.disconnect();
        connection.removeServerEventsListener();
        MainWindow.getInstance().removeTab(this);
    }

    public ChannelTab getChannelTabByName(String name) {
        name = name.toLowerCase();
        for (ChannelTab channel : channelTabs) {
            if ( channel.getTabName().toLowerCase().equals(name) )
                return channel;
        }

        return null;
    }

    public PrivateChatTab getPrivateChatByName(String nickname) {
        nickname = nickname.toLowerCase();
        for (PrivateChatTab chat : privateChatTabs) {
            if ( chat.getTabName().toLowerCase().equals(nickname) )
                return chat;
        }

        return null;
    }

    @Override
    public ServerTab getServerTab() {
        return this;
    }

    public void setChannelsCount(String count) {
        channelsLabel.setText(count);
    }

    @Override
    public void connected() {
        GUI.getMenuBar().toggleDisconectFromAll(true);
        GUI.getMenuBar().toggleUserMenuBar(true);
        GUI.getInput().setNickname( connection.getNick() );
    }

    @Override
    public void connectionCantBeEstabilished(String reason) {
        new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Chyba připojení",
            "K vybranému serveru se nelze připojit.");
        appendText("Spojení nelze uskutečnit: " + reason);
        ClientLogger.log("Nelze se připojit: " + reason, ClientLogger.ERROR);
    }

    @Override
    public void serverMessageReceived(String message) {
        appendText(message);
    }

    @Override
    public void privateMessageWithoutListenerReceived(String sender, String message) {
        PrivateChatTab tab = createPrivateChatTab(sender);
        tab.appendText(sender + ": " + message);
    }

}
