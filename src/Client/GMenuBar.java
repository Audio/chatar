package Client;

import Connection.Input;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;

/**
 * Horní nabídka - menu aplikace. Sdružuje skupiny tlačítek.
 * Modifikace je prováděna dynamicky - na základě stavu spojení s IRC servery.
 * Dynamicky skrývaná/zobrazovaná tlačítka jsou uvedena jako atributy.
 *
 * @author Martin Fouček
 */
public class GMenuBar extends JMenuBar {

    // jednotliva menu
    private JMenu menuFile;
    private JMenu menuServer;
    private JMenu menuUser;

    // dynamicky modifovatelna tlacitka
    private JMenuItem disconnectFromAll;
    private JMenuItem disconnectFromServer;
    private JMenuItem closePanel;

    /**
     * Vytvori menu zakladniho formulare aplikace.
     */
    public GMenuBar () {

        // menu 1
        menuFile = new JMenu("Chatař");

        // Otevre dialog pro nastaveni.
        JMenuItem settings = new JMenuItem("Nastavení");
        settings.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK) );
        settings.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUI.showOptionsDialog(true);
            }
        });

        // Zavření aktuálně otevřeného panelu
        closePanel = new JMenuItem("Zavřít panel");
        closePanel.setVisible(false);
        closePanel.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK) );
        closePanel.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionClosePanel();
            }
        });

        // Ukonceni behu aplikace.
        JMenuItem close = new JMenuItem("Ukončit program");
        close.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK) );
        close.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Client.terminate(0);
            }
        });

        menuFile.add(settings);
        menuFile.add(closePanel);
        menuFile.add(close);

        // menu 2
        menuServer = new JMenu("Server");

        // Dialog pro nastaveni oblibenych serveru.
        JMenuItem connect = new JMenuItem("Připojit...");
        connect.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK) );
        connect.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUI.showServersDialog(true);
            }
        });

        // Odpojeni od vsech serveru.
        disconnectFromAll = new JMenuItem("Odpojit od všech serverů");
        disconnectFromAll.setVisible(false);
        disconnectFromAll.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK) );
        disconnectFromAll.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionDisconnectFromAll();
            }
        });

        // Odpojeni od aktualne vybraneho serveru.
        disconnectFromServer = new JMenuItem("Odpojit od ???");
        disconnectFromServer.setVisible(false);
        disconnectFromServer.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Input.handleQuit(null);
            }
        });

        menuServer.add(connect);
        menuServer.add(disconnectFromServer);
        menuServer.add(disconnectFromAll);

        // menu 3
        menuUser = new JMenu("Uživatel");
        menuUser.setVisible(false);

        // Dialog pro zmenu prezdivky
        JMenuItem change_nickname = new JMenuItem("Změnit přezdívku");
        change_nickname.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK) );
        change_nickname.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUI.showSetNicknameDialog();
            }
        });

        // Dialog pro nastaveni stavu AFK (away from keyboard)
        JMenuItem afk = new JMenuItem("Nastavit nepřítomnost (AFK)");
        afk.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUI.showSetAwayDialog();
            }
        });

        // Dialog pro nastaveni stavu Back (rusi AFK)
        JMenuItem back = new JMenuItem("Zrušit nepřítomnost");
        back.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUI.setBack();
            }
        });

        menuUser.add(change_nickname);
        menuUser.add(afk);
        menuUser.add(back);

        // naplni menubar
        add(menuFile);
        add(menuServer);
        add(menuUser);

    }

    /**
     * Zobrazuje / skrývá tlačítko pro odpojení od všech serverů.
     * 
     * @param setVisible
     */
    public void toggleDisconectFromAll (boolean setVisible) {
        disconnectFromAll.setVisible(setVisible);
    }

    /**
     * Zobrazuje / skrývá tlačítko pro odpojení od konkrétního serveru.
     *
     * @param setVisible
     */
    public void toggleDisconectFromServer (boolean setVisible) {

        if (setVisible) {
            String name = Input.currentTab.getServer().getTabName();
            disconnectFromServer.setText("Odpojit od " + name);
        }

        disconnectFromServer.setVisible(setVisible);

    }

    /**
     * Zobrazuje / skrývá uživatelskou nabídku (změna nicku, nastavení AFK).
     *
     * @param setVisible
     */
    public void toggleUserMenuBar (boolean setVisible) {
        menuUser.setVisible(setVisible);
    }

    /**
     * Zobrazuje / skrývá tlačítko na zavření panelu.
     *
     * @param setVisible
     */
    public void toggleClosePanel (boolean setVisible) {
        closePanel.setVisible(setVisible);
    }

    /**
     * Odpojeni od vsech serveru.
     */
    private void actionDisconnectFromAll () {
        
        Component[] array = GUI.getTab().getComponents();
        for (int i = 0; i < array.length; i++) {
            Component component = array[i];
            if ( component.getClass().getSimpleName().equals("GTabServer") ) {
                GTabServer c = (GTabServer) component;
                Input.handleQuit(c, null);
            }
        }

    }

    /**
     * Zavření aktuálně otevřeného panelu.
     */
    private void actionClosePanel () {

        GTabWindow tab = Input.currentTab;

        if (tab == null)
            return;

        String type = tab.getClass().getSimpleName();

        if ( type.equals("GTabChannel") ) {
            Input.handlePart(null);
        }
        else if ( type.equals("GTabPrivateChat") ) {
            tab.die();
            tab.killMyself();
        }
        else {
            Input.handleQuit(null);
        }


    }

}