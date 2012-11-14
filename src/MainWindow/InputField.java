package MainWindow;

import Command.*;
import Connection.InputHandler;
import Settings.Settings;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.JTextField;


public class InputField extends JTextField {

    static final long serialVersionUID = 1L;

    private static final byte MAX_RECURSION_DEPTH = 10;
    private byte recursionDepth;

    private enum Commands {

        UNKNOWN, ACTION, AFK, AWAY, BACK, CLEAR, JOIN, KICK, LEAVE, ME, MODE,
        NICK, PART, PRIVMSG, QUIT, SERVER, TOPIC, WHOIS;

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
        setFocusTraversalKeysEnabled(false);

        addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInputText();
            }
        });

        addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch ( e.getKeyCode() ) {
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_UP:
                        browseHistory(e); break;
                    case KeyEvent.VK_TAB:
                        completeNickname(e); break;
                }
            }
        });
    }

    private void browseHistory(KeyEvent e) {
        int c = e.getKeyCode();
        String command = "";

        if (c == KeyEvent.VK_UP)
            command = CommandHistory.getPrevious();
        else if (c == KeyEvent.VK_DOWN)
            command = CommandHistory.getNext();

        if ( command.length() == 0)
            return;

        e.consume();

        setText(command);
        selectAll();
    }

    private void completeNickname(KeyEvent e) {
        e.consume();

        String text = getText();
        if ( text.trim().isEmpty() )
            return;

        AbstractTab activeTab = InputHandler.getActiveTab();
        if (activeTab instanceof ChannelTab == false)
            return;

        ChannelTab tab = (ChannelTab) activeTab;
        
        int position = getCaret().getDot();

        int nickStartPos = text.substring(0, position).lastIndexOf(" ");
        if (nickStartPos == -1)
            nickStartPos = 0;
        else
            nickStartPos++;

        int nickEndPos = text.indexOf(" ", position);
        if (nickEndPos == -1)
            nickEndPos = text.length();

        if (nickEndPos < text.length() && text.charAt(nickEndPos) == ':')
            nickEndPos--;

        String partialNick = text.substring(nickStartPos, nickEndPos);
        String completeNickname;
        if ( tab.contains(partialNick) )
            completeNickname = tab.getNickAfter(partialNick);
        else
            completeNickname = tab.getCompleteNickname(partialNick);

        text = text.substring(0, nickStartPos) + completeNickname + text.substring(nickEndPos);
        setText(text);
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

        if (command == Commands.UNKNOWN && handleCustomCommand(rawCommand, params))
            return;

        AbstractTab tab = MainWindow.getInstance().getActiveTab();
        boolean connectionRequired = (command != Commands.CLEAR && command != Commands.SERVER);
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
            }

        }
    }

    private boolean handleCustomCommand(String name, String params) {
        boolean handled = true;

        Command customCommand = Settings.getInstance().getCommand(name);
        if (customCommand != null) {

            if (++recursionDepth > MAX_RECURSION_DEPTH) {
                InputHandler.showRecursionTooDeepError();
                recursionDepth = 0;
            } else {
                String newInput = customCommand.content.replaceAll("%T", params);
                setText(newInput);
                handleInputText();
            }

        } else {
            recursionDepth = 0;
            handled = false;
        }

        return handled;
    }

    private void handleMessage() {
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
