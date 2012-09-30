package Connection;

import Client.*;
import java.util.Iterator;


/**
 * Zpracovává vstup od uživatele (pouze příkazy, které zná). Seznam známých
 * (povolených) příkazů určuje třída GInput, která příkazy načítá.
 */
public class Input {
    
    public static AbstractTab currentTab;


    public static ServerTab getCurrentServer() {
        return currentTab.getServerTab();
    }

    public static boolean isChannelTabActive() {
        // TODO instanceof? instanceof ChannelTab
        return currentTab.getClass().getSimpleName().equals("GTabChannel");
    }

    public static void outputToCurrentTab(String str) {
        currentTab.getConnection().getTab().addText(str);
    }

    public static void clearText() {
        MainWindow.getInstance().getGInput().getTextField().setText(null);
    }

    public static boolean isConnected() {
        return (currentTab != null && currentTab.getConnection().isConnected() );
    }

    public static void showNoConnectionError() {
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
        handleQuit(Input.currentTab, reason);
    }

    /**
     * Uzavira spojeni - nikoli aktualniho panelu, ale vybraneho.
     */
    public static void handleQuit (AbstractTab tab, String reason) {
        if (currentTab == null) {
            showNoConnectionError();
            return;
        }

        ServerTab server = tab.getServerTab();

        // pozavira vsechny kanaly
        Iterator it = server.channels.iterator();
        while ( it.hasNext() ) {
            ChannelTab channel = (ChannelTab) it.next();
            channel.killMyself();
        }

        // pozavira soukroma okna
        it = server.privateChats.iterator();
        while ( it.hasNext() ) {
            PrivateChatTab chat = (PrivateChatTab) it.next();
            chat.killMyself();
        }

        clearText();

        server.die(reason);
        server.killMyself();

        if ( GUI.getTabContainer().getTabCount() == 0 ) {
            Input.currentTab = null;
        }
    }

    /**
     * Odesle soukromou zpravu vybranemu uzivateli,
     * nebo odesle zpravu do vybraneho kanalu.
     */
    public static void handlePrivMessage (String params) {
        if (!isConnected()) {
            showNoConnectionError();
            return;
        }

        String user;
        String msg;
        int upto;

        if ((upto = params.indexOf(" ")) == -1) {
            outputToCurrentTab( mType("error") + "Špatná syntaxe příkazu. Použijte /privmsg prijemce zprava");
            return;
        }

        user = params.substring(0, upto);
        msg  = ":" + params.substring(upto + 1);

        // currentTab.getQuery().privMsg(user, msg);
        clearText();

        // Otevření nového okna při soukromé zprávě (tzn. uživateli)
        if ( user.startsWith("#") == false && getCurrentServer().getPrivateChatByName(user) == null) {
            try {
                MainWindow.getInstance().addTab(TabContainer.PANEL_PRIVATE, user);
            } catch (ClientException e) { }
        }
    }

    /**
     * Pripoji se na vybrany kanal.
     * Otevre novy panel pro komunikaci na zminenem kanale.
     */
    public static void handleJoin(String channel) {
        if ( !isConnected() ) {
            showNoConnectionError();
            return;
        }

        if (channel == null || channel.trim().length() == 0) {
            outputToCurrentTab( mType("error") + "Špatná syntaxe příkazu. Použijte /join nazev_kanalu");
            return;
        }

        if (channel.startsWith("#")) {
            channel = channel.substring(1);
        }

        // neotevre panel, co jiz existuje; pouze prepne
        ChannelTab exists = getCurrentServer().getChannelTabByName("#" + channel);
        if (exists != null) {
            GUI.getTabContainer().setSelectedComponent( exists );
            clearText();
            return;
        }

        try {
            MainWindow.getInstance().addTab(TabContainer.PANEL_CHANNEL, channel);
            // currentTab.getQuery().join(channel);
            clearText();
        } catch (ClientException e) { }
    }

    /**
     * Zavre panel se zvolenym nazvem a odejde z kanalu.
     * Odstrani se take ze seznamu kanalu v objektu ServerTab.
     */
    public static void handlePart(String channel) {
        if (!isConnected()) {
            showNoConnectionError(); return;
        }

        if (channel == null || channel.trim().length() == 0) {
            channel = currentTab.getTabName();
            if ( !isChannelTabActive() ) {
                showNotActiveChannelError();
                return;
            }
        }

        if ( channel.startsWith("#") )
            channel = channel.substring(1);

        ChannelTab channel_tab = getCurrentServer().getChannelTabByName("#" + channel);

        // overi existenci panelu
        if (channel_tab == null) {
            clearText();
            return;
        }

        // channel_tab.getQuery().leave(channel);
        channel_tab.die();
        channel_tab.killMyself();
        clearText();
    }

    /**
     * Mění přezdívku uživatele.
     */
    public static void handleNick(String nick) {
        if ( !isConnected() ) {
            showNoConnectionError(); return;
        }
        
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
        if (!isConnected()) {
            showNoConnectionError();
            return;
        }

        if ( !currentTab.getClass().getSimpleName().equals("GTabChannel") ) {
            outputToCurrentTab( mType("error") + "Téma lze změnit pouze ve vybraném kanále.");
            return;
        }

        if (topic != null)
            topic = topic.trim();

        String channel = currentTab.getTabName().substring(1);
        // currentTab.getQuery().topic(channel, topic );
        clearText();
    }
    
    /**
     * Zpracovani prikazu NAMES.
     */
    public static void handleNames(String channels) {
        // currentTab.getQuery().names(channels);
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

        // currentTab.getQuery().mode(params);
        clearText();
    }

    /**
     * Ukonci program.
     */
    public static void handleShutdown() {
        Client.terminate(0);
    }

    /**
     * Pripojeni na zvoleny server.
     */
    public static void handleServer(String address) {
        try {
            MainWindow.getInstance().addTab(TabContainer.PANEL_SERVER, address);
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
        if ( !isConnected() ) {
            showNoConnectionError();
            return;
        }

        if (params == null)
            return;

        String syntax = mType("error") + "Nesprávná syntaxe příkazu: KICK #kanal uzivatel [:duvod]";
        String match = "#[a-zA-Z_0-9]+ [a-zA-Z_0-9]+( :.+)?";
        if ( !params.matches(match) ) {
            outputToCurrentTab(syntax);
            return;
        }

        // currentTab.getQuery().kick(params);
        clearText();
    }

    /**
     * Nastavení ci zrušení nepřítomnosti (AFK - away from keyboard).
     */
    public static void handleAway(String params) {
        if (!isConnected()) {
            showNoConnectionError();
            return;
        }

        /*
        if (params == null || params.trim().length() == 0)
            currentTab.getQuery().away(null);
        else
            currentTab.getQuery().away( params.trim() );
            */

        clearText();
    }

    /**
     * Zrušení nepřítomnosti (AFK).
     */
    public static void handleBack() {
        handleAway(null);
    }

    /**
     * Vymaže obsah aktualního panelu (záložky).
     */
    public static void handleClear() {
        if ( GUI.getTabContainer().getTabCount() > 0)
            currentTab.clearContent();

        clearText();
    }

    /**
     * Žádost uživatele o přidělení práv operátora (mode +o).
     */
    public static void handleOper(String params) {
        if ( !isConnected() ) {
            showNoConnectionError();
            return;
        }

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
        
        if (!isConnected()) {
            showNoConnectionError();
            return;
        }

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
        if (!isConnected()) {
            showNoConnectionError();
            return;
        }

        if ( !isChannelTabActive() ) {
            showNotActiveChannelError();
            return;
        }

        String channel = currentTab.getTabName();

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
        if (Input.currentTab == null) {
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
