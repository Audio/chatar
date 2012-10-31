package MainWindow;

import Client.Client;
import Config.GConfig;
import Connection.InputHandler;
import Dialog.MessageDialog;
import Favorites.FavoritesWindow;
import Favorites.ServerAddress;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;


public class MainMenu extends JMenuBar {

    static final long serialVersionUID = 1L;

    private JMenu userMenu;

    private JMenuItem joinToChannel;
    private JMenuItem disconnectFromAll;
    private JMenuItem disconnectFromServer;
    private JMenuItem clearChat;
    private JMenuItem closePanel;


    public MainMenu() {
        createFileMenu();
        createFavoritesMenu();
        createServerMenu();
        createUserMenu();
    }

    private void createFileMenu() {
        JMenu menu = new JMenu("Chatař");

        JMenuItem settings = new JMenuItem("Nastavení");
        settings.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK) );
        settings.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GConfig.getInstance().setVisible(true);
            }
        });

        clearChat = new JMenuItem("Vymazat chat");
        clearChat.setVisible(false);
        clearChat.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK) );
        clearChat.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputHandler.handleClear();
            }
        });

        closePanel = new JMenuItem("Zavřít panel");
        closePanel.setVisible(false);
        closePanel.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK) );
        closePanel.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionClosePanel();
            }
        });

        JMenuItem close = new JMenuItem("Ukončit program");
        close.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK) );
        close.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.terminate(0);
            }
        });

        menu.add(settings);
        menu.add(clearChat);
        menu.add(closePanel);
        menu.add(close);

        add(menu);
    }

    private void createFavoritesMenu() {
        JMenu menu = new JMenu("Oblíbené");

        // TODO seznam serverů
        // TODO po kliknutí se připojí

        JMenuItem edit = new JMenuItem("Editovat seznam");
        edit.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK) );
        edit.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FavoritesWindow.getInstance().setVisible(true);
            }
        });

        menu.add(edit);

        add(menu);
    }

    private void createServerMenu() {
        JMenu menu = new JMenu("Server");

        JMenuItem connect = new JMenuItem("Připojit...");
        connect.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK) );
        connect.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String addr = MessageDialog.inputQuestion("Rychlé připojení k serveru",
                                                             "Adresa serveru (:port)");
                ServerAddress sa = new ServerAddress(addr);
                if ( sa.isValid() )
                    MainWindow.getInstance().createServerTab(sa);
            }
        });

        joinToChannel = new JMenuItem("Připojit na kanál");
        joinToChannel.setVisible(false);
        joinToChannel.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK) );
        joinToChannel.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String channel = MessageDialog.inputQuestion("Připojení ke kanálu",
                                                             "Název kanálu");
                if (channel != null) {
                    if ( !channel.startsWith("#") )
                        channel = "#" + channel;

                    InputHandler.handleJoin(channel);
                }
            }
        });

        disconnectFromAll = new JMenuItem("Odpojit od všech serverů");
        disconnectFromAll.setVisible(false);
        disconnectFromAll.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK) );
        disconnectFromAll.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionDisconnectFromAll();
            }
        });

        disconnectFromServer = new JMenuItem("Odpojit od ???");
        disconnectFromServer.setVisible(false);
        disconnectFromServer.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputHandler.handleQuit(null);
            }
        });

        menu.add(connect);
        menu.add(joinToChannel);
        menu.add(disconnectFromServer);
        menu.add(disconnectFromAll);

        add(menu);
    }

    private void createUserMenu() {
        userMenu = new JMenu("Uživatel");
        userMenu.setVisible(false);

        JMenuItem changeNickname = new JMenuItem("Změnit přezdívku");
        changeNickname.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK) );
        changeNickname.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = MessageDialog.showSetNicknameDialog();
                if (nickname != null)
                    InputHandler.handleNick(nickname);
            }
        });

        JMenuItem afk = new JMenuItem("Nastavit nepřítomnost (AFK)");
        afk.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reason = MessageDialog.inputQuestion("Nastavení vlastní zprávy",
                        "Nastavte důvod své nepřítomnosti, nebo nechte pole prázdné.");
                if (reason != null)
                    InputHandler.handleAway( reason.trim() );
            }
        });

        JMenuItem notAfk = new JMenuItem("Zrušit nepřítomnost");
        notAfk.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputHandler.handleNotAway();
            }
        });

        userMenu.add(changeNickname);
        userMenu.add(afk);
        userMenu.add(notAfk);

        add(userMenu);
    }

    public void toggleConnectionActions(boolean setVisible) {
        if (setVisible) {
            String name = InputHandler.getCurrentServerTab().getTabName();
            disconnectFromServer.setText("Odpojit od " + name);
        }

        joinToChannel.setVisible(setVisible);
        disconnectFromAll.setVisible(setVisible);
        disconnectFromServer.setVisible(setVisible);
    }

    public void toggleUserMenuBar(boolean setVisible) {
        userMenu.setVisible(setVisible);
    }

    public void togglePanelActions(boolean setVisible) {
        clearChat.setVisible(setVisible);
        closePanel.setVisible(setVisible);
    }

    private void actionDisconnectFromAll() {
        Component[] array = MainWindow.getInstance().getTabContainer().getComponents();
        for (int i = 0; i < array.length; i++) {
            Component component = array[i];
            if (component instanceof ServerTab) {
                ServerTab c = (ServerTab) component;
                InputHandler.handleQuit(c, null);
            }
        }
    }

    private void actionClosePanel() {
        AbstractTab tab = MainWindow.getInstance().getActiveTab();

        if (tab == null)
            return;

        if (tab instanceof ChannelTab) {
            InputHandler.handlePart("");
        } else if (tab instanceof PrivateChatTab) {
            tab.getServerTab().removePrivateChatTab((PrivateChatTab) tab);
        } else {
            InputHandler.handleQuit(null);
        }
    }

}
