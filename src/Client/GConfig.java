package Client;

import Config.Config;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;


public class GConfig extends JFrame implements WindowListener {

    private static GConfig instance;

    private JTextField nickname;
    private JTextField username;
    private JTextField hostname;
    private JTextField servername;
    private JTextField realname;
    private JPasswordField password;


    public static GConfig getInstance() {
        if (instance == null)
            instance = new GConfig();

        return instance;
    }

    private GConfig() {

        // Nastaveni okna
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle("Osobní nastavení");
        setResizable(false);
        setSize(385, 300);

        getContentPane().setLayout( new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS) );

        // Panel stranky - obsahuje 3 panely: panel labelu (popisu, main_panel),
        // panel text. vstupu (input_panel) a panel s tlacitky (button_panel).
        JPanel page = new JPanel();
        page.setLayout( new BoxLayout(page, BoxLayout.LINE_AXIS) );


        // Panel s popisy
        JPanel main_panel = new JPanel();
        main_panel.setLayout( new BoxLayout(main_panel, BoxLayout.LINE_AXIS) );
        main_panel.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 0) );

        JLabel nickname_label   = new JLabel("Přezdívka");
        JLabel username_label   = new JLabel("Uživatelské jméno");
        JLabel hostname_label   = new JLabel("Hostname");
        JLabel servername_label = new JLabel("Název serveru");
        JLabel realname_label   = new JLabel("Skutečné jméno");
        JLabel password_label   = new JLabel("Heslo");

        setTextFieldSize(nickname_label);
        setTextFieldSize(username_label);
        setTextFieldSize(hostname_label);
        setTextFieldSize(servername_label);
        setTextFieldSize(realname_label);
        setTextFieldSize(password_label);

        Box box = Box.createVerticalBox();
        box.add(nickname_label);
        box.add(username_label);
        box.add(hostname_label);
        box.add(servername_label);
        box.add(realname_label);
        box.add(password_label);
        main_panel.add(box);

        // Panel s text. vstupy
        JPanel input_panel = new JPanel();
        input_panel.setLayout( new BoxLayout(input_panel, BoxLayout.LINE_AXIS) );
        input_panel.setBorder( BorderFactory.createEmptyBorder(10, 0, 10, 10) );

        nickname = new JTextField();
        username = new JTextField();
        hostname = new JTextField();
        servername = new JTextField();
        realname = new JTextField();
        password = new JPasswordField();

        setTextFieldSize(nickname);
        setTextFieldSize(username);
        setTextFieldSize(hostname);
        setTextFieldSize(servername);
        setTextFieldSize(realname);
        setTextFieldSize(password);

        Box boxi = Box.createVerticalBox();
        boxi.add(nickname);
        boxi.add(username);
        boxi.add(hostname);
        boxi.add(servername);
        boxi.add(realname);
        boxi.add(password);
        input_panel.add(boxi);


        // Panel s tlacitky
        JPanel button_panel = new JPanel();
        button_panel.setLayout( new BoxLayout(button_panel, BoxLayout.LINE_AXIS) );
        button_panel.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 0) );

        JButton save = new JButton("Uložit");
        save.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOptions();
                setVisible(false);
            }
        });

        JButton cancel = new JButton("Storno");
        cancel.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });

        setButtonSize(save);
        setButtonSize(cancel);

        Box boxb = Box.createHorizontalBox();
        boxb.add(save);
        boxb.add( Box.createRigidArea( new Dimension(20, 0) ) );
        boxb.add(cancel);

        button_panel.add(boxb);


        // Konecne usporadani panelu do "stranky".
        page.add(main_panel);
        page.add( Box.createHorizontalGlue() );
        page.add(input_panel);

        getContentPane().add(page);
        page.add( Box.createVerticalGlue() );
        getContentPane().add(button_panel);

        // Nahrani hodnot.
        loadOptions();

        // Zobrazeni okna.
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(this);
        //setAlwaysOnTop(true);

    }

    /**
     * Nastavi defaultni velikost pro vsechna tlacitka.
     */
    public static void setButtonSize(Component button) {
        GUI.setExactSize(button, 100, 30);
    }

    /**
     * Nastavi defaultni velikost pro vsechna vstupni pole a labely.
     */
    public static void setTextFieldSize(Component textField) {
        GUI.setExactSize(textField, 175, 30);
    }

    private void actionCancel () {
        setVisible(false);
    }

    /**
     * Ziska uzivatelske informace z aktulniho nastaveni vybraneho serveru.
     */
    private void loadOptions () {
        Config c = getConfig();
        nickname.setText(c.nickname);
        username.setText(c.username);
        hostname.setText(c.hostname);
        servername.setText(c.servername);
        realname.setText(c.realname);
        password.setText(c.password);
    }

    /**
     * Pri zavirani okna (skryti) uklada uzivatelske nastaveni do souboru.
     * Není-li uživatel připojen k žádnému serveru, pak rovnou změní
     * přezdívku na tlačítku na novou (GInput/button).
     */
    private void saveOptions () {

        Config c = new Config();
        c.nickname   = nickname.getText();
        c.username   = username.getText();
        c.hostname   = hostname.getText();
        c.servername = servername.getText();
        c.realname   = realname.getText();
        c.password   = password.getPassword().toString();
        c.saveToFile();

        if ( MainWindow.getInstance().getTabContainer().getTabCount() == 0) {
            MainWindow.getInstance().getGInput().setNickname(c.nickname);
        }

    }

    public static Config getConfig () {
        AbstractTab tab = MainWindow.getInstance().getActiveTab();
        if (tab == null) {
            Config c = new Config();
            c.loadFromFile();
            return c;
        }

        return null; // tab.getConnection().config;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        setVisible(false);
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
