package Connection;


/**
 * Response prefix. Contains up to 3 parts:
 * <servername> | <nick> [ '!' <user> ] [ '@' <host> ]
 * source: http://www.irchelp.org/irchelp/rfc/chapter2.html#c2_3_1
 */
public class Prefix {

    public String originalPrefix;
    public String nick;
    public String username;
    public String hostname;


    public Prefix(String prefix) {
        originalPrefix = prefix;

        int upto;

        // the <nick> part
        if ( (upto = prefix.indexOf("!")) < 0 ) {
            nick = prefix;
            return;
        }

        // the <user> part
        nick = prefix.substring(0, upto);
        username = prefix.substring(upto + 1);

        // the <host> part
        if ( (upto = username.indexOf("@")) > -1 ) {
            hostname = username.substring(upto + 1);
            username = username.substring(0, upto);
        }
    }

    @Override
    public String toString() {
        return originalPrefix;
    }

}


