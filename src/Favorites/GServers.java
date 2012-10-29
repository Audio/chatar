package Favorites;

import Client.GUI;
import MainWindow.MainWindow;
import Dialog.MessageDialog;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GServers extends JFrame implements WindowListener {

    private static GServers instance;
    static final long serialVersionUID = 1L;

    private JList<String> list;
    private DefaultListModel<String> servers;
    private JButton connect;
    private JButton delete;
    private boolean changed;


    public static GServers getInstance() {
        if (instance == null)
            instance = new GServers();

        return instance;
    }

    private GServers() {

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
        servers = new DefaultListModel<>();
        loadServerList();

        list = new JList<>(servers);
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
            @Override
            public void actionPerformed(ActionEvent e) {
                actionConnect();
            }
        });

        JButton create = new JButton("Přidat");
        create.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAppend();
            }
        });

        delete = new JButton("Smazat");
        delete.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionDelete();
            }
        });

        JButton cancel = new JButton("Storno");
        cancel.addActionListener( new ActionListener() {
            @Override
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

        setLocationRelativeTo( MainWindow.getInstance() );
        setVisible(true);
        changed = false;

        addWindowListener(this);
        //setAlwaysOnTop(true);

    }

    /**
     * Nastavi defaultni velikost pro vsechna tlacitka.
     */
    public static void setButtonSize(Component c) {
        GUI.setExactSize(c, 100, 30);
    }

    /**
     * Pri adresu serveru do seznamu.
     */
    private void addServer(String address) {
        servers.addElement(address);
    }

    /**
     * Nacte seznam serveru ze souboru.
     */
    private void loadServerList() {
        String[] srv = Servers.loadFile();
        for (int i = 0; i < srv.length; i++) {
            addServer(srv[i]);
        }
    }

    /**
     * Pripoji se k vybranemu serveru.
     */
    private void actionConnect() {
        String server = list.getSelectedValue();
        try {
            MainWindow.getInstance().createServerTab(server);
            close();
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.error("Chyba aplikace", "Připojení nelze uskutečnit.");
        }
    }

    /**
     * Skryje okno. Zrusi pripadne zmeny.
     */
    private void actionCancel() {
        changed = false;
        setVisible(false);
    }

    /**
     * Odstrani aktualne vybrany prvek seznamu.
     */
    private void actionDelete() {
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
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        int pos = model.getSize();

        String msg = "Vložte adresu serveru ve tvaru 'irc.adresa.cz'."
                   + "\n\nUpozornění: výchozí port je 6667.\nPro určení jiného portu zadejte adresu ve tvaru 'irc.adresa.cz:port'."
                   + "\n\nNapř.: irc.mmoirc.com nebo irc.mmoirc.com:6667"
                   + "\n\n";
        String address = MessageDialog.inputQuestion("Přidání adresy serveru", msg);

        if (address != null && address.trim().length() > 0) {
            model.add(pos, address);
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

        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        Servers.reset();
        for (int i = 0; i < model.getSize(); i++) {
            Servers.addServer( model.get(i) );
        }
        Servers.saveFile();

        changed = false;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        setLocationRelativeTo( MainWindow.getInstance() );
    }

    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }

}
