package Connection;

import MainWindow.*;
import Dialog.MessageDialog;
import Client.*;
import Favorites.ConnectionDetails;


public class InputHandler {
    
    public static AbstractTab getActiveTab() {
        return MainWindow.getInstance().getActiveTab();
    }

    public static ServerTab getCurrentServerTab() {
        return getActiveTab().getServerTab();
    }

    public static boolean isChannelTabActive() {
        return getActiveTab() instanceof ChannelTab;
    }

    private static void appendError(String str) {
        getActiveTab().appendError(str);
    }

    private static void clearInput() {
        MainWindow.getInstance().getInputField().setText(null);
    }

    public static void showNotConnectedError() {
        MessageDialog.error("Připojení nedostupné", "Nejste připojen/a k žádnému serveru.");
    }

    public static void showNotActiveChannelWarning() {
        MessageDialog.warning("Přepni si na kanál", "Aktivním oknem není kanál (channel).");
    }

    public static void showRecursionTooDeepError() {
        MessageDialog.error("Příkaz nelze zpracovat", "Rekurze při vykonávání vlastních příkazů");
    }

    public static void handle(String command, String params) {
        getCurrentServerTab().getConnection().sendRawLine(command + " " + params);
        clearInput();
    }

    public static void handleQuit(String reason) {
        handleQuit( getCurrentServerTab(), reason);
    }

    public static void handleQuit(ServerTab serverTab, String reason) {
        clearInput();
        serverTab.closeAllTabs();
    }

    public static void handlePrivMessage(String params) {
        int upto;
        if ((upto = params.indexOf(" ")) == -1) {
            appendError("Nesprávná syntaxe příkazu: /privmsg příjemce zpráva");
            return;
        }

        String target = params.substring(0, upto);
        String message  = params.substring(upto + 1);

        getCurrentServerTab().getConnection().sendMessage(target, message);
        clearInput();

        AbstractTab tab;
        ServerTab serverTab = getCurrentServerTab();
        if ( target.startsWith("#") ) {
            tab = serverTab.getChannelTabByName(target);
        } else {
            tab = serverTab.getPrivateChatByName(target);
            if (tab  == null )
                tab = serverTab.createPrivateChatTab(target);
        }

        if (tab != null) {
            String myNick = serverTab.getConnection().getNick();
            tab.appendText( HTML.bold(myNick) + ": " + HTML.escapeTags(message) );
        }
    }

    public static void handleJoin(String channel) {
        if ( !channel.startsWith("#") ) {
            appendError("Nesprávná syntaxe příkazu: /join #kanál");
            return;
        }

        getCurrentServerTab().getConnection().joinChannel(channel);
        clearInput();
    }

    public static void handlePart(String channel) {
        if ( channel.isEmpty() ) {
            channel = getActiveTab().getTabName();
            if ( !isChannelTabActive() ) {
                showNotActiveChannelWarning();
                return;
            }
        }

        ChannelTab channelTab = getCurrentServerTab().getChannelTabByName(channel);

        if (channelTab == null) {
            clearInput();
            return;
        }

        channelTab.getServerTab().getConnection().partChannel(channel);
        channelTab.getServerTab().removeChannelTab(channelTab);
        clearInput();
    }

    public static void handleNick(String nick) {
        if ( nick.isEmpty() ) {
            appendError("Nebyla zadána nová přezdívka.");
            return;
        }

        getCurrentServerTab().getConnection().changeNick(nick);
        clearInput();
    }

    public static void handleTopic(String topic) {
        if (getActiveTab() instanceof ChannelTab == false) {
            appendError("Téma lze změnit pouze ve vybraném kanále.");
            return;
        }

        if (topic == null)
            return;

        String channel = getActiveTab().getTabName();
        getCurrentServerTab().getConnection().setTopic(channel, topic);
        clearInput();
    }

    public static void handleMode(String params) {
        String errorMessage = "Nesprávná syntaxe příkazu: /mode #kanál mód";
        String match = "#\\w+ .+";
        if ( !params.matches(match) ) {
            appendError(errorMessage);
            return;
        }

        String[] parts = params.split(" ", 2);
        getCurrentServerTab().getConnection().setMode(parts[0], parts[1]);
        clearInput();
    }

    public static void handleServer(String address) {
        try {
            MainWindow.getInstance().createServerTab( ConnectionDetails.fromAddress(address) );
            clearInput();
        } catch (Exception e) {
            MessageDialog.error("Chyba aplikace", "Připojení nelze uskutečnit.");
        }
    }

    public static void handleKick(String params) {
        String syntax = "Nesprávná syntaxe příkazu: /kick #kanál uživatel [důvod]";
        String match = "#\\w+ \\S+( .+)?";
        if ( !params.matches(match) ) {
            appendError(syntax);
            return;
        }

        String[] parts = params.split(" ", 3);
        String channel = parts[0];
        String nickname = parts[1];

        if (parts.length == 2) {
            getCurrentServerTab().getConnection().kick(channel, nickname);
        } else {
            String reason = parts[2];
            getCurrentServerTab().getConnection().kick(channel, nickname, reason);
        }

        clearInput();
    }

    public static void handleAway(String reason) {
        if ( reason.isEmpty() )
            reason = "Prostě se flákam.";

        getCurrentServerTab().getConnection().setAway(reason);
        clearInput();
    }

    public static void handleNotAway() {
        getCurrentServerTab().getConnection().setNowAway();
        clearInput();
    }

    public static void handleClear() {
        if (getActiveTab() != null)
            getActiveTab().clearContent();

        clearInput();
    }

    public static void handleWhois(String nick) {
        if ( nick.isEmpty() ) {
            appendError("Nesprávná syntaxe příkazu: /whois uživatel");
        } else {
            getCurrentServerTab().getConnection().whois(nick);
            clearInput();
        }
    }

    public static void handleAction(String params) {
        if ( !isChannelTabActive() ) {
            showNotActiveChannelWarning();
            return;
        }

        if ( params.isEmpty() )
            params = "neumí používat IRC klienta."; // :)

        String channel = getActiveTab().getTabName();
        getCurrentServerTab().getConnection().sendAction(channel, params);

        String nick = getCurrentServerTab().getConnection().getNick();
        getActiveTab().appendText( HTML.italic(nick + " " + params) );
        clearInput();
    }

}
