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
public class Input extends JPanel {

    private JButton button;
    private JTextField textField;

    private enum Commands {

        UNKNOWN, ACTION, AFK, AWAY, BACK, CLEAR, JOIN, KICK, LEAVE, ME, MODE,
        NICK, OPER, PART, PRIVMSG, QUIT, SERVER, TOPIC, WHOIS
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
    public Input(int width, int height) {
        GUI.setPreferredSize(this, width, height);
        setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS) );

        JPanel inner_panel = new JPanel();
        inner_panel.setLayout( new BoxLayout(inner_panel, BoxLayout.LINE_AXIS) );
        inner_panel.setBorder( BorderFactory.createEmptyBorder(5, 10, 5, 10) );

        button = new JButton("Chatař");
        button.setToolTipText("Kliknutím nastavíte přezdívku (vyžaduje aktivní připojení na server)");
        button.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( MainWindow.getInstance().getActiveTab() == null )
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
        String rawCommand;
        String params = "";
        int upto;

        if ( (upto = inputText.indexOf(" ") ) > -1 ) {
            rawCommand = inputText.substring(1, upto);
            params = inputText.substring(upto + 1).trim();
        }
        else {
            rawCommand = inputText.substring(1);
        }

        rawCommand = rawCommand.toUpperCase();

        CommandHistory.add(inputText);

        Commands command = Commands.fromString(rawCommand);

        AbstractTab tab = MainWindow.getInstance().getActiveTab();
        boolean connectionRequired = (command != Commands.CLEAR && command != Commands.SERVER && command != Commands.C);
        boolean isConnected = (tab == null) ? false : tab.getServerTab().getConnection().isConnected();
        if (connectionRequired && !isConnected) {
            InputHandler.showNotConnectedError();
        } else {

            switch (command) {
                case UNKNOWN: { InputHandler.showUnknownCommandError(rawCommand); break; }
                case QUIT:    { InputHandler.handleQuit(params); break; }
                case PRIVMSG: { InputHandler.handlePrivMessage(params); break; }
                case JOIN:    { InputHandler.handleJoin(params); break; }
                case LEAVE:   { InputHandler.handlePart(params); break; }
                case PART:    { InputHandler.handlePart(params); break; }
                case NICK:    { InputHandler.handleNick(params); break; }
                case TOPIC:   { InputHandler.handleTopic(params); break; }
                case MODE:    { InputHandler.handleMode(params); break; }
                case KICK:    { InputHandler.handleKick(params); break; }
                case AWAY:    { InputHandler.handleAway(params); break; }
                case AFK:     { InputHandler.handleAway(params); break; }
                case BACK:    { InputHandler.handleNotAway(); break; }
                case CLEAR:   { InputHandler.handleClear(); break; }
                case OPER:    { InputHandler.handleOper(params); break; }
                case WHOIS:   { InputHandler.handleWhois(params); break; }
                case ME:      { InputHandler.handleAction(params); break; }
                case ACTION:  { InputHandler.handleAction(params); break; }

                case SERVER:  { InputHandler.handleServer(params); break; }
            }

        }
    }

    private void handleMessage () {
        if (InputHandler.getCurrentServerTab() == null) {
            InputHandler.showNotConnectedError();
            return;
        }

        AbstractTab tab = MainWindow.getInstance().getActiveTab();
        if (tab instanceof ServerTab) {
            String err = Output.HTML.mType("error");
            tab.addText(err+ "Nelze odeslat zprávu. Toto není chatovací místnost!");
            return;
        }

        String message = textField.getText();
        String target = tab.getTabName();
        InputHandler.handlePrivMessage(target + " " + message);
    }

}
