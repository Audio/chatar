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
    private boolean numeric;

    // casti zpravy
    private String type;
    private String target;
    private String params;

    // vyctovy typ obsahujici povolene prikazy ke zpracovani
    private enum Allowed {

        fake, NOTICE,
        CODE_221, CODE_251, CODE_252, CODE_253, CODE_254, CODE_255,
        CODE_301, CODE_305, CODE_306, 
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
    private DeprecatedReply(String str) {

        this.message = str;
        this.numeric = false;
        this.handle();

    }

    /**
     * Vytvori objekt typu Reply, ktery nasledne zpracuje.
     *
     * @param str
     * @param connection
     */
    public static void create (String str) {
        new DeprecatedReply(str);
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
    // TODO tohle by se mozna este mohlo hodit, ne?
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
        return HTML.small(str);
    }

    /**
     * Preda zpravu obsluze, ktera ji dale zpracuje.
     * Zpracovava TEXTOVE i CISELNE odpovedi.
     */
    private void handle () {

        switch ( Allowed.fromString(type) ) {
            case fake:    { /*if ( !isNumeric() ) System.err.println("Nezapomen implementovat obsluhu prikazu " + type + ".");*/ break; }
            case NOTICE:  { handleNotice(); break; }

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

            // Zpravy, ktere nemaji pro uzivatele informacni hodnotu.
        }

        // Pouze pro účely ladění
        //if ( Allowed.fromString(type).equals(Allowed.fake) )
            //System.out.println("Neznámá přijatá zpráva: " + this);

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
        // connection.getServerTab().setChannelsCount(target);
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
