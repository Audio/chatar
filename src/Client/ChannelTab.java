package Client;

import Client.TabContainer.PanelTypes;
import Connection.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;


public class ChannelTab extends AbstractTab {

    private JList users;
    private DefaultListModel usersModel;
    private JEditorPane infobox;
    private JEditorPane chat;
    private String tabName;
    private JPopupMenu popup;
    private MouseListener popupListener;
    private String selectedPopupUser;
    private LinkedList<String> tempUserNames;


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

        usersModel = new DefaultListModel();

        users = new JList(usersModel);
        users.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        users.setSelectedIndex(0);
        users.setLayoutOrientation(JList.VERTICAL);
        users.setSelectionBackground( new Color(230, 230, 255) );
        users.setCellRenderer( new ListImageRenderer() );

        JScrollPane panel = new JScrollPane(users);
        GUI.setPreferredSize(panel, 250, 355);

        scrollpanel.setViewportView(users);
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

        chat = GUI.createHTMLPane();

        chatscroll.setViewportView(chat);
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
                // getQuery().whois(selectedPopupUser);
            }
        } );

        JMenuItem tl3 = new JMenuItem("Vyhodit z místnosti");
        tl3.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String params = getTabName() + " " + selectedPopupUser;
                // getQuery().kick(params);
            }
        } );

        popup.add(tl1);
        popup.add(tl2);
        popup.add(tl3);

        popupListener = new PopupListener();
        users.addMouseListener(popupListener);

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


        // Priradi nazev zalozky
        tabName = channel;

        // Reset nazvu tematu
        setTopic(null);

        // Vytvori instanci zasobniku
        tempUserNames = new LinkedList<String>();
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
        int cell = users.locationToIndex( e.getPoint() );
        if (cell == -1)
            return null;

        // Opravdu bylo kliknuto na prvek, nebo jen "nekam blizko" nej?
        Rectangle r = users.getCellBounds(cell, cell);
        if ( !r.contains( e.getPoint() ) )
            return null;

        // Získá přezdívku a zároven označí daný element
        // (při kliknutí pravým tlačítkem se neoznačí).
        String nickname = (String) usersModel.getElementAt(cell);
        nickname = Output.User.removePrefix(nickname);
        users.setSelectedIndex(cell);

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
        if (pc == null) {
            try {
                MainWindow.getInstance().addTab(PanelTypes.PANEL_PRIVATE, nickname);
                pc = getServerTab().getPrivateChatByName(nickname);
            } catch (ClientException e) { }
        }
        pc.setFocus();
    }

    /**
     * Zobrazuje vystupni text - pridava jej na konec.
     */
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
     * Nastavuje (vypisuje) prihlase uzivatele v kanale.
     * Vstupni retezec si rozdeli do pole dle mezer.
     * Kazda hodnota v poli predstavuje jednoho uzivatele.
     */
    public void setUsers(String userlist) {

        String[] juzrs = userlist.split(" ");
        Arrays.sort(juzrs, String.CASE_INSENSITIVE_ORDER);
        usersModel.clear();

        Runnable adder = new Runnable () {

            @Override
            public void run () {
                String tempUserName = tempUserNames.poll();
                if (tempUserName != null) {
                    usersModel.addElement(tempUserName);
                }
                else {
                    // Provede seřazení uživatelů dle abecedy vzestupně
                    // TODO feature řazení při změně/přidání
                    // http://java.sun.com/developer/technicalArticles/J2SE/Desktop/sorted_jlist/
                }
            }

        };


        // Nastavi uzivatele
        try {
            for (int i = 0; i < juzrs.length; i++) {
                tempUserNames.add( juzrs[i] );
                if ( SwingUtilities.isEventDispatchThread() )
                    SwingUtilities.invokeLater(adder);
                else
                    SwingUtilities.invokeAndWait(adder);

                if (i+1 == juzrs.length) {
                    tempUserNames.add(null);
                    if ( SwingUtilities.isEventDispatchThread() )
                        SwingUtilities.invokeLater(adder);
                    else
                        SwingUtilities.invokeAndWait(adder);
                }

            }
        }
        catch (Exception e) {
            ClientLogger.log("Chyba pri nastavovani seznamu uzivatelu: " + e.getMessage(), ClientLogger.ERROR);
            e.printStackTrace();
        }

    }

    /**
     * Přidá uživatele do seznamu.
     */
    public void addUser(String nickname) {
        if ( !hasNick(nickname) )
            usersModel.addElement(nickname);
    }

    /**
     * Odebere uživatele se seznamu.
     */
    public void removeUser(String nickname) {

        int index = getNick(nickname);
        if (index < 0)
            return;

        usersModel.remove(index);

    }

    /**
     * Změní přezdívku vybraného uživatele.
     */
    public void changeUsersNickname(String oldname, String newname) {
        removeUser(oldname);
        addUser(newname);
    }

    /**
     * Nastavuje obsah horni casti - vypis aktualniho tematu.
     */
    public void setTopic(String topic) {

        if (topic == null || topic.trim().length() == 0)
            topic = "Diskusní téma není nastaveno.";
        else
            topic = Output.HTML.bold(topic);

        infobox.setText(topic);

    }

    @Override
    public String getTabName() {
        return "#" + tabName;
    }

    /**
     * Urci sama sebe jako aktualni panel.
     * Pouzito mj. pri zpracovani vstupu od uzivatele.
     */
    @Override
    public void setFocus() {
        changeNickname();
        GUI.getTabContainer().setSelectedComponent(this);
        GUI.getMenuBar().toggleDisconectFromServer(true);
        GUI.focusInput();
    }

    @Override
    public void clearContent() {
        chat.setText(null);
    }

    /**
     * Vrací odpověď, zda je uživatel se zvoleným nickem v této místnosti.
     * Kvůli prefixům nelze použít metodu (usersModel.)contains.
     */
    public boolean hasNick(String user) {
        user = Output.User.removePrefix(user).toLowerCase();

        int size = usersModel.size();
        for (int i=0; i < size; i++) {

            String nick = (String) usersModel.getElementAt(i);
            nick = Output.User.removePrefix(nick).toLowerCase();
            if ( nick.equals(user) )
                return true;

        }

        return false;
    }

    /**
     * Vrací index v seznamu, na kterém se nachází uživatel se zvoleným nickem.
     * Kvůli prefixům nelze použít metodu (usersModel.)indexOf.
     * Pokud uživatele nenajde, vrací hodnotu -1.
     */
    public int getNick(String user) {
        user = Output.User.removePrefix(user).toLowerCase();

        int size = usersModel.size();
        for (int i=0; i < size; i++) {

            String nick = (String) usersModel.getElementAt(i);
            nick = Output.User.removePrefix(nick).toLowerCase();
            if ( nick.equals(user) )
                return i;

        }

        return -1;
    }

    /**
     * Třída, která podporuje vkládání obrázků (ikon) do JListu.
     * Využito pro přehledné označení uživatelů s rozdílnými právy (operátor, moderátor..).
     */
    class ListImageRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {

            Component c = super.getListCellRendererComponent(list, value,
                                           index, isSelected, cellHasFocus);

            // přiřadí komponentě ikonu a text
            c = Output.User.addIcon(c, (String) value);

            return c;
        }

    }

}
