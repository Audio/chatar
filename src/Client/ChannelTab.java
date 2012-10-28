package Client;

import Connection.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.swing.*;


public class ChannelTab extends AbstractTab implements ChannelEventsListener {

    private JList userList;
    private UserListRenderer userListRenderer;
    private SortedListModel usersModel;
    private JEditorPane infobox;
    private JPopupMenu popup;
    private MouseListener popupListener;
    private String selectedPopupUser;
    private LinkedList<User> usersQueue;


    public ChannelTab(String channel, final ServerTab serverTab) {
        this.serverTab = serverTab;

        // Konstrukce panelu
        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        // Levy panel s vypisem pripojenych uzivatelu
        JPanel userspanel = new JPanel();
        userspanel.setLayout( new BoxLayout(userspanel, BoxLayout.LINE_AXIS) );
        GUI.setPreferredSize(userspanel, 200, 375);
        userspanel.setBorder( BorderFactory.createEmptyBorder(0, 7, 0, 0) );
        userspanel.setBackground(Color.WHITE);

        // Levy panel - Scrollpanel
        JScrollPane scrollpanel = new JScrollPane();
        scrollpanel.setBorder( BorderFactory.createEmptyBorder() );
        scrollpanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollpanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpanel.setAutoscrolls(true);

        userListRenderer = new UserListRenderer();
        usersModel = new SortedListModel();

        userList = new JList(usersModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setSelectedIndex(0);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setSelectionBackground( new Color(230, 230, 255) );
        userList.setCellRenderer(userListRenderer);

        JScrollPane panel = new JScrollPane(userList);
        GUI.setPreferredSize(panel, 250, 355);

        scrollpanel.setViewportView(userList);
        userspanel.add(scrollpanel);

        // Pravy obsahovy panel - vypis informaci o kanale; vypis chatu
        JPanel toppanel = new JPanel();
        toppanel.setLayout ( new BoxLayout(toppanel, BoxLayout.LINE_AXIS) );
        GUI.setPreferredSize(toppanel, 480, 80);
        toppanel.setMaximumSize( new Dimension(3200, 100) );
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

        JPanel bottompanel = new JPanel();
        bottompanel.setLayout( new BoxLayout(bottompanel, BoxLayout.LINE_AXIS) );
        GUI.setPreferredSize(bottompanel, 480, 305);
        bottompanel.setBorder( BorderFactory.createEmptyBorder() );

        JScrollPane chatscroll = new JScrollPane();
        chatscroll.setBorder( BorderFactory.createEmptyBorder() );
        chatscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chatscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatscroll.setAutoscrolls(true);

        chatscroll.setViewportView(content);
        bottompanel.add(chatscroll);

        // Slepeni horniho a spodniho praveho panelu
        Box rightbox = Box.createVerticalBox();
        rightbox.setBorder( BorderFactory.createEmptyBorder(0, 0, 0, 5) );
        GUI.setPreferredSize(rightbox, 485, 375);
        rightbox.add(toppanel);
        rightbox.add( Box.createRigidArea(new Dimension(0, 5)) );
        rightbox.add(bottompanel);

        // Popup menu - navazuje se az na konkretni objekty v seznamu
        popup = new JPopupMenu();

        JMenuItem tl1 = new JMenuItem("Poslat zprávu");
        tl1.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChatByClick(selectedPopupUser);
            }
        } );

        JMenuItem tl2 = new JMenuItem("Získat informace");
        tl2.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getServerTab().getConnection().whois(selectedPopupUser);
            }
        } );

        JMenuItem tl3 = new JMenuItem("Vyhodit z místnosti");
        tl3.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String params = getTabName() + " " + selectedPopupUser;
                InputHandler.handleKick(params);
            }
        } );

        popup.add(tl1);
        popup.add(tl2);
        popup.add(tl3);

        popupListener = new PopupListener();
        userList.addMouseListener(popupListener);

        // Uskladneni objektu to hlavniho panelu
        add(userspanel);
        add(rightbox);
        setBackground(Color.WHITE);

        //layout.putConstraint(SpringLayout.WEST, this, 0, SpringLayout.WEST, userspanel);
        layout.putConstraint(SpringLayout.WEST, rightbox, 5, SpringLayout.EAST, userspanel);
        layout.putConstraint(SpringLayout.EAST, rightbox, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, rightbox);
        layout.putConstraint(SpringLayout.NORTH, userspanel, 5, SpringLayout.NORTH, rightbox);
        layout.putConstraint(SpringLayout.SOUTH, userspanel, 0, SpringLayout.SOUTH, rightbox);

        tabName = channel;
        usersQueue = new LinkedList<>();

        setTopic(null);
    }

    /**
     * Třída rozbrazující PopupMenu - pouze pokud je kliknuto nad nějakou
     * přezdívkou v seznamu uživatelů.
     *
     * Z důvodu rozdílné implementace v různých operačních systémech
     * se kontroluje událost stisknutí i uvolnění tlačítka myši.
     */
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

            String nickname = getCellIndexFromListWhereClicked(e);
            if (nickname == null)
                return;

            selectedPopupUser = nickname;
            popup.show( e.getComponent() , e.getX() , e.getY() );
        }

        /**
         * Při dvojkliku na přezdívku nějakého uživatele otevírá novou záložku
         * v tabbed panelu - soukromý chat. Pokud je již otevřený, pouze
         * na něj hodí focus.
         */
        private void openPrivateChat(MouseEvent e) {
            if ( e.getClickCount() < 2 )
                return;

            openChatByClick(e);
        }

    }

    /**
     * Získá index prvku, na který bylo kliknuto. Dle indexu vrací přezdívku
     * uživatele ze seznamu. Pokud nebylo kliknuto na konkrétní položku
     * JListu, vrací null.
     */
    public String getCellIndexFromListWhereClicked(MouseEvent e) {
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

    /**
     * Otevírá okno se soukromým chatem a nastavuje mu focus.
     * Přezdívku získá z místa v seznamu uživatelů, kam bylo kliknuto.
     */
    public void openChatByClick(MouseEvent e) {
        String nickname = getCellIndexFromListWhereClicked(e);
        openChatByClick(nickname);
    }

    /**
     * Otevírá okno se soukromým chatem a nastavuje mu focus.
     * Přezdívka je udána parametrem.
     */
    public void openChatByClick(String nickname) {
        if (nickname == null)
            return;

        // Neklikl uživatel na svou přezdívku?
        // TODO check nick
        // if ( getConnection().isMe(nickname) )
            // return;

        PrivateChatTab pc = getServerTab().getPrivateChatByName(nickname);
        if (pc == null)
            pc = getServerTab().createPrivateChatTab(nickname);

        pc.setFocus();
    }

    public void setUsers(User[] users) {
        usersModel.clear();

        // TODO potrebuju v novym vlakne?
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
            e.printStackTrace();
        }
    }

    private void setTopic(String topic) {
        if (topic == null || topic.trim().length() == 0)
            topic = "Diskusní téma není nastaveno.";
        else
            topic = HTML.bold(topic);

        infobox.setText(topic);
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
        appendText(sender + ": " + message);
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
        appendInfo(nickname + " odešel z místnosti." );
        usersModel.detachUser(nickname);
    }

    @Override
    public void userQuit(String nickname, String reason) {
        appendInfo(nickname + " odešel (důvod: " + reason + ")");
        usersModel.detachUser(nickname);
    }

    @Override
    public void userKicked(String initiator, String recipient, String reason) {
        String myNick = getServerTab().getConnection().getNick();
        if ( recipient.equals(myNick) ) {
            getServerTab().removeChannelTab(this);
        } else {
            appendInfo(initiator + " vykopnul " + recipient + " (důvod: " + reason + ")");
            usersModel.detachUser(initiator);
        }
    }

    @Override
    public void userBanned(String initiator, String recipient) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void userUnbanned(String initiator, String recipient) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void topicChanged(String initiator, String topic) {
        String message = initiator + " nastavil téma na " + HTML.bold(topic);
        appendInfo(message);
        setTopic(topic);
    }

}
