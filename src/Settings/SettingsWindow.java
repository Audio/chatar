package Settings;

import Client.GUI;
import MainWindow.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class SettingsWindow extends TabbedWindow implements WindowListener {

    static final long serialVersionUID = 1L;

    private static SettingsWindow instance;

    private Settings settings;
    private UserForm userForm;
    private EventsForm eventsForm;
    private ViewForm viewForm;
    private BlockedForm blockedForm;


    public static SettingsWindow getInstance() {
        if (instance == null)
            instance = new SettingsWindow();

        return instance;
    }

    private SettingsWindow() {
        this.settings = new Settings();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle("Osobní nastavení");
        setResizable(false);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setIconImage( new ImageIcon("img/settings-icon.png").getImage() );

        createMainPanel();
        loadSettings();

        addWindowListener(this);
    }

    @Override
    protected JPanel createButtonPanel() {
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
                saveSettings();
                close();
            }
        });

        Box buttons = Box.createHorizontalBox();
        buttons.add( Box.createRigidArea( new Dimension(WINDOW_WIDTH - 190, 0)) );
        buttons.add(cancel);
        buttons.add( Box.createRigidArea( new Dimension(10, 0)) );
        buttons.add(save);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.X_AXIS) );
        buttonPanel.add(buttons);

        return buttonPanel;
    }

    private void addTab(Component c, String title) {
        JLabel label = new JLabel(title);
        GUI.setExactSize(label, 100, 20);

        tabPanel.add("", c);
        int index = tabPanel.getTabCount() - 1;
        tabPanel.setTabComponentAt(index, label);
    }

    private void actionCancel() {
        close();
    }

    private void loadSettings() {
        userForm = new UserForm();
        userForm.setNickname( settings.getUserProperty("nickname") );
        userForm.setAltNickname( settings.getUserProperty("altnickname") );
        userForm.setUsername( settings.getUserProperty("username") );
        userForm.setRealname( settings.getUserProperty("realname") );
        userForm.setEmail( settings.getUserProperty("email") );

        eventsForm = new EventsForm();
        eventsForm.setLogChatChecked( settings.isEventEnabled("log-chat") );
        eventsForm.setRejoinChecked( settings.isEventEnabled("rejoin-after-kick") );
        eventsForm.setClickableLinksChecked( settings.isEventEnabled("clickable-links") );
        eventsForm.setAskForQuitChecked( settings.isEventEnabled("ask-for-quit") );

        viewForm = new ViewForm();
        viewForm.setDisplayTopicChecked( settings.isViewEnabled("display-topic") );
        viewForm.setTimestampEnabled( settings.isViewEnabled("timestamp-enabled") );
        viewForm.setTimestampFormat( settings.getViewTimestampFormat() );

        blockedForm = new BlockedForm();
        blockedForm.setBlockedNicknames( settings.getBlockedNicknames() );

        addTab(userForm, "Uživatel");
        addTab(eventsForm, "Události");
        addTab(viewForm, "Zobrazení");
        addTab(blockedForm, "Blokovaní uživatelé");

        // TODO vlastni prikazy
    }

    private void saveSettings() {
        settings.setUserProperty("nickname", userForm.getNickname() );
        settings.setUserProperty("altnickname", userForm.getAltNickname() );
        settings.setUserProperty("username", userForm.getUsername() );
        settings.setUserProperty("realname", userForm.getRealname() );
        settings.setUserProperty("email", userForm.getEmail() );

        settings.setEventEnabled("log-chat", eventsForm.isLogChatChecked() );
        settings.setEventEnabled("rejoin-after-kick", eventsForm.isRejoinChecked() );
        settings.setEventEnabled("clickable-links", eventsForm.isClickableLinksChecked() );
        settings.setEventEnabled("ask-for-quit", eventsForm.isAskForQuitChecked() );

        settings.setViewEnabled("display-topic", viewForm.isDisplayTopicChecked() );
        settings.setViewEnabled("timestamp-enabled", viewForm.isTimestampEnabled() );
        settings.setViewTimestampFormat( viewForm.getTimestampFormat() );

        settings.setBlockedNicknames( blockedForm.getBlockedNicknames() );

        // TODO vlastni prikazy

        settings.store();
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
