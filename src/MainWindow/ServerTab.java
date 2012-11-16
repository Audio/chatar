package MainWindow;

import Client.*;
import Connection.*;
import Dialog.MessageDialog;
import Favorites.ConnectionDetails;
import Settings.*;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;
import javax.swing.*;


public class ServerTab extends AbstractTab implements ServerEventsListener, SettingsChangesListener {

    static final long serialVersionUID = 1L;

    private JLabel addressLabel;
    private JLabel channelsLabel;
    private Connection connection;
    private List<String> channelsToJoin;
    private List<ChannelTab> channelTabs;
    private List<PrivateChatTab> privateChatTabs;


    public ServerTab(ConnectionDetails cd) {
        tabName = cd.address;
        channelsToJoin = cd.channelsToJoin;
        channelTabs = new ArrayList<>();
        privateChatTabs = new ArrayList<>();

        createMainPanel();
        appendInfo("Probíhá připojování na server...");

        connection = new Connection(cd);
        connection.setServerEventsListener(this);
        (new Thread(connection)).start();

        Settings.getInstance().addChangesListener(this);
    }

    private void createMainPanel() {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        JPanel top = new JPanel();
        top.setLayout( new BoxLayout(top, BoxLayout.PAGE_AXIS) );
        top.setBorder( BorderFactory.createEmptyBorder() );
        GUI.setPreferredSize(top, 700, 50);

        JLabel textAddress = new JLabel("Server:");
        addressLabel = new JLabel("irc://" + tabName + "/");

        Box r1 = Box.createHorizontalBox();
        r1.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 0) );
        r1.add(textAddress);
        r1.add( Box.createRigidArea( new Dimension(10, 0)) );
        r1.add(addressLabel);

        JLabel textChannels = new JLabel("Počet místností:");
        channelsLabel = new JLabel("-");

        Box r2 = Box.createHorizontalBox();
        r2.setBorder( BorderFactory.createEmptyBorder() );
        r2.add(textChannels);
        r2.add( Box.createRigidArea( new Dimension(10, 0)) );
        r2.add(channelsLabel);

        top.add(r1);
        top.add(r2);

        JPanel textpanel = new JPanel();
        textpanel.setLayout( new BoxLayout(textpanel, BoxLayout.LINE_AXIS) );
        GUI.setPreferredSize(textpanel, 650, 320);
        textpanel.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
        textpanel.setBackground(Color.WHITE);

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
    }

    public Connection getConnection() {
        return connection;
    }

    public ChannelTab createChannelTab(String channel) {
        ChannelTab tab = new ChannelTab(channel, this);
        channelTabs.add(tab);
        connection.addChannelEventsListener(tab);
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
        Settings.getInstance().removeChangesListener(this);
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

    @Override
    public void connected() {
        MainWindow.getInstance().getMainMenu().toggleConnectionActions(true);
        MainWindow.getInstance().getMainMenu().toggleUserMenuBar(true);
        MainWindow.getInstance().getNickButton().setText( connection.getNick() );
        autoJoinChannels();
    }

    @Override
    public void connectionCantBeEstabilished(String reason) {
        MessageDialog.error("Chyba připojení", "K vybranému serveru se nelze připojit.");
        appendError("Spojení nelze uskutečnit: " + reason);
        ClientLogger.log("Nelze se připojit: " + reason, ClientLogger.ERROR);
    }

    private void autoJoinChannels() {
        while ( channelsToJoin.size() > 0 )
            connection.joinChannel( channelsToJoin.remove(0) );
    }

    @Override
    public void serverMessageReceived(int code, String message) {
        if (code > 400 && code < 500) {
            message = message.split(" ", 2)[1];
            appendError(message);

            final int ERR_NOMOTD = 422;
            AbstractTab tab = InputHandler.getActiveTab();
            if ( code != ERR_NOMOTD && tab.getServerTab().equals(this) && !tab.equals(this) )
                tab.appendError(message);

        } else {
            appendText( HTML.escapeTags(message) );
        }
    }

    @Override
    public void noticeMessageReceived(String sender, String message) {
        message = sender + ": " + message;
        appendNotice(message);

        AbstractTab tab = InputHandler.getActiveTab();
        if ( tab.getServerTab().equals(this) && !tab.equals(this) )
            tab.appendNotice(message);
    }

    @Override
    public void privateMessageWithoutListenerReceived(String sender, String message) {
        PrivateChatTab tab = createPrivateChatTab(sender);
        tab.appendText(sender + ": " + message);
    }

    @Override
    public void joined(String channel) {
        ChannelTab tab = getChannelTabByName(channel);
        if (tab == null)
            tab = createChannelTab(channel);

        tab.setFocus();
    }

    @Override
    public void channelCountReceived(String count) {
        channelsLabel.setText(count);
    }

    @Override
    public void myNickHasChanged(String newNickname) {
        MainWindow.getInstance().getNickButton().setText(newNickname);
    }

    @Override
    public void chatLoggingChanged(boolean enabled) {
        isChatLoggingEnabled = enabled;
    }

    @Override
    public void topicVisibilityChanged(boolean visible) {
        for (ChannelTab channel : channelTabs)
            channel.setTopicPanelVisibility(visible);
    }

    @Override
    public void timestampFormatChanged(String format) {
        timestampFormat = format;
    }

}
