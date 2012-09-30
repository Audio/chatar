package Client;

import Config.CommandHistory;
import Connection.*;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

/**
 * Základní panel, viditelný neustále. Načítá textový vstup od uživatele.
 * Vstup má podobu příkazů nebo obyčejného textu (zpráva na odeslání
 * při komunikaci s ostatními uživateli).
 */
public class GInput extends JPanel {

    private JButton button;
    private JTextField textField;

    private enum Commands {

        UNKNOWN, ACTION, AFK, AWAY, BACK, CLEAR, JOIN, KICK, LEAVE, ME, MODE,
        NAMES, NICK, OPER, PART, PRIVMSG, QUIT, SERVER, TOPIC, WHOIS
        ;

        public static Commands fromString(String Str) {
            try {
                return valueOf(Str);
            }
            catch (Exception e){ return Commands.UNKNOWN; }
        }
    };


    /**
     * Zkonstruuje spodni panel. Na nem se bude nachazet tlacitko
     * a textove pole pro vstup. Po stisku tlacitka se objevi popup menu
     * slouzici pro rychly vyber prikazu (napr. zmena prezdivky, odpojeni
     * od serveru atd.). Textove pole slouzi pro odesilani textovych
     * zprav do kanalu (je-li uzivatel pripojen) a odesilani prikazu
     * vybranemu serveru.
     */
    public GInput(int width, int height) {
        GUI.setPreferredSize(this, width, height);
        setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS) );

        JPanel inner_panel = new JPanel();
        inner_panel.setLayout( new BoxLayout(inner_panel, BoxLayout.LINE_AXIS) );
        inner_panel.setBorder( BorderFactory.createEmptyBorder(5, 10, 5, 10) );

        button = new JButton("Chatař");
        button.setToolTipText("Kliknutím nastavíte přezdívku (vyžaduje aktivní připojení na server)");
        button.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Input.currentTab == null)
                    return;

                GUI.showSetNicknameDialog();
            }
        });

        textField = new JTextField(510);
        textField.setMaximumSize( new Dimension(0, 28) );

        // Zpracování události po odentrování
        textField.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleInputText();
            }
        });

        // Zpracování stisku šipek (kvůli historii příkazů)
        textField.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed (KeyEvent e) {
                browseHistory(e);
            }
        });

        inner_panel.add(button);
        inner_panel.add( Box.createHorizontalGlue() );
        inner_panel.add( Box.createRigidArea( new Dimension(10,0) ) );
        inner_panel.add( Box.createHorizontalGlue() );
        inner_panel.add(textField);

        add(inner_panel);
    }

    public JButton getButton() {
        return button;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setNickname(String nick) {
        button.setText(nick);
    }

    private void handleInputText() {
        String inputText = textField.getText();
        if ( inputText.trim().isEmpty() )
            return;

        if ( inputText.startsWith("/") )
            handleCommand();
        else
            handleMessage();
    }

    /**
     * Práce s historií příkazů.
     */
    private void browseHistory(KeyEvent e) {
        int c = e.getKeyCode();
        String command = "";

        // Odchytí stisknutí šipky nahoru/dolu.
        if (c == KeyEvent.VK_UP)
            command = CommandHistory.getOlder();
        else if (c == KeyEvent.VK_DOWN)
            command = CommandHistory.getNewer();

        // Žádný vrácený příkaz, nebo se nejedná o šipku.
        if ( command.length() == 0)
            return;

        // Zabrání dalším akcím.
        e.consume();

        // Vypíše příkaz z historie.
        JTextField field = MainWindow.getInstance().getGInput().getTextField();
        field.setText(command);
        field.selectAll();
    }

    /**
     * Zjisti, o jaky prikaz se jedna.
     * Je-li prikaz neznamy / nepovoleny, oznami to uzivateli
     * textovou hlaskou do aktualniho panelu.
     *
     * Neexistuje-li zadny panel, vytvori dialogove okno s oznamenim.
     *
     * Standardni prikaz preda jeho obsluzne metode.
     *
     * Feature: příkazy přidávány do historie příkazů.
     */
    private void handleCommand() {
        String inputText = textField.getText();
        String command;
        String params = null;
        int upto;

        if ( (upto = inputText.indexOf(" ") ) > -1 ) {
            command = inputText.substring(1, upto);
            params  = inputText.substring(upto + 1);
        }
        else {
            command = inputText.substring(1);
        }

        command = command.toUpperCase();

        CommandHistory.add(inputText);

        switch ( Commands.fromString(command) ) {
            case UNKNOWN:    { Input.showError(command); break; }
            case QUIT:    { Input.handleQuit(params); break; }
            case PRIVMSG: { Input.handlePrivMessage(params); break; }
            case JOIN:    { Input.handleJoin(params); break; }
            case LEAVE:   { Input.handlePart(params); break; }
            case PART:    { Input.handlePart(params); break; }
            case NICK:    { Input.handleNick(params); break; }
            case TOPIC:   { Input.handleTopic(params); break; }
            case NAMES:   { Input.handleNames(params); break; }
            case MODE:    { Input.handleMode(params); break; }
            case KICK:    { Input.handleKick(params); break; }
            case AWAY:    { Input.handleAway(params); break; }
            case AFK:     { Input.handleAway(params); break; }
            case BACK:    { Input.handleBack(); break; }
            case CLEAR:   { Input.handleClear(); break; }
            case OPER:    { Input.handleOper(params); break; }
            case WHOIS:   { Input.handleWhois(params); break; }
            case ME:      { Input.handleMe(params); break; }
            case ACTION:  { Input.handleMe(params); break; }

            case SERVER:  { Input.handleServer(params); break; }
        }
    }

    private void handleMessage () {
        if (Input.getCurrentServer() == null) {
            Input.showNoConnectionError();
            return;
        }

        // TODO instanceof ?
        if ( Input.currentTab.getClass().getName().equals("Client.ServerTab") ) {
            String err = Output.HTML.mType("error");
            Input.currentTab.addText(err+ "Nelze odeslat zprávu. Toto není chatovací místnost!");
            return;
        }

        String msg = textField.getText();
        String room = Input.currentTab.getTabName();
        DeprecatedConnection con = Input.currentTab.getConnection();

        Input.handlePrivMessage(room + " " + msg);
        con.output(con.config.nickname + ": " + msg);
    }

}
