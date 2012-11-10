package MainWindow;

import Client.ClientLogger;
import Connection.InputHandler;
import Dialog.MessageDialog;
import Favorites.*;
import Settings.*;
import java.awt.Component;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;


public class MainMenu extends JMenuBar {

    static final long serialVersionUID = 1L;

    private JMenu userMenu;
    private JMenu favoritesMenu;

    private JMenuItem joinToChannel;
    private JMenuItem disconnectFromAll;
    private JMenuItem disconnectFromServer;
    private JMenuItem clearChat;
    private JMenuItem closePanel;
    private JMenuItem editFavorites;


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
                SettingsWindow.getInstance().setVisible(true);
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
                boolean ask = Settings.getInstance().isEventEnabled("ask-for-quit");
                boolean close = ask ? MessageDialog.confirmQuestion("Ukončit aplikaci",
                                            "Opravdu chcete ukončit aplikaci?") : true;
                if (close) {
                    ClientLogger.quit();
                    System.exit(0);
                }
            }
        });

        menu.add(settings);
        menu.add(clearChat);
        menu.add(closePanel);
        menu.add(close);

        add(menu);
    }

    private void createFavoritesMenu() {
        favoritesMenu = new JMenu("Oblíbené");

        editFavorites = new JMenuItem("Editovat seznam");
        editFavorites.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK) );
        editFavorites.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FavoritesWindow.getInstance().setVisible(true);
            }
        });

        Storage storage = new Storage();
        loadFavoriteServersList( storage.load() );

        add(favoritesMenu);
    }

    public void loadFavoriteServersList(List<Server> servers) {
        favoritesMenu.removeAll();

        if ( servers.size() > 0 ) {
            for (final Server s : servers) {
                JMenuItem item = new JMenuItem( s.get("title") );
                item.addActionListener( new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ConnectionDetails cd = ConnectionDetails.fromServer(s);
                        MainWindow.getInstance().createServerTab(cd);
                    }
                });

                favoritesMenu.add(item);
            }

            favoritesMenu.addSeparator();
        }

        favoritesMenu.add(editFavorites);
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
                ConnectionDetails sa = ConnectionDetails.fromAddress(addr);
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
