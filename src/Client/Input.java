package Client;

import Config.CommandHistory;
import Connection.*;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;


public class Input extends JPanel {

    private JButton button;
    private JTextField textField;

    private enum Commands {

        UNKNOWN, ACTION, AFK, AWAY, BACK, CLEAR, JOIN, KICK, LEAVE, ME, MODE,
        NICK, PART, PRIVMSG, QUIT, SERVER, TOPIC, WHOIS
        ;

        public static Commands fromString(String Str) {
            try {
                return valueOf(Str);
            } catch (Exception e){
                return Commands.UNKNOWN;
            }
        }
    };


    public Input(int width, int height) {
        GUI.setPreferredSize(this, width, height);
        setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS) );

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout( new BoxLayout(innerPanel, BoxLayout.LINE_AXIS) );
        innerPanel.setBorder( BorderFactory.createEmptyBorder(5, 10, 5, 10) );

        button = new JButton("Chatař");
        button.setToolTipText("Kliknutím nastavíte přezdívku (vyžaduje aktivní připojení na server)");
        button.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractTab tab = MainWindow.getInstance().getActiveTab();
                if (tab == null)
                    return;

                String nickname = GUI.showSetNicknameDialog();
                if (nickname != null)
                    InputHandler.handleNick(nickname);
            }
        });

        textField = new JTextField(510);
        textField.setMaximumSize( new Dimension(0, 28) );

        // Zpracování události po odentrování
        textField.addActionListener( new ActionListener() {
            @Override
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

        innerPanel.add(button);
        innerPanel.add( Box.createHorizontalGlue() );
        innerPanel.add( Box.createRigidArea( new Dimension(10,0) ) );
        innerPanel.add( Box.createHorizontalGlue() );
        innerPanel.add(textField);

        add(innerPanel);
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

    private void handleCommand() {
        String inputText = textField.getText();
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
            tab.appendError("Nelze odeslat zprávu. Toto není chatovací místnost!");
            return;
        }

        String message = textField.getText();
        String target = tab.getTabName();
        InputHandler.handlePrivMessage(target + " " + message);
    }

}
