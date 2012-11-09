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
        setSize(WINDOW_WIDTH, 290);
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
                saveOptions();
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
        GUI.setExactSize(label, 45, 20);

        tabPanel.add("", c);
        int index = tabPanel.getTabCount() - 1;
        tabPanel.setTabComponentAt(index, label);
    }

    private void actionCancel() {
        close();
    }

    private void loadSettings() {
        userForm = new UserForm();
        eventsForm = new EventsForm();
        viewForm = new ViewForm();

        addTab(userForm, "Uživatel");
        addTab(eventsForm, "Události");
        addTab(viewForm, "Zobrazení");

        Settings c = getSettings();
        /*
        nickname.setText(c.nickname);
        username.setText(c.username);
        hostname.setText(c.hostname);
        servername.setText(c.servername);
        realname.setText(c.realname);
        password.setText(c.password);
        */
    }

    private void saveOptions() {
        /*
        Config c = new Config();
        c.nickname   = nickname.getText();
        c.username   = username.getText();
        c.hostname   = hostname.getText();
        c.servername = servername.getText();
        c.realname   = realname.getText();
        c.password   = password.getPassword().toString();
        c.saveToFile();
        */

        /*
        if ( MainWindow.getInstance().getTabContainer().getTabCount() == 0) {
            MainWindow.getInstance().getGInput().setNickname(c.nickname);
        }
        */
    }

    public Settings getSettings() {
        AbstractTab tab = MainWindow.getInstance().getActiveTab();
        if (tab == null) {
            Settings c = new Settings();
            // c.loadFromFile();
            return c;
        }

        return null; // tab.getConnection().config;
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
