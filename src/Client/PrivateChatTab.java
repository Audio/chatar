package Client;

import Connection.*;
import java.awt.*;
import javax.swing.*;


public class PrivateChatTab extends AbstractTab implements PrivateMessagingListener {

    private JEditorPane infobox;
    private Color originalColor;
    private Color unreadMessageColor;


    public PrivateChatTab(String nickname, final ServerTab serverTab) {
        this.serverTab = serverTab;

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

        chatscroll.setViewportView(content);
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

    public void setTabName(String tabName) {
        this.tabName = tabName;
        MainWindow.getInstance().getTabContainer().removeTab(this);
        MainWindow.getInstance().getTabContainer().insertTab(this);
    }

    @Override
    public void setFocus() {
        setNotification(false);
        super.setFocus();
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

    public void setNotification(boolean useNofiticationColor) {
        setupOriginalColor();

        int index = GUI.getTabContainer().indexOfComponent(this);
        Color color = useNofiticationColor ? unreadMessageColor : originalColor;
        GUI.getTabContainer().setBackgroundAt(index, color);
    }

    @Override
    public String getNickname() {
        return tabName;
    }

    @Override
    public void privateMessageReceived(String message) {
        appendText( getNickname() + ": " + message);
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
