package Settings;

import Dialog.TabbedWindow;
import Client.GUI;
import Dialog.MessageDialog;
import MainWindow.*;
import java.awt.*;
import java.awt.event.*;
import javax.management.InvalidAttributeValueException;
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
        this.settings = Settings.getInstance();

        setAlwaysOnTop(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setIconImage( new ImageIcon("img/settings-icon.png").getImage() );
        setResizable(false);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Osobní nastavení");

        createMainPanel();
        createTabs();
        reloadSettings();

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
                try {
                    saveSettings();
                    close();
                } catch (InvalidAttributeValueException ex) {
                    MessageDialog.error("Nesprávně vyplněný formulář", ex.getMessage() );
                }
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

    private void createTabs() {
        userForm = new UserForm();
        eventsForm = new EventsForm();
        viewForm = new ViewForm();
        blockedForm = new BlockedForm();

        addTab(userForm, "Uživatel");
        addTab(eventsForm, "Události");
        addTab(viewForm, "Zobrazení");
        addTab(blockedForm, "Blokovaní uživatelé");
    }

    private void reloadSettings() {
        userForm.setNickname( settings.getUserProperty("nickname") );
        userForm.setAltNickname( settings.getUserProperty("altnickname") );
        userForm.setUsername( settings.getUserProperty("username") );
        userForm.setRealname( settings.getUserProperty("realname") );
        userForm.setEmail( settings.getUserProperty("email") );

        eventsForm.setLogChatChecked( settings.isEventEnabled("log-chat") );
        eventsForm.setRejoinChecked( settings.isEventEnabled("rejoin-after-kick") );
        eventsForm.setClickableLinksChecked( settings.isEventEnabled("clickable-links") );
        eventsForm.setAskForQuitChecked( settings.isEventEnabled("ask-for-quit") );

        viewForm.setDisplayTopicChecked( settings.isViewEnabled("display-topic") );
        viewForm.setTimestampEnabled( settings.isViewEnabled("timestamp-enabled") );
        viewForm.setTimestampFormat( settings.getViewTimestampFormat() );

        blockedForm.setBlockedNicknames( settings.getBlockedNicknames() );
    }

    private void saveSettings() throws InvalidAttributeValueException {
        if ( userForm.getNickname().isEmpty() )
            throw new InvalidAttributeValueException("Přezdívka musí být vyplněna");

        else if ( userForm.getUsername().isEmpty() )
            throw new InvalidAttributeValueException("Uživatelské jméno musí být vyplněno");

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

        settings.store();
    }

    private void actionCancel() {
        reloadSettings();
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

}
