package MainWindow;

import Client.*;
import Connection.*;
import java.awt.*;
import javax.swing.*;


public class PrivateChatTab extends AbstractTab implements PrivateMessagingListener {

    static final long serialVersionUID = 1L;

    private JEditorPane infobox;


    public PrivateChatTab(String nickname, final ServerTab serverTab) {
        this.serverTab = serverTab;

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        // Vypis dodatecnych informaci o uzivateli, se kterym komunikujeme
        JPanel topPanel = new JPanel();
        topPanel.setLayout (new BoxLayout(topPanel, BoxLayout.LINE_AXIS) );
        GUI.setPreferredSize(topPanel, 695, 75);
        topPanel.setMaximumSize( new Dimension(3200, 100) );
        topPanel.setBorder( BorderFactory.createEmptyBorder() );

        JScrollPane infoscroll = new JScrollPane();
        infoscroll.setBorder( BorderFactory.createEmptyBorder() );
        infoscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        infoscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        infoscroll.setAutoscrolls(true);

        infobox = new JEditorPane();
        infobox.setContentType("text/html");
        infobox.setEditable(false);
        infobox.setBackground(backgroundColor);
        setNicerFont(infobox);

        infoscroll.setViewportView(infobox);
        topPanel.add(infoscroll);

        // Obsahovy panel - vypis chatu
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout( new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS) );
        GUI.setPreferredSize(bottomPanel, 695, 275);
        bottomPanel.setBorder( BorderFactory.createEmptyBorder() );

        JScrollPane chatscroll = new JScrollPane();
        chatscroll.setBorder( BorderFactory.createEmptyBorder() );
        chatscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chatscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatscroll.setAutoscrolls(true);

        chatscroll.setViewportView(content);
        bottomPanel.add(chatscroll);

        // Uskladneni objektu to hlavniho panelu
        add(topPanel);
        add(bottomPanel);
        setBackground(Color.WHITE);

        layout.putConstraint(SpringLayout.NORTH, bottomPanel, 5, SpringLayout.SOUTH, topPanel);
        layout.putConstraint(SpringLayout.WEST, topPanel, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, bottomPanel, 0, SpringLayout.WEST, topPanel);
        layout.putConstraint(SpringLayout.EAST, topPanel, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, bottomPanel, 0, SpringLayout.EAST, topPanel);
        layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, bottomPanel);

        tabName = nickname;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
        MainWindow.getInstance().getTabContainer().removeTab(this);
        MainWindow.getInstance().getTabContainer().insertTab(this);
    }

    @Override
    public String getNickname() {
        return tabName;
    }

    @Override
    public void privateMessageReceived(String message) {
        appendText( HTML.bold( getNickname() ) + ": " + message);
    }

    @Override
    public void userChangesNick(String newNick) {
        setTabName(newNick);
    }

    @Override
    public void userIsAway(String reason) {
        appendInfo("Uživatel je nepřítomen z důvodu: " + reason);
    }

    @Override
    public void whoisUser(String userInfo) {
        infobox.setText(null);
        appendText("Uživatel " + HTML.bold(userInfo) , infobox);
    }

    @Override
    public void whoisServer(String serverInfo) {
        appendText("Připojen k " + HTML.bold(serverInfo) , infobox);
    }

    @Override
    public void whoisChannels(String channelList) {
        appendText("Přítomen na " + HTML.bold(channelList) , infobox);
    }

    @Override
    public void whoisIdle(String seconds) {
        appendText("Nečinný " + HTML.bold(seconds + " sekund") , infobox);
    }

}
