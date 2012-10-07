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

    public static void outputToCurrentTab(String str) {
        getActiveTab().addText(str);
    }

    public static void clearInput() {
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
            String err = Output.HTML.color( mType("error"), Output.HTML.RED);
            outputToCurrentTab(err + " Neznámý přijatý příkaz: " + unknownCommand);
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
            outputToCurrentTab( mType("error") + "Špatná syntaxe příkazu. Použijte /privmsg prijemce zprava");
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
            tab.addText(myNick + ": " + message);
        }
    }

    public static void handleJoin(String channel) {
        if (channel == null || channel.trim().length() == 0) {
            outputToCurrentTab( mType("error") + "Špatná syntaxe příkazu. Použijte /join nazev_kanalu");
            return;
        }

        ChannelTab tab = getCurrentServerTab().getChannelTabByName(channel);
        if (tab == null)
            tab = getCurrentServerTab().createChannelTab(channel);

        GUI.getTabContainer().setSelectedComponent(tab);
        clearInput();
    }

    public static void handlePart(String channel) {
        if (channel == null || channel.trim().length() == 0) {
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

        channelTab.getConnection().partChannel(channel);
        channelTab.getServerTab().removeChannelTab(channelTab);
        clearInput();
    }

    public static void handleNick(String nick) {
        if (nick == null || nick.trim().length() == 0) {
            outputToCurrentTab( mType("error") + "Nebyla zadána nová přezdívka.");
            return;
        }

        getCurrentServerTab().getConnection().changeNick(nick);
        clearInput();
    }

    public static void handleTopic(String topic) {
        if (getActiveTab() instanceof ChannelTab == false) {
            outputToCurrentTab( mType("error") + "Téma lze změnit pouze ve vybraném kanále.");
            return;
        }

        if (topic == null)
            return;

        String channel = getActiveTab().getTabName();
        getActiveTab().getConnection().setTopic(channel, topic );
        clearInput();
    }

    /**
     * Zpracovani prikazu MODE.
     */
    public static void handleMode(String params) {
        if (params != null && params.trim().length() > 0) {
            params = params.trim();
        } else {
            outputToCurrentTab( mType("error") + "Příkaz MODE: nesprávná syntaxe příkazu. Použijte /mode {UZIVATEL|KANAL}");
            return;
        }

        // getActiveTab().getQuery().mode(params);
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
     *
     * @param params
     */
    public static void handleKick(String params) {
        if (params == null)
            return;

        String syntax = mType("error") + "Nesprávná syntaxe příkazu: KICK #kanal uzivatel [:duvod]";
        String match = "#[a-zA-Z_0-9]+ [a-zA-Z_0-9]+( :.+)?";
        if ( !params.matches(match) ) {
            outputToCurrentTab(syntax);
            return;
        }

        // getActiveTab().getQuery().kick(params);
        clearInput();
    }

    public static void handleAway(String params) {
        Connection con = getCurrentServerTab().getConnection();
        if (params == null || params.trim().length() == 0)
            con.sendRawLine("AWAY");
        else
            con.sendRawLine("AWAY :" + params);

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
        params = (params == null) ? null : params.trim();
        clearInput();

        if (params == null || params.length() == 0 || params.indexOf(" ") == -1) {
            outputToCurrentTab( mType("error") + "Špatná syntaxe příkazu. Použijte /oper uzivatel heslo");
            return;
        }

        // getCurrentServer().getQuery().oper(params);
    }

    /**
     * Informace o uživateli.
     */
    public static void handleWhois(String params) {
        params = (params == null) ? null : params.trim();
        clearInput();

        if (params == null || params.length() == 0) {
            outputToCurrentTab( mType("error") + "Špatná syntaxe příkazu. Použijte /whois [server ]uzivatel");
            return;
        }

        // getCurrentServer().getQuery().whois(params);
    }

    public static void handleMe(String params) {
        if ( !isChannelTabActive() ) {
            showNotActiveChannelError();
            return;
        }

        if (params == null || params.trim().length() == 0)
            params = "neumi pouzivat IRC klienta."; // :)

        String channel = getActiveTab().getTabName();
        getCurrentServerTab().getConnection().sendAction(channel, params);
        clearInput();
    }

    /**
     * Tunýlek pro stejnojmennou metodu v Output.HTML - kvůli přehlednosti.
     */
    public static String mType(String str) {
        return Output.HTML.mType(str);
    }

}
