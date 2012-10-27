package Connection;

import Client.*;


public class InputHandler {
    
    private static AbstractTab getActiveTab() {
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
        MainWindow.getInstance().getGInput().getTextField().setText(null);
    }

    public static void showNotConnectedError() {
        new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Připojení nedostupné",
                "Nejste připojen/a k žádnému serveru.");
    }

    public static void showNotActiveChannelError() {
        new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_WARN, "Přepni si na kanál",
                "Aktivním oknem není kanál (channel).");
    }

    public static void showUnknownCommandError(String unknownCommand) {
        if (getActiveTab() == null) {
            new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Neznámý příkaz",
                    "Neznámý příkaz " + unknownCommand.toUpperCase() + ".");
        } else {
            appendError(" Neznámý přijatý příkaz: " + unknownCommand);
        }
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
            appendError("Špatná syntaxe příkazu. Použijte /privmsg prijemce zprava");
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
            tab.appendText(myNick + ": " + message);
        }
    }

    public static void handleJoin(String channel) {
        if ( channel.isEmpty() ) {
            appendError("Špatná syntaxe příkazu. Použijte /join nazev_kanalu");
            return;
        }

        ChannelTab tab = getCurrentServerTab().getChannelTabByName(channel);
        if (tab == null)
            tab = getCurrentServerTab().createChannelTab(channel);

        tab.setFocus();
        clearInput();
    }

    public static void handlePart(String channel) {
        if ( channel.isEmpty() ) {
            channel = getActiveTab().getTabName();
            if ( !isChannelTabActive() ) {
                showNotActiveChannelError();
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
        getCurrentServerTab().getConnection().setTopic(channel, topic );
        clearInput();
    }

    public static void handleMode(String params) {
        String errorMessage = "Příkaz MODE: nesprávná syntaxe příkazu. Použijte /mode #KANAL MOD";
        String match = "#[a-zA-Z_0-9]+ .+";
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
            MainWindow.getInstance().createServerTab(address);
            clearInput();
        } catch (Exception e) {
            new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Chyba aplikace", "Připojení nelze uskutečnit.");
        }
    }

    /**
     * Vyhozeni uzivatele z kanalu.
     * Syntaxe: KICK #kanal uzivatel [:duvod]
     */
    public static void handleKick(String params) {
        String syntax = "Nesprávná syntaxe příkazu: KICK #kanal uzivatel [:duvod]";
        String match = "#[a-zA-Z_0-9]+ [a-zA-Z_0-9]+( :.+)?";
        if ( !params.matches(match) ) {
            appendError(syntax);
            return;
        }

        // getActiveTab().getQuery().kick(params);
        clearInput();
    }

    public static void handleAway(String reason) {
        Connection con = getCurrentServerTab().getConnection();
        if ( reason.isEmpty() )
            con.sendRawLine("AWAY");
        else
            con.sendRawLine("AWAY :" + reason);

        clearInput();
    }

    public static void handleNotAway() {
        handleAway(null);
    }

    public static void handleClear() {
        if (getActiveTab() != null)
            getActiveTab().clearContent();

        clearInput();
    }

    /**
     * Žádost uživatele o přidělení práv operátora (mode +o).
     */
    public static void handleOper(String params) {
        clearInput();

        if (params.isEmpty() || params.indexOf(" ") == -1) {
            appendError("Špatná syntaxe příkazu. Použijte /oper uzivatel heslo");
            return;
        }

        // getCurrentServer().getQuery().oper(params);
    }

    /**
     * Informace o uživateli.
     */
    public static void handleWhois(String nick) {
        clearInput();

        if ( nick.isEmpty() ) {
            appendError("Špatná syntaxe příkazu. Použijte /whois [server ]uzivatel");
            return;
        }

        // getCurrentServer().getQuery().whois(params);
    }

    public static void handleAction(String params) {
        if ( !isChannelTabActive() ) {
            showNotActiveChannelError();
            return;
        }

        if ( params.isEmpty() )
            params = "neumi pouzivat IRC klienta."; // :)

        String channel = getActiveTab().getTabName();
        getCurrentServerTab().getConnection().sendAction(channel, params);

        String nick = getCurrentServerTab().getConnection().getNick();
        getActiveTab().appendText( HTML.italic(nick + " " + params) );
        clearInput();
    }

}
