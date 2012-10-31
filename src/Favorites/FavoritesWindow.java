package Favorites;

import Client.GUI;
import MainWindow.MainWindow;
import Dialog.MessageDialog;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


// TODO ikonku pro todle vokno
public class FavoritesWindow extends JFrame implements WindowListener {

    private static FavoritesWindow instance;
    static final long serialVersionUID = 1L;

    private final int WINDOW_WIDTH = 500;
    private JTabbedPane tabPanel;

    // TODO nepotřeba
    private boolean changed;


    public static FavoritesWindow getInstance() {
        if (instance == null)
            instance = new FavoritesWindow();

        return instance;
    }

    private FavoritesWindow() {
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle("Seznam oblíbených serverů");
        setResizable(false);
        setSize(WINDOW_WIDTH, 290);

        createMainPanel();
        loadServerList();

        addWindowListener(this);
    }

    private void createMainPanel() {
        JPanel contentPanel = (JPanel) getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPanel.setLayout(layout);

        tabPanel = new JTabbedPane(JTabbedPane.LEFT);
        tabPanel.setBorder( new EmptyBorder(10, 10, 0, 10) );
        GUI.setExactSize(tabPanel, WINDOW_WIDTH - 10, 210);

        JPanel buttonPanel = createButtonPanel();
        GUI.setExactSize(buttonPanel, WINDOW_WIDTH, 60);

        layout.putConstraint(SpringLayout.WEST, buttonPanel, 0, SpringLayout.WEST, tabPanel);
        layout.putConstraint(SpringLayout.NORTH, buttonPanel, 0, SpringLayout.SOUTH, tabPanel);

        contentPanel.add(tabPanel);
        contentPanel.add( Box.createVerticalGlue() );
        contentPanel.add(buttonPanel);
    }

    private JPanel createButtonPanel() {
        JButton create = new JButton("Přidat server...");
        create.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAddServer();
            }
        });

        JButton cancel = new JButton("Storno");
        cancel.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });

        JButton save = new JButton("Uložit změny");
        save.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println("neumim!!");
                // actionSave();
            }
        });

        Box buttons = Box.createHorizontalBox();
        buttons.add( Box.createRigidArea( new Dimension(70, 0)) );
        buttons.add(create);
        buttons.add( Box.createRigidArea( new Dimension(WINDOW_WIDTH - 370, 0)) );
        buttons.add(cancel);
        buttons.add( Box.createRigidArea( new Dimension(10, 0)) );
        buttons.add(save);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.X_AXIS) );
        buttonPanel.add(buttons);

        return buttonPanel;
    }

    private void addServer(String address) {
        // servers.addElement(address);
    }

    private void loadServerList() {
        tabPanel.add("Rizon", new Form() );
        tabPanel.add("Quakenet", new Form() );
        /*
        String[] srv = Storage.loadFile();
        for (int i = 0; i < srv.length; i++) {
            addServer(srv[i]);
        }
        */
    }

    private void actionCancel() {
        changed = false;
        setVisible(false);
    }

    private void actionDeleteCurrent() {
        /*
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
        */
    }

    private void actionAddServer() {
        /*
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
        */
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

        /*
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        Storage.reset();
        for (int i = 0; i < model.getSize(); i++) {
            Storage.addServer( model.get(i) );
        }
        Storage.saveFile();
        */

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
