package MainWindow;

import Client.*;
import Connection.*;
import Settings.Settings;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.swing.*;


public class ChannelTab extends AbstractTab implements ChannelEventsListener {

    static final long serialVersionUID = 1L;

    private JList<User> userList;
    private UserListRenderer<User> userListRenderer;
    private SortedListModel<User> usersModel;
    private JEditorPane topicPanel;
    private JPopupMenu popup;
    private MouseListener popupListener;
    private String selectedPopupUser;
    private LinkedList<User> usersQueue;


    public ChannelTab(String channel, final ServerTab serverTab) {
        this.serverTab = serverTab;
        this.tabName = channel;
        this.usersQueue = new LinkedList<>();
        this.usersModel = new SortedListModel<>();
        this.userListRenderer = new UserListRenderer<>();

        createMainPanel();
        createPopupMenu();
        setTopic(null);
    }

    private void createMainPanel() {
        userList = new JList<>(usersModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setSelectedIndex(0);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setSelectionBackground( new Color(102, 153, 255) );
        userList.setCellRenderer(userListRenderer);
        userList.setBorder( BorderFactory.createEmptyBorder(10, 10, 0, 0) );

        JScrollPane usersPanel = new JScrollPane(userList);
        usersPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        usersPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        usersPanel.setAutoscrolls(true);
        usersPanel.setBorder( BorderFactory.createEmptyBorder() );
        GUI.setPreferredSize(usersPanel, 200, 350);

        topicPanel = new JEditorPane();
        topicPanel.setContentType("text/html");
        topicPanel.setEditable(false);
        topicPanel.setBackground(backgroundColor);
        topicPanel.setBorder( BorderFactory.createEmptyBorder(10, 10, 0, 10) );
        setNicerFont(topicPanel);

        JScrollPane topPanel = new JScrollPane(topicPanel);
        topPanel.setBorder( BorderFactory.createEmptyBorder() );
        topPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        topPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        topPanel.setMaximumSize( new Dimension(4000, 75) );
        GUI.setPreferredSize(topPanel, 480, 50);

        content.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );

        JScrollPane bottomPanel = new JScrollPane(content);
        bottomPanel.setBorder( BorderFactory.createEmptyBorder() );
        bottomPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bottomPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        bottomPanel.setAutoscrolls(true);
        GUI.setPreferredSize(bottomPanel, 480, 300);

        Box leftBox = Box.createVerticalBox();
        leftBox.add(topPanel);
        leftBox.add(bottomPanel);

        add(leftBox);
        add(usersPanel);

        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.EAST, leftBox, 0, SpringLayout.WEST, usersPanel);
        layout.putConstraint(SpringLayout.WEST, leftBox, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, usersPanel, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, leftBox, 0, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, usersPanel, 0, SpringLayout.NORTH, leftBox);
        layout.putConstraint(SpringLayout.SOUTH, leftBox, 0, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.SOUTH, usersPanel, 0, SpringLayout.SOUTH, leftBox);

        setLayout(layout);

        if ( !Settings.getInstance().isViewEnabled("display-topic") )
            setTopicPanelVisibility(false);
    }

    private void createPopupMenu() {
        popup = new JPopupMenu();

        JMenuItem b1 = new JMenuItem("Poslat zprávu");
        b1.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChatByClick(selectedPopupUser);
            }
        });

        JMenuItem b2 = new JMenuItem("Zobrazit údaje");
        b2.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getServerTab().getConnection().whois(selectedPopupUser);
            }
        });

        JMenuItem b3 = new JMenuItem("Vyhodit");
        b3.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getServerTab().getConnection().kick( getTabName(), selectedPopupUser);
            }
        });

        JMenuItem b4 = new JMenuItem("Zakázat");
        b4.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getServerTab().getConnection().ban( getTabName(), selectedPopupUser);
            }
        });

        JMenuItem b5 = new JMenuItem("Zakázat a vyhodit");
        b5.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getServerTab().getConnection().ban( getTabName(), selectedPopupUser);
                getServerTab().getConnection().kick( getTabName(), selectedPopupUser);
            }
        });

        popup.add(b1);
        popup.add(b2);
        popup.add(b3);
        popup.add(b4);
        popup.add(b5);

        popupListener = new PopupListener();
        userList.addMouseListener(popupListener);
    }

    class PopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            openPrivateChat(e);
            showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            if ( !e.isPopupTrigger() )
                return;

            String nickname = getNicknameWhereClicked(e);
            if (nickname == null)
                return;

            selectedPopupUser = nickname;
            popup.show( e.getComponent() , e.getX() , e.getY() );
        }

        private void openPrivateChat(MouseEvent e) {
            if ( e.getClickCount() < 2 )
                return;

            openChatByClick(e);
        }

    }

    public String getNicknameWhereClicked(MouseEvent e) {
        // Nejblizsi prvek u mista, kde bylo kliknuto
        int cell = userList.locationToIndex( e.getPoint() );
        if (cell == -1)
            return null;

        // Opravdu bylo kliknuto na prvek, nebo jen "nekam blizko" nej?
        Rectangle r = userList.getCellBounds(cell, cell);
        if ( !r.contains( e.getPoint() ) )
            return null;

        // Získá přezdívku a zároven označí daný element
        // (při kliknutí pravým tlačítkem se neoznačí).
        User user = usersModel.getElementAt(cell);
        String nickname = user.getNickname();
        userList.setSelectedIndex(cell);

        return nickname;
    }

    public void openChatByClick(MouseEvent e) {
        String nickname = getNicknameWhereClicked(e);
        openChatByClick(nickname);
    }

    public void openChatByClick(String nickname) {
        if (nickname == null)
            return;

        PrivateChatTab tab = getServerTab().getPrivateChatByName(nickname);
        if (tab == null)
            tab = getServerTab().createPrivateChatTab(nickname);

        tab.setFocus();
    }

    public void setUsers(User[] users) {
        usersModel.clear();

        Runnable adder = new Runnable () {
            @Override
            public void run () {
                usersModel.add( usersQueue.poll() );
            }
        };

        try {
            for (User user : users) {
                usersQueue.add(user);
                if ( SwingUtilities.isEventDispatchThread() )
                    SwingUtilities.invokeLater(adder);
                else
                    SwingUtilities.invokeAndWait(adder);
            }
        } catch (InterruptedException | InvocationTargetException e) {
            ClientLogger.log("Chyba pri nastavovani seznamu uzivatelu: " + e.getMessage(), ClientLogger.ERROR);
        }
    }

    public String getCompleteNickname(String partialNick) {
        return usersModel.getCompleteNickname(partialNick);
    }

    public String getNickAfter(String nickname) {
        return usersModel.getNickAfter(nickname);
    }

    private void setTopic(String topic) {
        if (topic == null || topic.trim().length() == 0)
            topic = HTML.italic("Diskusní téma není nastaveno.");
        else
            topic = HTML.bold(topic);

        topicPanel.setText(topic);
    }

    public void setTopicPanelVisibility(boolean visible) {
        topicPanel.getParent().getParent().setVisible(visible);
        validate();
    }

    @Override
    public String getChannelName() {
        return tabName;
    }

    @Override
    public boolean contains(String nickname) {
        return usersModel.contains(nickname);
    }

    @Override
    public void messageReceived(String sender, String message) {
        message = HTML.escapeTags(message);

        String myNick = getServerTab().getConnection().getNick();
        if ( message.indexOf(myNick) > -1 )
            message = HTML.blue(message);

        appendText( HTML.bold(sender) + ": " + message);
    }

    @Override
    public void userListReceived(User[] users) {
        setUsers(users);
    }

    @Override
    public void userModeGranted(String initiator, String recipient, String mode) {
        String modeName = User.getTextualRepresentation(mode);
        appendInfo(initiator + " udělil " + recipient + " právo " + modeName);

        User user = usersModel.detachUser(recipient);
        user.addPrefixBasedOnMode(mode);
        usersModel.add(user);
    }

    @Override
    public void userModeRevoked(String initiator, String recipient, String mode) {
        String modeName = User.getTextualRepresentation(mode);
        appendInfo(initiator + " odebral " + recipient + " právo " + modeName);

        User user = usersModel.detachUser(recipient);
        user.removePrefixBasedOnMode(mode);
        usersModel.add(user);
    }

    @Override
    public void userChangesNick(String oldNick, String newNick) {
        boolean isMe = getServerTab().getConnection().getNick().equals(oldNick);
        String message;
        if (isMe) {
            message = "Nyní vystupujete pod přezdívkou "
                    + HTML.bold(newNick) + ".";
        } else {
            message = oldNick + " si změnil přezdívku na "
                    + HTML.bold(newNick) + ".";
        }
        appendInfo(message);

        User user = usersModel.detachUser(oldNick);
        user.setNickname(newNick);
        usersModel.add(user);
    }

    @Override
    public void userJoined(String nickname) {
        appendInfo(nickname + " přišel se připojil na " + getTabName() );
        usersModel.add( new User(nickname) );
    }

    @Override
    public void userLeft(String nickname) {
        boolean isMe = getServerTab().getConnection().getNick().equals(nickname);
        if (isMe) {
            getServerTab().removeChannelTab(this);
        } else {
            appendInfo(nickname + " odešel z místnosti." );
            usersModel.detachUser(nickname);
        }
    }

    @Override
    public void userQuit(String nickname, String reason) {
        appendInfo(nickname + " odešel (důvod: " + reason + ")");
        usersModel.detachUser(nickname);
    }

    @Override
    public void userKicked(String initiator, String recipient, String reason) {
        appendInfo(initiator + " vyhodil " + recipient + " (důvod: " + reason + ")");

        boolean isMe = getServerTab().getConnection().getNick().equals(recipient);
        if (isMe) {
            if ( Settings.getInstance().isEventEnabled("rejoin-after-kick") )
                getServerTab().getConnection().joinChannel( getTabName() );
            else
                getServerTab().removeChannelTab(this);
        } else {
            usersModel.detachUser(recipient);
        }
    }

    @Override
    public void topicChanged(String initiator, String topic) {
        String message = initiator + " nastavil téma na " + HTML.bold(topic);
        appendInfo(message);
        setTopic(topic);
    }

}
