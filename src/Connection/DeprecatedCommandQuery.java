package Connection;

import java.util.Stack;

/**
 * Třída CommandQuery slouží k odesílání příkazů serveru. Veškeré příkazy
 * a odpovědi klienta procházejí přes instanci této třídy. Každá instance
 * je přiřazena k instanci spojení se serverem (třída Connection).
 *
 * @author Martin Fouček
 */
public class DeprecatedCommandQuery {

    public void addCommand (String command) {
    }

    /**
     * Žádost uživatele o přidělení práv operátora (mode +o).
     *
     * @param params
     */
    public void oper (String params) {
        addCommand("OPER " + params.trim() );
    }

    /**
     * Připojení na veřejný kanál.
     *
     * @param channel
     */
    public void join (String channel) {
        join(channel, null);
    }

    /**
     * Pripojeni na privatni kanal (pokud je nastaven klic "key").
     * V opacnem pripade se pokusi pripojit na verejny kanal.
     *
     * @param channel
     * @param key
     */
    public void join (String channel, String key) {

        String to;
        if (key == null)
            to = "#" + channel;
        else
            to = "&" + channel + " " + key;
        addCommand("JOIN " + to);

    }

}
