package Favorites;

import Client.GUI;
import MainWindow.MainWindow;
import Dialog.MessageDialog;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


// TODO ikonku pro todle vokno
public class FavoritesWindow extends JFrame implements WindowListener {

    private static FavoritesWindow instance;
    static final long serialVersionUID = 1L;

    private final int WINDOW_WIDTH = 500;
    private JTabbedPane tabPanel;

    private JTextField title;
    private JTextField address;
    private JTextField port;
    private JTextField nickname;
    private JTextArea channels;
    // TODO JPasswordField password (auth)

    private JButton delete;
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
        setSize(WINDOW_WIDTH, 300);

        createMainPanel();
        loadServerList();

        addWindowListener(this);
    }

    private void createMainPanel() {
        JPanel contentPanel = (JPanel) getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPanel.setLayout(layout);

        tabPanel = new JTabbedPane(JTabbedPane.LEFT);
        GUI.setExactSize(tabPanel, WINDOW_WIDTH, 200);

        JPanel buttonPanel = createButtonPanel();
        GUI.setExactSize(buttonPanel, WINDOW_WIDTH, 100);

        layout.putConstraint(SpringLayout.WEST, buttonPanel, 0, SpringLayout.WEST, tabPanel);
        layout.putConstraint(SpringLayout.NORTH, buttonPanel, 0, SpringLayout.SOUTH, tabPanel);

        contentPanel.add(tabPanel);
        contentPanel.add( Box.createVerticalGlue() );
        contentPanel.add(buttonPanel);
    }

    private JPanel createTabContent() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        final int SPACER = 15;
        final int LABEL_SPACER = 22;

        JLabel titleLabel = new JLabel("Název v Oblíbených:");
        JLabel addressLabel = new JLabel("Adresa serveru:");
        JLabel portLabel = new JLabel("Port:");
        JLabel nicknameLabel = new JLabel("Přezdívka:");
        JLabel channelsLabel = new JLabel("<html>Automaticky připojit<br>do těchto kanálů:"
                                        + "<br><br>(oddělujte čárkou)</html>");

        Box labels = Box.createVerticalBox();
        labels.add( Box.createRigidArea( new Dimension(0, 5)) );
        labels.add(titleLabel);
        labels.add( Box.createRigidArea( new Dimension(0, LABEL_SPACER)) );
        labels.add(addressLabel);
        labels.add( Box.createRigidArea( new Dimension(0, LABEL_SPACER)) );
        labels.add(nicknameLabel);
        labels.add( Box.createRigidArea( new Dimension(0, LABEL_SPACER)) );
        labels.add(channelsLabel);
        panel.add(labels);

        title = new JTextField(15);
        address = new JTextField();
        port = new JTextField(5);
        nickname = new JTextField();
        channels = new JTextArea(3, 35);
        channels.setFont( title.getFont() );
        channels.setBorder( title.getBorder() );

        Box inputsLeft = Box.createVerticalBox();
        inputsLeft.add(title);
        inputsLeft.add( Box.createRigidArea( new Dimension(0, SPACER)) );
        inputsLeft.add(address);
        inputsLeft.add( Box.createRigidArea( new Dimension(0, SPACER)) );
        inputsLeft.add(nickname);

        delete = new JButton("Odebrat");
        delete.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionDeleteCurrent();
            }
        });

        Box portBox = Box.createHorizontalBox();
        portBox.add(portLabel);
        portBox.add( Box.createRigidArea( new Dimension(SPACER, 0)) );
        portBox.add(port);

        Box inputsRight = Box.createVerticalBox();
        inputsRight.setAlignmentY(BOTTOM_ALIGNMENT);
        inputsRight.setAlignmentX(RIGHT_ALIGNMENT);
        inputsRight.add(delete);
        inputsRight.add( Box.createRigidArea( new Dimension(0, SPACER)) );
        inputsRight.add(portBox);

        Box inputs = Box.createHorizontalBox();
        inputs.add(inputsLeft);
        inputs.add( Box.createRigidArea( new Dimension(SPACER, 0)) );
        inputs.add(inputsRight);

        Box rightSide = Box.createVerticalBox();
        rightSide.add(inputs);
        rightSide.add( Box.createRigidArea( new Dimension(0, SPACER)) );
        rightSide.add(channels);

        Box all = Box.createHorizontalBox();
        all.add(labels);
        all.add( Box.createRigidArea( new Dimension(SPACER, 0)) );
        all.add(rightSide);

        panel.add(all);

        return panel;
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
        buttons.add( Box.createRigidArea( new Dimension(60, 0)) );
        buttons.add(create);
        buttons.add( Box.createRigidArea( new Dimension(WINDOW_WIDTH - 350, 0)) );
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
        JPanel panel = createTabContent();
        tabPanel.add("Brambora", panel);
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
