package Client;

import Connection.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

/**
 * Panel s výpisem informací o/ze kanálu (channel).
 * Grafická komponenta, s serverem komunikuje přes na instanci ServerTab.
 *
 * @author Martin Fouček
 */
public class ChannelTab extends AbstractTab {

    private JList users;
    private DefaultListModel usersModel;
    private JEditorPane infobox;
    private JEditorPane chat;
    private String tabName;
    private ServerTab server;
    private JPopupMenu popup;
    private MouseListener popupListener;
    private String selectedPopupUser;
    private LinkedList<String> tempUserNames;

    /**
     * Konstruktor. Vytváří GUI a lepí se na objekt ServerTab.
     *
     * @param channel
     */
    public ChannelTab(String channel) {

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
            public void actionPerformed(ActionEvent e) {
                openChatByClick(selectedPopupUser);
            }
        } );

        JMenuItem tl2 = new JMenuItem("Získat informace");
        tl2.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // getQuery().whois(selectedPopupUser);
            }
        } );

        JMenuItem tl3 = new JMenuItem("Vyhodit z místnosti");
        tl3.addActionListener( new ActionListener() {
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

        /**
         * Odpálí se při kliknutí myší.
         *
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            openPrivateChat(e);
            showPopup(e);
        }

        /**
         * Odpálí se při uvolnění tlačítka myši.
         *
         * @param e
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        /**
         * Zobrazí popup menu.
         *
         * @param e
         */
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
         *
         * @param e
         */
        private void openPrivateChat (MouseEvent e) {

            if ( e.getClickCount() < 2 )
                return;

            openChatByClick(e);

        }

    }

    /**
     * Získá index prvku, na který bylo kliknuto. Dle indexu vrací přezdívku
     * uživatele ze seznamu. Pokud nebylo kliknuto na konkrétní položku
     * JListu, vrací null.
     *
     * @param e
     * @return
     */
    public String getCellIndexFromListWhereClicked (MouseEvent e) {

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
     *
     * @param e
     */
    public void openChatByClick (MouseEvent e) {
        String nickname = getCellIndexFromListWhereClicked(e);
        openChatByClick(nickname);
    }

    /**
     * Otevírá okno se soukromým chatem a nastavuje mu focus.
     * Přezdívka je udána parametrem.
     *
     * @param nickname
     */
    public void openChatByClick (String nickname) {

        if (nickname == null)
            return;

        // Neklikl uživatel na svou přezdívku?
        if ( getConnection().isMe(nickname) )
            return;

        PrivateChatTab pc = getServerTab().getPrivateChatByName(nickname);
        if (pc == null) {
            try {
                MainWindow.getInstance().addTab(TabContainer.PANEL_PRIVATE, nickname);
                pc = getServerTab().getPrivateChatByName(nickname);
            } catch (ClientException e) { }
        }
        pc.setFocus();

    }

    /**
     * Vraci referenci na objekt Connection,
     * pres ktery komunikuje.
     *
     * @return
     */
    @Override
    public DeprecatedConnection getConnection () {
        return server.getConnection();
    }

    /**
     * Vraci referenci na server (ServerTab), pres ktery je kanal napojen.
     * V pripade serverove mistnosti vraci ref. na sebe.
     *
     * @return
     */
    @Override
    public ServerTab getServerTab () {
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
     * Nastavuje (vypisuje) prihlase uzivatele v kanale.
     * Vstupni retezec si rozdeli do pole dle mezer.
     * Kazda hodnota v poli predstavuje jednoho uzivatele.
     *
     * @param userlist
     */
    public void setUsers (String userlist) {

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
     *
     * @param nickname
     */
    public void addUser (String nickname) {
        if ( !hasNick(nickname) )
            usersModel.addElement(nickname);
    }

    /**
     * Odebere uživatele se seznamu.
     *
     * @param nickname
     */
    public void removeUser (String nickname) {

        int index = getNick(nickname);
        if (index < 0)
            return;

        usersModel.remove(index);

    }

    /**
     * Změní přezdívku vybraného uživatele.
     *
     * @param oldname
     * @param newname
     */
    public void changeUsersNickname (String oldname, String newname) {
        removeUser(oldname);
        addUser(newname);
    }

    /**
     * Nastavuje obsah horni casti - vypis aktualniho tematu.
     *
     * @param topic
     */
    public void setTopic (String topic) {

        if (topic == null || topic.trim().length() == 0)
            topic = "Diskusní téma není nastaveno.";
        else
            topic = Output.HTML.bold(topic);

        infobox.setText(topic);

    }

    /**
     * Vraci svuj nazev.
     *
     * @return
     */
    @Override
    public String getTabName() {
        return "#" + tabName;
    }

    /**
     * Kanal se prilepi na svuj server (ServerTab).
     * Zatim umisteno v konstruktoru.
     *
     * @param channel
     */
    @Override
    public void adapt (String channel) {

        if (Input.getCurrentServer() == null) {
            Input.showNoConnectionError();
        }
        else {
            if (channel.startsWith("#")) {
                channel = channel.substring(1);
            }
            // TODO prasečina
            server = Input.getCurrentServer();
            Input.getCurrentServer().channels.add(this);
            getConnection().setTab(this);
        }

    }

    /**
     * Ukonci cinnost panelu, ale nezavre ho.
     */
    @Override
    public void die() {
        server.channels.remove(this);
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
        GUI.getTabContainer().setSelectedComponent(this);
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
     * Vrací odpověď, zda je uživatel se zvoleným nickem v této místnosti.
     * Kvůli prefixům nelze použít metodu (usersModel.)contains.
     *
     * @param user
     * @return
     */
    public boolean hasNick (String user) {

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
     *
     * @param user
     * @return
     */
    public int getNick (String user) {

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
     *
     * @author Martin Fouček
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
