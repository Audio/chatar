package Client;

import Config.CommandHistory;
import Connection.InputHandler;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.JTextField;


public class InputField extends JTextField {

    private enum Commands {

        UNKNOWN, ACTION, AFK, AWAY, BACK, CLEAR, JOIN, KICK, LEAVE, ME, MODE,
        NICK, PART, PRIVMSG, QUIT, SERVER, TOPIC, WHOIS,
        C, J
        ;

        public static Commands fromString(String Str) {
            try {
                return valueOf(Str);
            } catch (Exception e){
                return Commands.UNKNOWN;
            }
        }
    };


    public InputField() {
        super(510);
        setMaximumSize( new Dimension(0, 28) );

        addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInputText();
            }
        });

        addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed (KeyEvent e) {
                browseHistory(e);
            }
        });
    }

    private void handleInputText() {
        String inputText = getText();
        if ( inputText.trim().isEmpty() )
            return;

        if ( inputText.startsWith("/") )
            handleCommand();
        else
            handleMessage();
    }

    private void browseHistory(KeyEvent e) {
        int c = e.getKeyCode();
        String command = "";

        // Odchytí stisknutí šipky nahoru/dolu.
        if (c == KeyEvent.VK_UP)
            command = CommandHistory.getPrevious();
        else if (c == KeyEvent.VK_DOWN)
            command = CommandHistory.getNext();

        // Žádný vrácený příkaz, nebo se nejedná o šipku.
        if ( command.length() == 0)
            return;

        // Zabrání dalším akcím.
        e.consume();

        // Vypíše příkaz z historie.
        setText(command);
        selectAll();
    }

    private void handleCommand() {
        String inputText = getText();
        String rawCommand;
        String params = "";
        int upto;

        if ( (upto = inputText.indexOf(" ") ) > -1 ) {
            rawCommand = inputText.substring(1, upto);
            params = inputText.substring(upto + 1).trim();
        } else {
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
                case UNKNOWN: { InputHandler.handle(rawCommand, params); break; }
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
                case WHOIS:   { InputHandler.handleWhois(params); break; }
                case ME:      { InputHandler.handleAction(params); break; }
                case ACTION:  { InputHandler.handleAction(params); break; }

                case SERVER:  { InputHandler.handleServer(params); break; }

                // TODO pryc
                case C:       { InputHandler.handleServer("irc.mmoirc.com"); break; }
                case J:       { InputHandler.handleJoin("#abraka"); break; }
            }

        }
    }

    private void handleMessage () {
        AbstractTab tab = InputHandler.getActiveTab();
        if (tab == null) {
            InputHandler.showNotConnectedError();
            return;
        } else if (tab instanceof ServerTab) {
            tab.appendError("Nelze odeslat zprávu. Toto není chatovací místnost!");
            return;
        }

        String message = getText();
        String target = tab.getTabName();
        InputHandler.handlePrivMessage(target + " " + message);
    }

}
