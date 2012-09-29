package Client;

import Config.Servers;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Okno pro nastaveni seznamu oblibenych serveru.
 *
 * @author Martin Fouček
 */
public class GServers extends JFrame implements WindowListener {

    private JList list;
    private DefaultListModel servers;
    private JButton connect;
    private JButton delete;
    private boolean changed;

    /**
     * Konstruktor. Vytváří GUI.
     */
    public GServers() {

        // Nastaveni okna
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle("Připojit k serveru");
        setResizable(false);
        setSize(385, 400);

        getContentPane().setLayout( new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS) );

        // Hlavni panel - obsahuje scrollpanel a tlacitka
        JPanel main_panel = new JPanel();
        main_panel.setLayout( new BoxLayout(main_panel, BoxLayout.LINE_AXIS) );
        main_panel.setBorder( BorderFactory.createEmptyBorder(10, 10, 0, 10) );

        // Scrollpanel
        servers = new DefaultListModel();
        loadServerList();

        list = new JList(servers);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount( servers.size() );
        list.setBackground( new Color(255, 255, 255) );

        JScrollPane panel = new JScrollPane(list);
        GUI.setExactSize(panel, 250, 355);

        // Box s tlacitky
        connect = new JButton("Připojit");
        connect.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionConnect();
            }
        });

        JButton create = new JButton("Přidat");
        create.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionAppend();
            }
        });

        delete = new JButton("Smazat");
        delete.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionDelete();
            }
        });

        JButton cancel = new JButton("Storno");
        cancel.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });

        setButtonSize(connect);
        setButtonSize(create);
        setButtonSize(delete);
        setButtonSize(cancel);

        Box box = Box.createVerticalBox();
        box.add( connect );
        box.add( Box.createRigidArea( new Dimension(0, 6)) );
        box.add( create );
        box.add( Box.createRigidArea( new Dimension(0, 6)) );
        box.add( delete );
        box.add( Box.createRigidArea( new Dimension(0, 6)) );
        box.add( cancel );

        // Pridani komponent do panelu
        main_panel.add(panel);
        main_panel.add( Box.createHorizontalGlue() );
        main_panel.add(box);
        getContentPane().add(main_panel);

        setLocationRelativeTo( GUI.getForm() );
        setVisible(true);
        changed = false;

        addWindowListener(this);
        //setAlwaysOnTop(true);

    }

    /**
     * Nastavi defaultni velikost pro vsechna tlacitka.
     *
     * @param c
     */
    public static void setButtonSize (Component c) {
        GUI.setExactSize(c, 100, 30);
    }

    /**
     * Pri adresu serveru do seznamu.
     *
     * @param address
     */
    private void addServer (String address) {
        servers.addElement(address);
    }

    /**
     * Nacte seznam serveru ze souboru.
     */
    private void loadServerList () {

        String[] srv = Servers.loadFile();
        for (int i = 0; i < srv.length; i++) {
            addServer(srv[i]);
        }

    }

    /**
     * Pripoji se k vybranemu serveru.
     */
    private void actionConnect() {

        String server = (String) list.getSelectedValue();
        try {
            GUI.addTab(GTab.PANEL_SERVER, server);
            close();
        }
        catch (Exception e) {
            new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Chyba aplikace", "Připojení nelze uskutečnit.");
        }

    }

    /**
     * Skryje okno. Zrusi pripadne zmeny.
     */
    private void actionCancel () {
        changed = false;
        setVisible(false);
    }

    /**
     * Odstrani aktualne vybrany prvek seznamu.
     */
    private void actionDelete () {

        int index = list.getSelectedIndex();
        servers.remove(index);

        int size = servers.getSize();

        if (size == 0) {
            // znemozni pripojeni / vyber
            connect.setEnabled(false);
            delete.setEnabled(false);
        }
        else {
            if (index == servers.getSize()) {
                // odstraneni posledni pozice
                index--;
            }

            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
            connect.setEnabled(true);
            delete.setEnabled(true);
        }

        changed = true;

    }

    /**
     * Prida novy prvek seznamu.
     */
    private void actionAppend () {

        DefaultListModel model = (DefaultListModel) list.getModel();
        int pos = model.getSize();

        String msg = "Vložte adresu serveru ve tvaru 'irc.adresa.cz'."
                   + "\n\nUpozornění: výchozí port je 6667.\nPro určení jiného portu zadejte adresu ve tvaru 'irc.adresa.cz:port'."
                   + "\n\nNapř.: irc.mmoirc.com nebo irc.mmoirc.com:6667"
                   + "\n\n";
        MessageDialog win = new MessageDialog(MessageDialog.GROUP_INPUT, MessageDialog.TYPE_QUESTION, "Přidání adresy serveru", msg);

        if (win.strConfirm != null && win.strConfirm.length() > 0) {
            model.add(pos, win.strConfirm);
            connect.setEnabled(true);
            delete.setEnabled(true);
        }

        changed = true;

    }

    /**
     * Skryva okno. Dava prikaz k ulozeni hodnot do souboru.
     */
    private void close () {
        setVisible(false);
        if (changed)
            saveList();
    }

    /**
     * Pri zavirani okna (skryti) uklada obsah serveru do souboru
     * (jen pokud doslo ke zmene).
     */
    private void saveList () {

        if (!changed)
            return;

        DefaultListModel model = (DefaultListModel) list.getModel();
        Servers.reset();
        for (int i = 0; i < model.getSize(); i++) {
            Servers.addServer( (String) model.get(i));
        }
        Servers.saveFile();

        changed = false;

    }

    /**
     * Pred uzavrenim (skrytim) ulozi konfiguracni soubor (je-li treba).
     *
     * @param e
     */
    public void windowClosing(WindowEvent e) {
        close();
    }

    // nevyuzite metody rozhrani WindowListener
    /**
     * Nevyužito.
     *
     * @param e
     */
    public void windowOpened      (WindowEvent e) { }
    /**
     * Nevyužito.
     *
     * @param e
     */
    public void windowClosed      (WindowEvent e) { }
    /**
     * Nevyužito.
     *
     * @param e
     */
    public void windowIconified   (WindowEvent e) { }
    /**
     * Nevyužito.
     *
     * @param e
     */
    public void windowDeiconified(WindowEvent e) { }
    /**
     * Nevyužito.
     *
     * @param e
     */
    public void windowActivated   (WindowEvent e) { }
    /**
     * Nevyužito.
     *
     * @param e
     */
    public void windowDeactivated(WindowEvent e) { }

}
