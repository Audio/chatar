package Connection;

import Client.*;


public class DeprecatedReply {

    private String params;

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
     * Aktualne nastavene mody uzivatele.
     */
    private void handleCode221 () {
        // vyparseTarget();
        String new_mode = HTML.bold( smileAtMe(params) );
        output( mType("mode") + "Nastaven mod " + new_mode, true);
        // TODO feature: mode set
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

}
