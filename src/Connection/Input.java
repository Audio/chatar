package Connection;

import Client.*;
import java.util.Iterator;

/**
 * Zpracovává vstup od uživatele (pouze příkazy, které zná). Seznam známých
 * (povolených) příkazů určuje třída GInput, která příkazy načítá.
 *
 * @author Martin Fouček
 */
public class Input {
    
    /**
     * Aktuálně použivaná záložka tabbed panelu.
     */
    public static AbstractTab currentTab;

    /**
     * Reference na použitý ServerTab.
     *
     * @return
     */
    public static ServerTab getCurrentServer () {
        return currentTab.getServerTab();
    }

    /**
     * Příznak, zda je právě používaným panelem kanál.
     *
     * @return
     */
    public static boolean isActiveChannel () {
        return currentTab.getClass().getSimpleName().equals("GTabChannel");
    }

    /**
     * Tunel pro vystup textu do aktualniho panelu.
     *
     * @param str
     */
    public static void output (String str) {
        currentTab.getConnection().getTab().addText(str);
    }

    /**
     * Odstrani text z textoveho pole (vstup uzivatele).
     * Provadi se v pripade validni syntaxe vkladaneho prikazu.
     */
    public static void clearText () {
        GUI.getInput().getTextField().setText(null);
    }

    /**
     * Overuje stav pripojeni.
     * Vetsina prikazu vyzaduje jiz otevrene pripojeni.
     *
     * @return
     */
    public static boolean isConnected () {
        return (currentTab != null && currentTab.getConnection().isConnected());
    }

    /**
     * Oznameni uzivateli, ze neni pripojen k zadnemu serveru.
     */
    public static void connectionError () {
        new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Připojení nedostupné",
                "Nejste připojen/a k žádnému serveru.");
    }

    /**
     * Oznameni uzivateli, ze nema prepnuto na panel nejakeho kanalu.
     */
    public static void activeChannelError () {
        new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_WARN, "Přepni si na kanál",
                "Aktivním oknem není kanál (channel).");
    }

    /**
     * Uzavre vsechny kanaly pridruzene k serveru.
     * Uzavre panel serveru a ukonci spojeni prikazem QUIT
     * a uzavrenim socketoveho spojeni.
     *
     * @param reason
     */
    public static void handleQuit (String reason) {
        handleQuit(Input.currentTab, reason);
    }

    /**
     * Uzavira spojeni - nikoli aktualniho panelu, ale vybraneho.
     *
     * @param tab
     * @param reason
     */
    public static void handleQuit (AbstractTab tab, String reason) {

        if (currentTab == null) {
            connectionError();
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
     *
     * @param params
     */
    public static void handlePrivMessage (String params) {

        if (!isConnected()) {
            connectionError(); return;
        }

        String user;
        String msg;
        int upto;

        if ((upto = params.indexOf(" ")) == -1) {
            output( mType("error") + "Špatná syntaxe příkazu. Použijte /privmsg prijemce zprava");
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
     *
     * @param channel
     */
    public static void handleJoin (String channel) {

        if (!isConnected()) {
            connectionError();
            return;
        }

        if (channel == null || channel.trim().length() == 0) {
            output( mType("error") + "Špatná syntaxe příkazu. Použijte /join nazev_kanalu");
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
     *
     * @param channel
     */
    public static void handlePart (String channel) {

        if (!isConnected()) {
            connectionError(); return;
        }

        if (channel == null || channel.trim().length() == 0) {
            channel = currentTab.getTabName();
            if ( !isActiveChannel() ) {
                activeChannelError();
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
     *
     * @param nick
     */
    public static void handleNick (String nick) {

        if (!isConnected()) {
            connectionError(); return;
        }
        
        // kontrola syntaxe
        if (nick == null || nick.trim().length() == 0) {
            output( mType("error") + "Nebyla zadána nová přezdívka.");
            return;
        }

        // getCurrentServer().getQuery().nick(nick);
        clearText();

    }

    /**
     * Meni tema ve vybranem kanale.
     *
     * @param topic
     */
    public static void handleTopic (String topic) {

        if (!isConnected()) {
            connectionError();
            return;
        }

        if ( !currentTab.getClass().getSimpleName().equals("GTabChannel") ) {
            output( mType("error") + "Téma lze změnit pouze ve vybraném kanále.");
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
     * 
     * @param channels
     */
    public static void handleNames (String channels) {
        // currentTab.getQuery().names(channels);
        clearText();
    }

    /**
     * Zpracovani prikazu MODE.
     *
     * @param params
     */
    public static void handleMode (String params) {

        if (params != null && params.trim().length() > 0) {
            params = params.trim();
        }
        else {
            output( mType("error") + "Příkaz MODE: nesprávná syntaxe příkazu. Použijte /mode {UZIVATEL|KANAL}");
            return;
        }

        // currentTab.getQuery().mode(params);
        clearText();

    }

    /**
     * Ukonci program.
     */
    public static void handleShutdown () {
        Client.terminate(0);
    }

    /**
     * Pripojeni na zvoleny server.
     *
     * @param address
     */
    public static void handleServer (String address) {
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
    public static void handleKick (String params) {

        if (!isConnected()) {
            connectionError();
            return;
        }

        if (params == null)
            return;

        String syntax = mType("error") + "Nesprávná syntaxe příkazu: KICK #kanal uzivatel [:duvod]";
        String match = "#[a-zA-Z_0-9]+ [a-zA-Z_0-9]+( :.+)?";
        if ( !params.matches(match) ) {
            output(syntax);
            return;
        }

        // currentTab.getQuery().kick(params);
        clearText();

    }

    /**
     * Nastavení ci zrušení nepřítomnosti (AFK - away from keyboard).
     *
     * @param params
     */
    public static void handleAway (String params) {

        if (!isConnected()) {
            connectionError();
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
    public static void handleBack () {
        handleAway(null);
    }

    /**
     * Vymaže obsah aktualního panelu (záložky).
     */
    public static void handleClear () {

        if ( GUI.getTabContainer().getTabCount() > 0)
            currentTab.clearContent();

        clearText();

    }

    /**
     * Žádost uživatele o přidělení práv operátora (mode +o).
     *
     * @param params
     */
    public static void handleOper (String params) {

        if (!isConnected()) {
            connectionError();
            return;
        }

        params = (params == null) ? null : params.trim();
        clearText();

        if (params == null || params.length() == 0 || params.indexOf(" ") == -1) {
            output( mType("error") + "Špatná syntaxe příkazu. Použijte /oper uzivatel heslo");
            return;
        }

        // getCurrentServer().getQuery().oper(params);

    }

    /**
     * Informace o uživateli.
     *
     * @param params
     */
    public static void handleWhois (String params) {
        
        if (!isConnected()) {
            connectionError();
            return;
        }

        params = (params == null) ? null : params.trim();
        clearText();

        if (params == null || params.length() == 0) {
            output( mType("error") + "Špatná syntaxe příkazu. Použijte /whois [server ]uzivatel");
            return;
        }

        // getCurrentServer().getQuery().whois(params);

    }

    /**
     * Vypíše do kanálu, že uživatel provádí akci speficikovanou parametrem.
     * 
     * @param params
     */
    public static void handleMe (String params) {

        if (!isConnected()) {
            connectionError();
            return;
        }

        if ( !isActiveChannel() ) {
            activeChannelError();
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
     *
     * @param invalidCommand
     */
    public static void showError (String invalidCommand) {

        if (Input.currentTab == null) {
            new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Neznámý příkaz",
                    "Neznámý příkaz " + invalidCommand.toUpperCase() + ".");
        }
        else {
            String err = Output.HTML.color( mType("error"), Output.HTML.RED);
            output(err + " Neznámý přijatý příkaz: " + invalidCommand);
        }

    }

    /**
     * Tunýlek pro stejnojmennou metodu v Output.HTML - kvůli přehlednosti.
     *
     * @param str
     * @return
     */
    public static String mType (String str) {
        return Output.HTML.mType(str);
    }

}