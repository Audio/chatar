package Connection;

import Client.*;
import Client.TabContainer.PanelTypes;
import java.util.Iterator;


/**
 * Zpracování odpovědí ze serveru. Server zasílá odpovědi v textové podobě.
 * Od sebe jsou odděleny znakem nového řádku. Každý řádek vyžaduje analýzu
 * odpovědi a její zpracování. Typ odpovědi je označen textově nebo číselným kódem.
 * Seznam zpracovávaných odpovědí sdružuje výčtový typ.
 *
 * @author Martin Fouček
 */
public class Reply {

    private String message;
    private DeprecatedConnection connection;
    private boolean numeric;

    // casti zpravy
    private Prefix prefix;
    private String type;
    private String target;
    private String params;

    /**
     * Znak Start of Heading. Nevykreslitelný.
     */
    private final char SOH = '';
    /**
     * Znak Start of Text. Nevykreslitelný.
     */
    private final char STX = '';

    // vyctovy typ obsahujici povolene prikazy ke zpracovani
    private enum Allowed {

        fake, ERROR, JOIN, KICK, MODE, NICK, NOTICE, PART, PING, PRIVMSG, QUIT, TOPIC,

        CODE_WELCOME, CODE_221, CODE_251, CODE_252, CODE_253, CODE_254, CODE_255,
        CODE_265, CODE_266,
        
        CODE_301, CODE_305, CODE_306, CODE_311, CODE_312, CODE_313, CODE_317,
        CODE_318, CODE_319, CODE_332, CODE_353, CODE_366, CODE_372, CODE_375, CODE_376,

        CODE_401, CODE_404, CODE_422, CODE_432,
        CODE_433, CODE_451, CODE_482,
        ;

        public static Allowed fromString(String Str) {
            try {
                return valueOf(Str);
            }
            catch (Exception e){ return Allowed.fake; }
        }
    };

    /**
     * Konstruktor. Vytvari instanci tridy Reply, ktera slouzi pro analyzu
     * a dalsi zpracovani vsech odpovedi ze serveru.
     *
     * @param str
     * @param connection
     */
    private Reply(String str, DeprecatedConnection connection) {

        this.message = str;
        this.connection = connection;
        this.numeric = false;
        this.parse();
        this.handle();

    }

    /**
     * Vraci referenci na prirazeny objekt CommandQuery.
     *
     * @return
     */
    private CommandQuery getQuery () {
        return connection.getQuery();
    }


    /**
     * Vytvori objekt typu Reply, ktery nasledne zpracuje.
     *
     * @param str
     * @param connection
     */
    public static void create (String str, DeprecatedConnection connection) {
        new Reply(str, connection);
    }

    /**
     * Rozlozeni zpravy dle vzoru
     * http://www.irchelp.org/irchelp/rfc/chapter2.html#c2_3_1
     */
    private void parse () {

        if (message == null || message.length() == 0) {
            //System.err.println("NEZNÁMÁ ZPRÁVA: " + message); // Pouze ladění
            return;
        }

        int upto;

        String temp_message = message;

        // ma zprava odesilatele?
        if (temp_message.startsWith(":")) {
            upto = temp_message.indexOf(" ");
            prefix = new Prefix(temp_message.substring(1, upto));
            temp_message = temp_message.substring(upto + 1);
        }

        // ziska typ zpravy
        upto = temp_message.indexOf(" ");
        if (upto == -1) {
            type = temp_message;
            return;
        }
        else {
            type = temp_message.substring(0, upto);
            temp_message = temp_message.substring(upto + 1);
            if (temp_message.length() < 1) {
                return;
            }
        }

        // ziska parametry
        params = temp_message;
        // nahraní všechny výskyty znaků SOH a STX
        params = removeChar(params, SOH);
        params = removeChar(params, STX);

        /**
         * Je-li typem cislo (3 cislice) pak parametry obsahuji nick adresata.
         * U textovych odpovedi to tak byt nemusi - vyresi konkretni
         * obsluha odpovedi.
         * http://www.irchelp.org/irchelp/rfc/chapter2.html#c2_4
         */
        if (type.matches( "\\d{3}" )) {
            numeric = true;
            vyparseTarget();
        }

    }

    /**
     * Vyparsuje z casti "params" adresata.
     */
    private void vyparseTarget () {

        int upto = params.indexOf(" ");
        if (upto < 0)
            return;

        target = params.substring(0, upto);
        params = params.substring(upto + 1);

    }

    /**
     * Modifikuje vystupni informaci.
     * Nici prvni dvojtecku, kterou nalezne.
     *
     * @param str
     * @return
     */
    public String smileAtMe (String str) {

        int upto;
        if (str.startsWith(":")) {
            str = str.substring(1);
        }
        else if ((upto = str.indexOf(":")) > -1) {
            str = str.substring(0, upto) + str.substring(upto + 1);
        }

        return str.trim();

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

    /**
     * Vrati refenci na prislusny kanal.
     * Implementovano kvuli prehlednosti.
     *
     * @param name
     * @return
     */
    private ChannelTab getChannel (String name) {
        return connection.getServerTab().getChannelTabByName(name);
    }

    /**
     * Vrati refenci na prislusne okno se soukromymi zpravami.
     * Implementovano kvuli prehlednosti.
     *
     * @param name
     * @return
     */
    private PrivateChatTab getPrivateChat (String name) {
        return connection.getServerTab().getPrivateChatByName(name);
    }

    /**
     * Preda zpravu obsluze, ktera ji dale zpracuje.
     * Zpracovava TEXTOVE i CISELNE odpovedi.
     */
    private void handle () {

        if (numeric) {
            if (Integer.parseInt(type) <= 99)
                type = "CODE_WELCOME";
            else
                type = "CODE_" + type;

            // vypis pouze tech zprav, ktere nemaji obsluhu
            try {
                Allowed.valueOf(type);
            }
            catch (IllegalArgumentException e) { }
        }

        switch ( Allowed.fromString(type) ) {
            case fake:    { /*if ( !isNumeric() ) System.err.println("Nezapomeň implementovat obsluhu příkazu " + type + ".");*/ break; }
            case NOTICE:  { handleNotice(); break; }
            case PING:    { handlePing(); break; }
            case ERROR:   { handleError(); break; }
            case PRIVMSG: { handlePrivMsg(); break; }
            case JOIN:    { handleJoin(); break; }
            case PART:    { handlePart(); break; }
            case KICK:    { handleKick(); break; }
            case TOPIC:   { handleTopic(); break; }
            case NICK:    { handleNick(); break; }
            case MODE:    { handleMode(); break; }
            case QUIT:    { handleQuit(); break; }

            // Uvitaci zpravy.
            case CODE_WELCOME: { handleCodeWelcome(); break; }
            case CODE_265: { handleCodeWelcome(); break; }
            case CODE_266: { handleCodeWelcome(); break; }

            // Unikatni zpravy.
            case CODE_221: { handleCode221(); break; }
            case CODE_251: { handleCode251(); break; }
            case CODE_252: { handleCode252(); break; }
            case CODE_253: { handleCode253(); break; }
            case CODE_254: { handleCode254(); break; }
            case CODE_255: { handleCode255(); break; }
            case CODE_301: { handleCode301(); break; }
            case CODE_305: { handleCode305(); break; }
            case CODE_306: { handleCode306(); break; }
            case CODE_311: { handleCode311(); break; }
            case CODE_312: { handleCode312(); break; }
            case CODE_313: { handleCode313(); break; }
            case CODE_317: { handleCode317(); break; }
            case CODE_318: { handleCode318(); break; }
            case CODE_319: { handleCode319(); break; }
            case CODE_332: { handleCode332(); break; }
            case CODE_353: { handleCode353(); break; }
            case CODE_401: { handleCode401(); break; }
            case CODE_404: { handleCode404(); break; }
            case CODE_432: { handleCode432(); break; }
            case CODE_433: { handleCode433(); break; }
            case CODE_451: { handleCode451(); break; }
            case CODE_482: { handleCode482(); break; }

            // Message of the day.
            case CODE_372: { handleMOTD(); break; }
            case CODE_375: { handleMOTD(); break; }
            case CODE_376: { handleMOTD(); break; }
            case CODE_422: { handleMOTD(); break; }

            // Zpravy, ktere nemaji pro uzivatele informacni hodnotu.
            case CODE_366: { break; }
        }

        // Pouze pro účely ladění
        //if ( Allowed.fromString(type).equals(Allowed.fake) )
            //System.out.println("Neznámá přijatá zpráva: " + this);

    }

    /**
     * Zpracování příkazu ERROR.
     * Obsahuje-li text ve tvaru "Closing Link: *duvod*", pak je spojení ukončeno.
     */
    private void handleError () {

        params = smileAtMe(params);
        output( mType("error") + params);

        if ( params.startsWith("Closing Link:") ) {
            // uzavře vsechny místnosti a soukromé chaty
            ServerTab s = connection.getServerTab();

            Iterator it = s.channels.iterator();
            while ( it.hasNext() ) {
                ChannelTab ch = (ChannelTab) it.next();
                ch.die();
                ch.killMyself();
            }

            it = s.privateChats.iterator();
            while ( it.hasNext() ) {
                PrivateChatTab ch = (PrivateChatTab) it.next();
                ch.die();
                ch.killMyself();
            }

            // Znemožní další operace související s připojováním
            connection.setClosedByServer();

            // Vypíše upozornění (heski šéski)
            output( Output.HTML.color( mType("error") , Output.HTML.RED)
                    + " Spojení uzavřeno.", true);
        }

    }

    /**
     * Zpracovani prikazu NOTICE. Vypis smerovan vzdy do panelu serveru.
     */
    private void handleNotice () {

        vyparseTarget();

        params = smileAtMe(params);

        if ( target.equals("AUTH") )
            output( Output.HTML.color( mType("auth") , Output.HTML.RED)
                    + Output.HTML.bold(params), true);
        else
            output( mType("info") + params, true);

    }

    /**
     * Zpracovani prikazu PING.
     */
    private void handlePing () {
        getQuery().pong(params);
        String user = Output.HTML.bold( smileAtMe(params) );
        output( mType("pong") + "Odeslána odezva uživateli " + user + ".", true);
    }

    /**
     * Zpracovani prikazu PRIVMSG.
     * Existuje-li panel s nazvem stejnym jako adresat prikazu,
     * vypise se zprava do tohoto panelu.
     * V opacnem pripade se vypise do aktualne zvoleneho panelu.
     */
    private void handlePrivMsg () {

        vyparseTarget();
        String out = prefix.nick + ": " + smileAtMe(params);

        // Jedná se o kanál nebo uživatele, co mi píše soukromou zprávu?
        if ( !target.equals(connection.config.nickname) ) {
            // kanál
            if ( isAction() )
                out = modifyAction();

            // výstup
            ChannelTab channel = getChannel(target);
            if (channel == null)
                output(out);
            else
                output(out, channel);
        }
        else {
            // soukroma zprava

            // pokud se jedna o PING, nevypisuje se
            if ( isPing() ) {
                params = prefix.nick;
                handlePing();
                return;
            }

            // otevre okno pro soukromy chat (pokud neni otevreno)
            PrivateChatTab chat = getPrivateChat(prefix.nick);
            if (chat == null) {
                try {
                    MainWindow.getInstance().addTab(PanelTypes.PANEL_PRIVATE, prefix.nick);
                    chat = getPrivateChat(prefix.nick);
                    chat.setFocus();
                } catch (ClientException e) { return; }
            }
            output(out, chat);
            if (MainWindow.getInstance().getActiveTab() != chat)
                chat.setToRead(true);
        }

    }

    /**
     * Zpracovani prikazu JOIN.
     */
    private void handleJoin () {

        String channel = smileAtMe(params);
        ChannelTab ch = getChannel(channel);
        if (ch == null)
            return;

        output( mType("info") + prefix.nick + " se připojil na " + channel, ch);
        ch.addUser(prefix.nick);

    }

    /**
     * Zpracovani prikazu PART.
     * Pokud odesel nas uzivatel, zadna z akci se neprovede,
     * zvoleny kanal (panel) se totiz zavre.
     */
    private void handlePart () {

        if ( connection.isMe(prefix.nick) )
            return;

        String channel = smileAtMe(params);
        ChannelTab ch = getChannel(channel);
        if (ch == null)
            return;

        output( mType("info") + prefix.nick + " odešel z místnosti.", ch);
        ch.removeUser(prefix.nick);

    }

    /**
     * Zpracovani prikazu KICK.
     *
     * Nastane jeden z dvou ruznych pripadu:
     * 1/ Je vyhozen uzivatel tohoto klienta. V takovem pripade se uzavre
     *    panel se zvolenym kanalem (odkud byl vyhozen). Informace o vyhazovu
     *    je pote zobrazena v panelu serveru.
     * 2/ Je vyhozen nekdo jiny. Ve vybranem kanale zobrazi danou informaci.
     */
    private void handleKick () {
        
        vyparseTarget();
        String channel = target;
        vyparseTarget();
        String person = target;
        String reason = smileAtMe(params);

        ChannelTab channel_tab = getChannel(channel);
        if (channel_tab == null)
            return;

        if ( connection.isMe(person) ) {
            String msg = mType("info") + "Byl/a jste vyhozen/a z kanálu " + channel + ". Důvod: " + reason;
            output(msg, channel_tab.getServerTab() );
            channel_tab.die();
            channel_tab.killMyself();
        }
        else {
            String msg = mType("info") + "Uživatel " + person + " byl vyhozen z místnosti. Důvod: " + reason;
            output(msg, channel_tab);
            channel_tab.removeUser(person);
        }

    }


    /**
     * Zpracovani prikazu TOPIC.
     */
    private void handleTopic () {

        vyparseTarget();
        String topic = smileAtMe(params);

        ChannelTab channel = connection.getServerTab().getChannelTabByName(target);
        channel.setTopic(topic);

        topic = Output.HTML.bold(topic);
        output( mType("info") + prefix.nick + " nastavil téma na " + topic, channel);

    }

    /**
     * Zpracovani prikazu NICK. Zprava se vypise do vsech mistnosti (kanalu),
     * do zalozky se serverem, a meni i nastaveni soukromeho chatu (je-li aktivni).
     */
    private void handleNick () {

        String new_nickname = smileAtMe(params);
        String temp = "";
        boolean isMe = connection.isMe(prefix.nick);

        if (isMe) {
            connection.config.nickname = new_nickname;
            temp = mType("info") + "Nyní vystupujete pod přezdívkou "
                    + Output.HTML.bold(new_nickname) + ".";
            output(temp, true);
            GUI.getInput().setNickname(new_nickname);
        }
        else {
            temp = mType("info") + prefix.nick + " si změnil předívku na "
                    + Output.HTML.bold(new_nickname) + ".";
        }

        /* Získa nový seznam uživatelů ve vybraných kanalech.
         *
         * Na některých serverech nelze odeslat prikaz NAMES s více cíly,
         * proto se odešle více příkazů (pro každý kanál jeden).
         */
        Iterator it = connection.getServerTab().channels.iterator();
        while ( it.hasNext() ) {
            ChannelTab ch = (ChannelTab) it.next();
            if (isMe || ch.hasNick(prefix.nick) ) {
                output(temp, ch);
                ch.changeUsersNickname(prefix.nick, new_nickname);
            }
        }

        /**
         * Existuje-li panel pro soukromy chat s uzivatelem, ktery si prave
         * zmenil prezdivku, prejmenuje se.
         */
        it = connection.getServerTab().privateChats.iterator();
        PrivateChatTab pr = null;
        while ( it.hasNext() ) {
            pr = (PrivateChatTab) it.next();
            if (isMe) {
                // oznameni o zmene meho nicku do soukromych mistnosti
                output(temp, pr);
            }
            else if ( pr.getTabName().equals(prefix.nick) ) {
                // uzivatel, se kterym si pisi soukromne, zmenil prezdivku
                output(temp, pr);
                pr.setTabName(new_nickname);
                GUI.getTabContainer().setTitleAt( GUI.getTabContainer().indexOfTab(prefix.nick) , new_nickname);
            }
        }

    }

    /**
     * Zpracovani prikazu MODE.
     */
    private void handleMode () {

        vyparseTarget();

        String channel = target;
        ChannelTab ch = getChannel(channel);
        if (ch == null)
            return;

        // tyka se kanalu
        if ( params.trim().indexOf(" ") == -1 ) {
            String mode = Output.HTML.bold( smileAtMe(params) );
            output( mType("info") + "Mód kanálu byl změněn: " + mode + " (" + prefix.nick + ")", ch);
        }
        // tyka se uzivatele
        else {
            vyparseTarget();
            String mode = Output.HTML.bold(target);
            String user = smileAtMe(params);

            output( mType("info") + "Mód uživatele " + user + " byl změněn: " + mode + " (" + prefix.nick + ")", ch);

            // TODO feature: zmena mode
            // getQuery().names(channel);
        }

    }

    /**
     * Zpracování příkazu QUIT.
     * Zpráva se zobrazí pouze v kanálech, ve kterých daný uživatel byl.
     */
    private void handleQuit () {

        String user = prefix.nick;
        String temp = mType("info") + "Uživatel " + Output.HTML.bold(user)
                + " se odpojil (" + smileAtMe(params) + ").";

        Iterator it = connection.getServerTab().channels.iterator();
        while ( it.hasNext() ) {
            ChannelTab ch = (ChannelTab) it.next();
            if ( ch.hasNick(user) ) {
                ch.removeUser(user);
                output(temp, ch);
            }
        }

    }

    /**
     * Uvítací zprávy. Označeny čísly 001 - 099. Využívání až od RFC 2812.
     */
    private void handleCodeWelcome () {
        output( mType("info") + smileAtMe(params), true);
    }


    /**
     * Aktualne nastavene mody uzivatele.
     */
    private void handleCode221 () {
        vyparseTarget();
        String new_mode = Output.HTML.bold( smileAtMe(params) );
        output( mType("mode") + "Nastaven mod " + new_mode, true);
        // TODO feature: mode set
    }


    /**
     * RPL_LUSERCLIENT
     * ":There are <integer> users and <integer> invisible on <integer> servers"
     */
    private void handleCode251 () {
        output( mType("fact") + smileAtMe(params), true);
    }


    /**
     * RPL_LUSEROP
     * "<integer> :operator(s) online"
     */
    private void handleCode252 () {
        output( mType("fact") + smileAtMe(params), true);
    }


    /**
     * RPL_LUSERUNKNOWN
     * "<integer> :unknown connection(s)"
     */
    private void handleCode253 () {
        output( mType("fact") + smileAtMe(params), true);
    }


    /**
     * RPL_LUSERCHANNELS
     * "<integer> :channels formed"
     */
    private void handleCode254 () {
        output( mType("fact") + smileAtMe(params), true);
        vyparseTarget();
        connection.getServerTab().setChannelsCount(target);
    }


    /**
     * RPL_LUSERME
     * ":I have <integer> clients and <integer> servers"
     */
    private void handleCode255 () {
        output( mType("fact") + smileAtMe(params), true);
    }

    /**
     * Oznámení - uživatel, se kterým chceme komunikovat je nepřítomen.
     */
    private void handleCode301 () {

        vyparseTarget();
        String user = Output.HTML.bold(target);
        String reason = smileAtMe(params);
        output( mType("warn") + "Uživatel " + user + " je nepřítomný: " + reason);

    }

    /**
     * Oznameni o zruseni nepritomnosti - do vsech otevrenych kanalu
     * i do serveroveho panelu.
     */
    private void handleCode305 () {

        String afk = Output.HTML.bold("AFK");
        String foo = mType("info") + "Stav nepřítomnosti \"" + afk + "\" byl zrušen.";

        output(foo, true);
        Iterator it = connection.getServerTab().channels.iterator();
        ChannelTab ch = null;
        while ( it.hasNext() ) {
            ch = (ChannelTab) it.next();
            output(foo, ch);
        }

    }

    /**
     * Oznameni o vlastni nepritomnosti - do vsech otevrenych kanalu
     * i do serveroveho panelu.
     * Implementace teto zpravy je doporucena a nemusi byt proto akceptovana
     * vsemi servery.
     */
    private void handleCode306 () {

        String afk = Output.HTML.bold("AFK");
        String foo = mType("info") + "Nastaven stav nepřítomnosti (" + afk + ").";

        output(foo, true);
        Iterator it = connection.getServerTab().channels.iterator();
        ChannelTab ch = null;
        while ( it.hasNext() ) {
            ch = (ChannelTab) it.next();
            output(foo, ch);
        }

    }

    /**
     * WHOIS - RPL_WHOISUSER - nickname, username, host..
     */
    private void handleCode311 () {

        vyparseTarget();
        params = target + " Uživatel " + Output.HTML.bold( smileAtMe(target) ) + ": "
               + smileAtMe(params) + ".";
        handleWhois();

    }

    /**
     * WHOIS - RPL_WHOISSERVER - server info.
     */
    private void handleCode312 () {

        vyparseTarget();
        params = target + " Připojen k " + Output.HTML.bold( smileAtMe(params) ) + ".";
        handleWhois();

    }

    /**
     * WHOIS - RPL_WHOISOPERATOR - na jakých kanálech je uživatel operátorem.
     */
    private void handleCode313 () {

        vyparseTarget();
        params = target + " Operátorem na " + Output.HTML.bold( smileAtMe(params) ) + ".";
        handleWhois();

    }

    /**
     * WHOIS - RPL_WHOISIDLE - neaktivita vyjádřená v sekundách.
     * Do první mezery je čas nečinnosti (v sekundách).
     */
    private void handleCode317 () {

        vyparseTarget();
        String time = params.substring(0, params.indexOf(' ') );
        params = target + " Nečinný " + Output.HTML.bold(time + " sekund") + ".";

        handleWhois();

    }

    /**
     * WHOIS - RPL_ENDOFWHOIS - konec výpisu.
     */
    private void handleCode318 () {

        vyparseTarget();
        params = target + " Konec výpisu /WHOIS.";
        handleWhois();

    }

    /**
     * WHOIS - RPL_WHOISCHANNELS - na jakých kanálech se uživatel právě nachází.
     */
    private void handleCode319 () {

        vyparseTarget();
        params = target + " Přítomen na " + Output.HTML.bold( smileAtMe(params) ) + ".";
        handleWhois();

    }

    /**
     * Oznameni o nastaveni tematu prislusneho kanalu.
     */
    private void handleCode332 () {

        vyparseTarget();
        String channel = target;
        String topic = Output.HTML.bold( smileAtMe(params) );

        ChannelTab ch = getChannel(channel);
        output( mType("info") + "Aktuální téma je " + topic, ch);
        ch.setTopic(topic);

    }

    /**
     * Vypis seznamu uzivatelu na kanale(ch).
     */
    private void handleCode353 () {

        // TODO feature: prvni znak: * & = mody kanalu

        // odstrani rovniko a mezeru
        params = params.substring(2);

        // vse do prvni mezery je nazev kanalu
        int upto = params.indexOf(" ");
        String channel = params.substring(0, upto);
        params = params.substring(upto + 2);

        ChannelTab ch = getChannel(channel);
        if (ch == null)
            return;

        ch.setUsers(params);

    }

    /**
     * Neexistujici prezdivka / nazev kanalu.
     */
    private void handleCode401 () {
        vyparseTarget();
        output( mType("error") + "Neexistující přezdívka/kanál: " + target);
    }

    /**
     * Klient nema dostatecna prava odesilat zpravy
     * na moderovany kanal.
     */
    private void handleCode404 () {

        vyparseTarget();
        String channel = target;
        ChannelTab ch = getChannel(channel);
        if (ch == null)
            return;

        String voice = Output.HTML.italic("voice");
        output( mType("error") + "K odesílání zpráv potřebujete " + voice + " (+v).", ch);

    }

    /**
     * Spatna syntaxe nove zvolene prezdivky.
     */
    private void handleCode432 () {
        output( mType("error") + "Nesprávně zvolená přezdívka.");
    }

    /**
     * Nove zvolena prezdivka je jiz pouzita jinym uzivatelem.
     * Pokud neni uzivatel k serveru prihlasen (pripad, kdy byla odeslana
     * data na server automaticky, nikoli rucni zmena prezdivky uzivatelem),
     * klient se automaticky pokusi modifikovat prezdivku pridanim dvou
     * cifer na jeji konec. Tim by mela byt zarucena unikatnost nove prezdivky.
     */
    private void handleCode433 () {
        output( mType("error") + "Přezdívka je již použita.");
        if ( !connection.isAuthenticated() )
            handleCode451();
    }

    /**
     * Oznameni "you have not registered". Server tak odpovida
     * pri zpracovani klientskych prikazu z toho duvodu,
     * ze klient (uzivatel) doposud nebyl zaregistrovan
     * (uspesne prihlasen k serveru).
     *
     * Hlaska vznika predevsim pri pokusu o pripojeni s prezdivkou,
     * ktera je jiz na serveru pouzita. V takovem pripade modifikujeme
     * uzivatelovu prezdivku (prirazenim dvou cifer jako postfix)
     * a odesleme zadost o jeji zmenu.
     */
    private void handleCode451 () {
        String nick = connection.config.nickname
                    + connection.config.random();
        getQuery().nick(nick);
        connection.config.nickname = nick;
        GUI.getInput().setNickname(nick);
    }

    /**
     * Oznameni "You're not channel operator"
     */
    private void handleCode482 () {

        vyparseTarget();
        String channel = target;
        String foo     = mType("error") + "Pro tento úkon potřebujete práva operátora.";

        ChannelTab ch = getChannel(channel);

        if (ch == null)
            return;

        output(foo, ch);

    }

    /**
     * Priznak, ze probehla zprava MOTD (uvitaci zprava Message of the day).
     */
    private void handleMOTD () {
        connection.authenticate();
        output( mType("motd") + smileAtMe(params), true);
    }

    /**
     * Zpracování všech odpovědí týkajících se WHOIS příkazu.
     * Odpovědi mají stejnou syntaxi a akce pro výpis je jednotná.
     *
     * Je-li právě otevřen (a vybrán) soukromý chat s daným uživatelem,
     * pak se pouze updatují informace v panelu.
     * V jiném případě se (krom výše uvedeného) do aktualního panelu vypisuje výstup.
     *
     * Není-li soukromý chat s uživatelem vůbec otevřený, informace se v něm neupdatují.
     */
    private void handleWhois () {

        vyparseTarget();
        String foo = mType("whois") + Output.HTML.bold(target) + ": " + params;

        // Jsme v nějakém chatu?
        boolean activeChat = ( MainWindow.getInstance().getActiveTab() instanceof PrivateChatTab );

        // Píšeme si zrovna s uživatelem, jehož WHOIS vypisujeme?
        boolean chatWithTarget = false;
        if (activeChat) {
            String nick = MainWindow.getInstance().getActiveTab().getTabName();
            if ( nick.equals(target) )
                chatWithTarget = true;
        }

        // Výpis na aktuální výstup
        if (!chatWithTarget)
            output(foo);

        // Provedeme update informací?
        // (END of WHOIS se neoznamuje - kod 319)
        // (kod 311 - zacatek WHOIS - maze stavajici info)
        PrivateChatTab pc = getPrivateChat(target);
        if (pc == null || type.equals("CODE_318") )
            return;

        if ( type.equals("CODE_311") )
            pc.clearUserInfo();

        pc.updateUserInfo(params);

    }

    /**
     * Příznak, zda se jedná o zprávu typu ACTION.
     * Lze vysledovat až z těla zprávy.
     *
     * @return
     */
    private boolean isAction () {
        return ( smileAtMe(params) ).startsWith("ACTION");
    }

    /**
     * Modifikuje výstup zprávy při ACTION.
     *
     * @return
     */
    private String modifyAction () {
        String out = prefix.nick + " " + smileAtMe(params).substring(7);
        return Output.HTML.italic(out);
    }

    /**
     * Příznak, zda se jedná o zprávu typu PING (ne od server, ale klienta).
     * Lze vysledovat až z těla zprávy.
     *
     * @return
     */
    private boolean isPing () {
        return ( smileAtMe(params) ).startsWith("PING");
    }

    /**
     * Vymaže znak z řetězce - nenahradí, ale vymaže.
     *
     * @param str
     * @param ch
     */
    private String removeChar (String str, char ch) {

        int pos;
        while ( (pos = str.indexOf(ch)) > -1 )
            str = str.substring(0, pos) + str.substring(pos + 1);

        return str;

    }



    /**
     * Vraci informaci, zda byla odpoved vyjadrena cislem (kodem odpovedi).
     * Typy odpovedi: http://www.irchelp.org/irchelp/rfc/chapter6.html
     *
     * @return
     */
    public boolean isNumeric () {
        return numeric;
    }

    /**
     * Tunel pro vypis vystupniho textu do prislusneho panelu.
     *
     * @param str
     */
    public void output (String str) {
        output(str, false);
    }

    /**
     * Tunel pro vypis vystupniho textu do panelu serveru.
     *
     * @param str
     * @param server
     */
    public void output (String str, boolean server) {

        if (server)
            connection.getServerTab().addText(str);
        else
            connection.getTab().addText(str);

    }

    /**
     * Tunel pro vypis vystupniho textu do libovolneho panelu.
     *
     * @param str
     * @param tab
     */
    public void output (String str, AbstractTab tab) {
        tab.addText(str);
    }

    /**
     * Vrati puvodni odpoved serveru.
     *
     * @return
     */
    @Override
    public String toString () {
        return message;
    }

}
