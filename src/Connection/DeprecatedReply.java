package Connection;

import Client.*;


/**
 * Zpracování odpovědí ze serveru. Server zasílá odpovědi v textové podobě.
 * Od sebe jsou odděleny znakem nového řádku. Každý řádek vyžaduje analýzu
 * odpovědi a její zpracování. Typ odpovědi je označen textově nebo číselným kódem.
 * Seznam zpracovávaných odpovědí sdružuje výčtový typ.
 *
 * @author Martin Fouček
 */
public class DeprecatedReply {

    private String message;
    private DeprecatedConnection connection;
    private boolean numeric;

    // casti zpravy
    private DeprecatedPrefix prefix;
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

        fake, ERROR, NOTICE,

        CODE_WELCOME, CODE_221, CODE_251, CODE_252, CODE_253, CODE_254, CODE_255,
        CODE_265, CODE_266,
        
        CODE_301, CODE_305, CODE_306, 
        CODE_366, CODE_372, CODE_375, CODE_376,

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
    private DeprecatedReply(String str, DeprecatedConnection connection) {

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
    private DeprecatedCommandQuery getQuery () {
        return connection.getQuery();
    }


    /**
     * Vytvori objekt typu Reply, ktery nasledne zpracuje.
     *
     * @param str
     * @param connection
     */
    public static void create (String str, DeprecatedConnection connection) {
        new DeprecatedReply(str, connection);
    }

    /**
     * Rozlozeni zpravy dle vzoru
     * http://www.irchelp.org/irchelp/rfc/chapter2.html#c2_3_1
     */
    private void parse () {

        if (message == null || message.length() == 0) {
            //System.err.println("NEZNAMA ZPRAVA: " + message); // Pouze ladění
            return;
        }

        int upto;

        String temp_message = message;

        // ma zprava odesilatele?
        if (temp_message.startsWith(":")) {
            upto = temp_message.indexOf(" ");
            prefix = new DeprecatedPrefix(temp_message.substring(1, upto));
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
     */
    public static String mType (String str) {
        return HTML.mType(str);
    }

    /**
     * Vrati refenci na prislusny kanal.
     * Implementovano kvuli prehlednosti.
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
            case fake:    { /*if ( !isNumeric() ) System.err.println("Nezapomen implementovat obsluhu prikazu " + type + ".");*/ break; }
            case NOTICE:  { handleNotice(); break; }
            case ERROR:   { handleError(); break; }

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

            /*
            Iterator it = s.channels.iterator();
            while ( it.hasNext() ) {
                ChannelTab ch = (ChannelTab) it.next();
                ch.destroy();
            }

            it = s.privateChats.iterator();
            while ( it.hasNext() ) {
                PrivateChatTab ch = (PrivateChatTab) it.next();
                ch.destroy();
            }
            */

            // Znemožní další operace související s připojováním
            connection.setClosedByServer();

            // Vypíše upozornění (heski šéski)
            output( HTML.red( mType("error") ) + " Spojení uzavřeno.", true);
        }

    }

    /**
     * Zpracovani prikazu NOTICE. Vypis smerovan vzdy do panelu serveru.
     */
    private void handleNotice () {

        vyparseTarget();

        params = smileAtMe(params);

        if ( target.equals("AUTH") )
            output( HTML.red( mType("auth") ) + HTML.bold(params), true);
        else
            output( mType("info") + params, true);

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
        String new_mode = HTML.bold( smileAtMe(params) );
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
        String user = HTML.bold(target);
        String reason = smileAtMe(params);
        output( mType("warn") + "Uživatel " + user + " je nepřítomný: " + reason);

    }

    /**
     * Oznameni o zruseni nepritomnosti - do vsech otevrenych kanalu
     * i do serveroveho panelu.
     */
    private void handleCode305 () {

        String afk = HTML.bold("AFK");
        String foo = mType("info") + "Stav nepřítomnosti \"" + afk + "\" byl zrušen.";

        output(foo, true);
        /*
        Iterator it = connection.getServerTab().channels.iterator();
        ChannelTab ch = null;
        while ( it.hasNext() ) {
            ch = (ChannelTab) it.next();
            output(foo, ch);
        }
        */

    }

    /**
     * Oznameni o vlastni nepritomnosti - do vsech otevrenych kanalu
     * i do serveroveho panelu.
     * Implementace teto zpravy je doporucena a nemusi byt proto akceptovana
     * vsemi servery.
     */
    private void handleCode306 () {

        String afk = HTML.bold("AFK");
        String foo = mType("info") + "Nastaven stav nepřítomnosti (" + afk + ").";

        output(foo, true);
        /*
        Iterator it = connection.getServerTab().channels.iterator();
        ChannelTab ch = null;
        while ( it.hasNext() ) {
            ch = (ChannelTab) it.next();
            output(foo, ch);
        }
        */

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

        String voice = HTML.italic("voice");
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
     * Vymaže znak z řetězce - nenahradí, ale vymaže.
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
     */
    public boolean isNumeric () {
        return numeric;
    }

    /**
     * Tunel pro vypis vystupniho textu do prislusneho panelu.
     */
    public void output (String str) {
        output(str, false);
    }

    /**
     * Tunel pro vypis vystupniho textu do panelu serveru.
     */
    public void output (String str, boolean server) {

        if (server)
            connection.getServerTab().appendText(str);
        else
            connection.getTab().appendText(str);

    }

    /**
     * Tunel pro vypis vystupniho textu do libovolneho panelu.
     */
    public void output (String str, AbstractTab tab) {
        tab.appendText(str);
    }

    /**
     * Vrati puvodni odpoved serveru.
     */
    @Override
    public String toString () {
        return message;
    }

}
