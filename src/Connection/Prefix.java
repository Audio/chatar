package Connection;

/**
 * Prefix odpovedi. Obsahuje az 3 slozky:
 * <servername> | <nick> [ '!' <user> ] [ '@' <host> ]
 * zdroj: http://www.irchelp.org/irchelp/rfc/chapter2.html#c2_3_1
 *
 * @author Martin Fouček
 */
public class Prefix {

    /**
     * Odesílatel zprávy, obsahuje všechny informační složky zprávy.
     */
    public String prefix;
    /**
     * Přezdívka odesílatele.
     */
    public String nick;
    /**
     * Uživatelské jméno odesílatele.
     */
    public String user;
    /**
     * Hostitelské jméno  odesílatelova počítače.
     */
    public String host;

    /**
     * Parsuje prefix na jednotlive casti.
     *
     * @param prefix
     */
    public Prefix(String prefix) {

        this.prefix = prefix;

        int upto;

        // cast <nick>
        if ((upto = prefix.indexOf("!")) < 0) {
            nick = prefix;
            return;
        }

        // cast <user>
        nick = prefix.substring(0, upto);
        user = prefix.substring(upto + 1);

        // cast <host>
        if ((upto = user.indexOf("@")) > -1) {
            host = user.substring(upto + 1);
            user = user.substring(0, upto);
        }

    }

    /**
     * Vraci puvodni (nezmeneny) prefix odpovedi.
     *
     * @return
     */
    @Override
    public String toString () {
        return prefix;
    }

}


