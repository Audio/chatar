package Client;

import Connection.InputHandler;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;


public class GMenuBar extends JMenuBar {

    private JMenu userMenu;

    private JMenuItem disconnectFromAll;
    private JMenuItem disconnectFromServer;
    private JMenuItem closePanel;


    public GMenuBar() {
        createFileMenu();
        createServerMenu();
        createUserMenu();
    }

    private void createFileMenu() {
        JMenu menu = new JMenu("Chatař");

        JMenuItem settings = new JMenuItem("Nastavení");
        settings.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK) );
        settings.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GConfig.getInstance().setVisible(true);
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
        menu.add(closePanel);
        menu.add(close);

        add(menu);
    }

    private void createServerMenu() {
        JMenu menu = new JMenu("Server");

        JMenuItem connect = new JMenuItem("Připojit...");
        connect.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK) );
        connect.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GServers.getInstance().setVisible(true);
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

    public void toggleDisconectFromAll(boolean setVisible) {
        disconnectFromAll.setVisible(setVisible);
    }

    public void toggleDisconectFromServer(boolean setVisible) {
        if (setVisible) {
            String name = MainWindow.getInstance().getActiveTab().getServerTab().getTabName();
            disconnectFromServer.setText("Odpojit od " + name);
        }

        disconnectFromServer.setVisible(setVisible);
    }

    public void toggleUserMenuBar(boolean setVisible) {
        userMenu.setVisible(setVisible);
    }

    public void toggleClosePanel(boolean setVisible) {
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
            InputHandler.handlePart(null);
        } else if (tab instanceof PrivateChatTab) {
            tab.getServerTab().removePrivateChatTab((PrivateChatTab) tab);
        } else {
            InputHandler.handleQuit(null);
        }
    }

}
