package Favorites;

import Client.GUI;
import Dialog.TabbedWindow;
import MainWindow.MainWindow;
import Dialog.MessageDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class FavoritesWindow extends TabbedWindow implements WindowListener {

    static final long serialVersionUID = 1L;

    private static FavoritesWindow instance;

    private Storage storage;
    private List<Server> servers;
    private List<JLabel> tabLabels;


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
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setIconImage( new ImageIcon("img/favorites-icon.png").getImage() );

        createMainPanel();
        reloadServerList();

        addWindowListener(this);

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher( new KeyDispatcher() );
    }

    @Override
    protected JPanel createButtonPanel() {
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

    private void addTab(Form form) {
        JLabel label = new JLabel( form.getTitle() );
        GUI.setExactSize(label, 45, 20);
        tabLabels.add(label);

        tabPanel.add("", form);
        int index = tabPanel.getTabCount() - 1;
        tabPanel.setTabComponentAt(index, label);
    }

    private void reloadServerList() {
        tabLabels = new ArrayList<>();
        tabPanel.removeAll();
        servers = storage.load();
        for (Server s : servers) {
            Form form = new Form(this) ;
            form.setTitle( s.get("title") );
            form.setAddress( s.get("address") );
            form.setPort( s.get("port") );
            form.setNickname( s.get("nickname") );
            form.setChannels( s.get("channels") );
            addTab(form);
        }
    }

    private void actionAddServer() {
        Form form = new Form(this);
        form.setTitle("Nový server");
        servers.add( new Server() );
        addTab(form);
        tabPanel.setSelectedIndex( tabPanel.getTabCount() - 1 );
        form.focusTitle();
    }

    void serverTitleHasChanged(String newTitle) {
        int serverId = tabPanel.getSelectedIndex();
        tabLabels.get(serverId).setText(newTitle);
    }

    void actionDeleteCurrent() {
        Form form = (Form) tabPanel.getSelectedComponent();
        boolean delete = MessageDialog.confirmQuestion("Odstranění serveru",
                        "Potvrďte odstranění serveru " + form.getTitle() + ".");

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
        MainWindow.getInstance().getMainMenu().loadFavoriteServersList(servers);
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

    private class KeyDispatcher implements KeyEventDispatcher {

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if ( e.getID() == KeyEvent.KEY_RELEASED ) {
                boolean happenedOnTextField = e.getComponent() instanceof JTextField;
                boolean happenedOnTextArea = e.getComponent() instanceof JTextArea;

                if ( e.getKeyCode() == KeyEvent.VK_ENTER && happenedOnTextField ) {
                    saveList();
                    close();
                } else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE
                            && (happenedOnTextField || happenedOnTextArea) ) {
                    close();
                }
            }

            return false;
        }

    }

}
