package Connection;

import Client.*;
import Client.TabContainer.PanelTypes;
import java.util.Iterator;


public class InputHandler {
    
    private static AbstractTab getActiveTab() {
        return MainWindow.getInstance().getActiveTab();
    }

    public static ServerTab getCurrentServer() {
        return getActiveTab().getServerTab();
    }

    public static boolean isChannelTabActive() {
        return getActiveTab() instanceof ChannelTab;
    }

    public static void outputToCurrentTab(String str) {
        getActiveTab().addText(str);
    }

    // TODO potrebuju to?
    public static void clearText() {
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

    /**
     * Uzavre vsechny kanaly pridruzene k serveru.
     * Uzavre panel serveru a ukonci spojeni prikazem QUIT
     * a uzavrenim socketoveho spojeni.
     */
    public static void handleQuit (String reason) {
        handleQuit(getActiveTab(), reason);
    }

    /**
     * Uzavira spojeni - nikoli aktualniho panelu, ale vybraneho.
     */
    public static void handleQuit (AbstractTab tab, String reason) {
        ServerTab server = tab.getServerTab();

        // pozavira vsechny kanaly
        Iterator it = server.channels.iterator();
        while ( it.hasNext() ) {
            ChannelTab channel = (ChannelTab) it.next();
            channel.destroy();
        }

        // pozavira soukroma okna
        it = server.privateChats.iterator();
        while ( it.hasNext() ) {
            PrivateChatTab chat = (PrivateChatTab) it.next();
            chat.destroy();
        }

        clearText();

        server.die(reason);
        server.destroy();
    }

    /**
     * Odesle soukromou zpravu vybranemu uzivateli,
     * nebo odesle zpravu do vybraneho kanalu.
     */
    public static void handlePrivMessage (String params) {
        String user;
        String msg;
        int upto;

        if ((upto = params.indexOf(" ")) == -1) {
            outputToCurrentTab( mType("error") + "Špatná syntaxe příkazu. Použijte /privmsg prijemce zprava");
            return;
        }

        user = params.substring(0, upto);
        msg  = ":" + params.substring(upto + 1);

        // getActiveTab().getQuery().privMsg(user, msg);
        clearText();

        // Otevření nového okna při soukromé zprávě (tzn. uživateli)
        if ( user.startsWith("#") == false && getCurrentServer().getPrivateChatByName(user) == null) {
            try {
                MainWindow.getInstance().addTab(PanelTypes.PANEL_PRIVATE, user);
            } catch (ClientException e) { }
        }
    }

    /**
     * Pripoji se na vybrany kanal.
     * Otevre novy panel pro komunikaci na zminenem kanale.
     */
    public static void handleJoin(String channel) {
        if (channel == null || channel.trim().length() == 0) {
            outputToCurrentTab( mType("error") + "Špatná syntaxe příkazu. Použijte /join nazev_kanalu");
            return;
        }

        if ( channel.startsWith("#") )
            channel = channel.substring(1);

        // neotevre panel, co jiz existuje; pouze prepne
        ChannelTab exists = getCurrentServer().getChannelTabByName("#" + channel);
        if (exists != null) {
            GUI.getTabContainer().setSelectedComponent( exists );
            clearText();
            return;
        }

        try {
            MainWindow.getInstance().addTab(PanelTypes.PANEL_CHANNEL, channel);
            // getActiveTab().getQuery().join(channel);
            clearText();
        } catch (ClientException e) { }
    }

    /**
     * Zavre panel se zvolenym nazvem a odejde z kanalu.
     * Odstrani se take ze seznamu kanalu v objektu ServerTab.
     */
    public static void handlePart(String channel) {
        if (channel == null || channel.trim().length() == 0) {
            channel = getActiveTab().getTabName();
            if ( !isChannelTabActive() ) {
                showNotActiveChannelError();
                return;
            }
        }

        if ( channel.startsWith("#") )
            channel = channel.substring(1);

        ChannelTab channelTab = getCurrentServer().getChannelTabByName("#" + channel);

        // overi existenci panelu
        if (channelTab == null) {
            clearText();
            return;
        }

        // channel_tab.getQuery().leave(channel);
        channelTab.destroy();
        clearText();
    }

    /**
     * Mění přezdívku uživatele.
     */
    public static void handleNick(String nick) {
        // kontrola syntaxe
        if (nick == null || nick.trim().length() == 0) {
            outputToCurrentTab( mType("error") + "Nebyla zadána nová přezdívka.");
            return;
        }

        // getCurrentServer().getQuery().nick(nick);
        clearText();
    }

    /**
     * Meni tema ve vybranem kanale.
     */
    public static void handleTopic(String topic) {
        if (getActiveTab() instanceof ChannelTab == false) {
            outputToCurrentTab( mType("error") + "Téma lze změnit pouze ve vybraném kanále.");
            return;
        }

        if (topic != null)
            topic = topic.trim();

        String channel = getActiveTab().getTabName().substring(1);
        // getActiveTab().getQuery().topic(channel, topic );
        clearText();
    }
    
    /**
     * Zpracovani prikazu NAMES.
     */
    public static void handleNames(String channels) {
        // getActiveTab().getQuery().names(channels);
        clearText();
    }

    /**
     * Zpracovani prikazu MODE.
     */
    public static void handleMode(String params) {
        if (params != null && params.trim().length() > 0) {
            params = params.trim();
        }
        else {
            outputToCurrentTab( mType("error") + "Příkaz MODE: nesprávná syntaxe příkazu. Použijte /mode {UZIVATEL|KANAL}");
            return;
        }

        // getActiveTab().getQuery().mode(params);
        clearText();
    }

    /**
     * Pripojeni na zvoleny server.
     */
    public static void handleServer(String address) {
        try {
            MainWindow.getInstance().addTab(PanelTypes.PANEL_SERVER, address);
            clearText();
        }
        catch (Exception e) {
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
        clearText();
    }

    /**
     * Nastavení ci zrušení nepřítomnosti (AFK - away from keyboard).
     */
    public static void handleAway(String params) {
        /*
        if (params == null || params.trim().length() == 0)
            getActiveTab().getQuery().away(null);
        else
            getActiveTab().getQuery().away( params.trim() );
            */

        clearText();
    }

    /**
     * Zrušení nepřítomnosti (AFK).
     */
    public static void handleNotAway() {
        handleAway(null);
    }

    /**
     * Vymaže obsah aktualního panelu (záložky).
     */
    public static void handleClear() {
        if ( GUI.getTabContainer().getTabCount() > 0)
            getActiveTab().clearContent();

        clearText();
    }

    /**
     * Žádost uživatele o přidělení práv operátora (mode +o).
     */
    public static void handleOper(String params) {
        params = (params == null) ? null : params.trim();
        clearText();

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
        clearText();

        if (params == null || params.length() == 0) {
            outputToCurrentTab( mType("error") + "Špatná syntaxe příkazu. Použijte /whois [server ]uzivatel");
            return;
        }

        // getCurrentServer().getQuery().whois(params);
    }

    /**
     * Vypíše do kanálu, že uživatel provádí akci speficikovanou parametrem.
     */
    public static void handleMe(String params) {
        if ( !isChannelTabActive() ) {
            showNotActiveChannelError();
            return;
        }

        String channel = getActiveTab().getTabName();

        params = (params == null) ? null : params.trim();
        clearText();

        if (params == null || params.length() == 0) {
            params = "neumi pouzivat IRC klienta."; // :)
        }

        // getCurrentServer().getQuery().me(channel, params);
    }

    /**
     * Oznámení, že vložený příkaz nelze vykonat, neboť nemá obsluhu.
     */
    public static void showError(String invalidCommand) {
        if (getActiveTab() == null) {
            new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Neznámý příkaz",
                    "Neznámý příkaz " + invalidCommand.toUpperCase() + ".");
        }
        else {
            String err = Output.HTML.color( mType("error"), Output.HTML.RED);
            outputToCurrentTab(err + " Neznámý přijatý příkaz: " + invalidCommand);
        }
    }

    /**
     * Tunýlek pro stejnojmennou metodu v Output.HTML - kvůli přehlednosti.
     */
    public static String mType(String str) {
        return Output.HTML.mType(str);
    }

}
