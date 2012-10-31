package Favorites;

import Client.GUI;
import MainWindow.MainWindow;
import Dialog.MessageDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


// TODO ikonku pro todle vokno
public class FavoritesWindow extends JFrame implements WindowListener {

    private static FavoritesWindow instance;
    static final long serialVersionUID = 1L;

    private final int WINDOW_WIDTH = 500;
    private Storage storage;
    private List<Server> servers;
    private JTabbedPane tabPanel;


    public static FavoritesWindow getInstance() {
        if (instance == null)
            instance = new FavoritesWindow();

        return instance;
    }

    private FavoritesWindow() {
        this.storage = new Storage();

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle("Seznam oblíbených serverů");
        setResizable(false);
        setSize(WINDOW_WIDTH, 290);

        createMainPanel();
        reloadServerList();

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
                saveList();
                close();
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

    private void reloadServerList() {
        tabPanel.removeAll();
        servers = storage.load();
        for (Server s : servers) {
            Form form = new Form(this) ;
            form.setTitle( s.get("title") );
            form.setAddress( s.get("address") );
            form.setPort( s.get("port") );
            form.setNickname( s.get("nickname") );
            form.setChannels( s.get("channels") );
            tabPanel.add(s.get("title"), form);
        }

    }

    private void actionAddServer() {
        Form form = new Form(this);
        servers.add( new Server() );
        tabPanel.add("Nový server", form);
        tabPanel.setSelectedIndex( tabPanel.getComponentCount() - 1 );
        form.focusTitle();
    }

    void serverTitleHasChanged(String newTitle) {
        int serverId = tabPanel.getSelectedIndex();
        tabPanel.setTitleAt(serverId, newTitle);
    }

    void actionDeleteCurrent() {
        Form form = (Form) tabPanel.getSelectedComponent();
        boolean delete = MessageDialog.confirmQuestion("Odstranění serveru",
                        "Potvrďte odstranění serveru " + form.getTitle() + "." );

        if (delete) {
            int serverId = tabPanel.getSelectedIndex();
            servers.remove(serverId);
            tabPanel.remove(serverId);
        }
    }

    private void saveList() {
        int serverCount = servers.size();
        for (int i = 0; i < serverCount; ++i)
            storeDetails(i);

        storage.store(servers);
    }

    private void storeDetails(int serverId) {
        Server s = servers.get(serverId);
        Form form = (Form) tabPanel.getComponentAt(serverId);
        s.set("title", form.getTitle() );
        s.set("address", form.getAddress() );
        s.set("port", form.getPort() );
        s.set("nickname", form.getNickname() );
        s.set("channels", form.getChannels() );
    }

    private void actionCancel() {
        reloadServerList();
        close();
    }

    private void close() {
        setVisible(false);
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
