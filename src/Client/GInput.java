package Client;

import Config.CommandHistory;
import Connection.Connection;
import Connection.Input;
import Connection.Output;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Základní panel, viditelný neustále. Načítá textový vstup od uživatele.
 * Vstup má podobu příkazů nebo obyčejného textu (zpráva na odeslání
 * při komunikaci s ostatními uživateli).
 *
 * @author Martin Fouček
 */
public class GInput extends JPanel {

    private JButton button;
    private JTextField textField;

    // vyctovy typ obsahujici povolene prikazy ke zpracovani
    private enum Allowed {

        fake, ACTION, AFK, AWAY, BACK, CLEAR, JOIN, KICK, LEAVE, ME, MODE,
        NAMES, NICK, OPER, PART, PRIVMSG, QUIT, SERVER, SHUTDOWN, TOPIC, WHOIS
        ;

        public static Allowed fromString(String Str) {
            try {
                return valueOf(Str);
            }
            catch (Exception e){ return Allowed.fake; }
        }
    };

    /**
     * Zkonstruuje spodni panel. Na nem se bude nachazet tlacitko
     * a textove pole pro vstup. Po stisku tlacitka se objevi popup menu
     * slouzici pro rychly vyber prikazu (napr. zmena prezdivky, odpojeni
     * od serveru atd.). Textove pole slouzi pro odesilani textovych
     * zprav do kanalu (je-li uzivatel pripojen) a odesilani prikazu
     * vybranemu serveru.
     * 
     * @param width
     * @param height
     */
    public GInput (int width, int height) {

        GUI.setMySize(this, width, height);
        setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS) );

        JPanel inner_panel = new JPanel();
        inner_panel.setLayout( new BoxLayout(inner_panel, BoxLayout.LINE_AXIS) );
        inner_panel.setBorder( BorderFactory.createEmptyBorder(5, 10, 5, 10) );

        button = new JButton("Chatař");
        button.setToolTipText("Kliknutím nastavíte přezdívku (vyžaduje aktivní připojení na server)");
        button.addActionListener( new ActionListener() {

            /**
             * Změna přezdívky při kliku na tlačítko.
             * Použitelné jen v případě, že je uživatel připojen k serveru.
             */
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
                actionInput();
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

    /**
     * Vraci referenci na tlacitko.
     *
     * @return
     */
    public JButton getButton() {
        return button;
    }

    /**
     * Vraci referenci na textove pole.
     *
     * @return
     */
    public JTextField getTextField() {
        return textField;
    }

    /**
     * Na tlacitko vepise nove zvolenou prezdivku.
     * 
     * @param nick
     */
    public void setNickname (String nick) {
        button.setText(nick);
    }

    /**
     * Analyzuje a zpracuje vstupni text.
     */
    private void actionInput () {

        String input_text = textField.getText();
        if (input_text.trim().isEmpty())
            return;

        if ( input_text.startsWith("/") )
            handleCommand();
        else
            handleMessage();

    }

    /**
     * Práce s historií příkazů.
     *
     * @param e
     */
    private void browseHistory (KeyEvent e) {

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
        GUI.getInput().getTextField().setText(command);
        GUI.getInput().getTextField().selectAll();

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
    private void handleCommand () {

        String input_text = textField.getText();
        String command = null;
        String params = null;
        int upto;

        if ( (upto = input_text.indexOf(" ") ) > -1 ) {
            command = input_text.substring(1, upto);
            params  = input_text.substring(upto + 1);
        }
        else {
            command = input_text.substring(1);
        }

        command = command.toUpperCase();


        // historie příkazů
        CommandHistory.add(input_text);


        switch ( Allowed.fromString(command) ) {
            case fake:    { Input.showError(command); break; }
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

            case SHUTDOWN:{ Input.handleShutdown(); break; }
            case SERVER:  { Input.handleServer(params); break; }

            // DEBUG - makra
            // case A:       { Input.handleServer("irc.mmoirc.com"); break; }
            // case B:       { Input.handleJoin("hokus"); break; }
        }

    }

    /**
     * Odesle zpravu do vybrane mistnosti / soukromeho okna.
     * Pokud neni aktivni zadne z predchozich oken,
     * oznami uzivateli, ze ma smulu; zpravu v textovem
     * policku ponecha.
     *
     * Po korektnim odeslani zpravy ji zobrazi take v panelu.
     */
    private void handleMessage () {

        if (Input.getCurrentServer() == null) {
            Input.connectionError();
            return;
        }


        if ( Input.currentTab.getClass().getName().equals("Client.GTabServer") ) {
            String err = Output.HTML.mType("error");
            Input.currentTab.addText(err+ "Nelze odeslat zprávu. Toto není chatovací místnost!");
            return;
        }

        String msg = textField.getText();
        String room = Input.currentTab.getTabName();
        Connection con = Input.currentTab.getConnection();

        Input.handlePrivMessage(room + " " + msg);
        con.output(con.config.nickname + ": " + msg);

    }

}
