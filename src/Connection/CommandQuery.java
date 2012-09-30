package Connection;

import Client.ClientLogger;
import Config.Config;
import java.util.Stack;

/**
 * Třída CommandQuery slouží k odesílání příkazů serveru. Veškeré příkazy
 * a odpovědi klienta procházejí přes instanci této třídy. Každá instance
 * je přiřazena k instanci spojení se serverem (třída Connection).
 *
 * @author Martin Fouček
 */
public class CommandQuery {

    private Stack<String> commands;
    private Connection connection;
    private boolean busy;
    private final char SOH = ''; // znak Start of Heading

    public CommandQuery(Connection connection) {

        this.connection = connection;
        this.setBusy(false);
        this.commands = new Stack<String>();

    }

    /**
     * Tunel na nastavovani konfigurace pripojeni.
     *
     * @return
     */
    public Config getConfig () {
        return connection.config;
    }

    /**
     * Nastavuje priznak, zda je objekt zaneprazdneny.
     *
     * Je-li objekt zaneprazdneny, neodesila na server dalsi prikazy.
     * Prikaz misto toho zaradi do fronty a odesle ho v momente,
     * kdy je zrusen tento priznak.
     */
    public void setBusy () {
        setBusy(true);
    }

    /**
     * Nastavuje / rusi priznak, zda je objekt zaneprazdneny.
     *
     * Je-li objekt zaneprazdneny, neodesila na server dalsi prikazy.
     * Prikaz misto toho zaradi do fronty a odesle ho v momente,
     * kdy je zrusen tento priznak.
     *
     * @param busy
     */
    public void setBusy (boolean busy) {
        this.busy = busy;
    }

    /**
     * Vraci informaci, zda je objekt zaneprazdneny.
     *
     * @return
     */
    public boolean isBusy () {
        return busy;
    }

    /**
     * Rusi priznak zaneprazdneni a dava objektu pokyn k dalsi cinnosti.
     */
    public void goOn () {
        setBusy(false);
        execute();
    }

    /**
     * Pridani prikazu do fronty prikazu a pokyn k jeho vykonani.
     *
     * @param command
     */
    public void addCommand (String command) {
        commands.push(command);
        execute();
    }

    /**
     * Vykonání příkazu z fronty příkazů.
     *
     * Je-li objekt zaneprázdněn, příkaz je vykonán později.
     */
    private void execute () {

        if ( commands.size() == 0 )
            return;

        if ( isBusy() )
            return;

        String command = commands.pop();

        // Maximální délka odeslané zprávy je 510 znaků (RFC 1459).
        if ( command.length() > 510 )
            command = command.substring(0, 509);

        try {
            connection.send(command);
        }
        catch (Exception e) {
            String msg = "Chyba vykonávání příkazu " + command + ": "
                       + e.getMessage();
            ClientLogger.log(msg, ClientLogger.ERROR);
        }

    }

    /**
     * Prve pripojeni na server.
     */
    public void login () {
        addCommand("PASS " + getConfig().password + "\r\n"
                      + "NICK " + getConfig().nickname + "\r\n"
                      + "USER " + getConfig().username + " " + getConfig().hostname
                      + " " + getConfig().servername +  " :" + getConfig().realname);
    }

    /**
     * Odpojeni od serveru. Nevyzaduje duvod ukonceni spojeni.
     */
    public void disconnect () {
        disconnect(null);
    }

    /**
     * Odpojeni od serveru. Vyzaduje duvod ukonceni spojeni.
     *
     * @param reason
     */
    public void disconnect (String reason) {

        if (reason != null)
            addCommand("QUIT :" + reason);
        else
            addCommand("QUIT");

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

    /**
     * Odchod z kanalu.
     *
     * @param channel
     */
    public void leave (String channel) {
        addCommand("PART #" + channel);
    }

    /**
     * Ziskani aktualniho tematu.
     *
     * @param channel
     */
    public void topic (String channel) {
        topic(channel, null);
    }

    /**
     * Zmena aktualniho tematu.
     *
     * @param channel
     * @param topic
     */
    public void topic (String channel, String topic) {

        String to;
        if (topic == null)
            to = "#" + channel;
        else
            to = "#" + channel + " :" + topic;
        addCommand("TOPIC " + to);

    }

    /**
     * Odezva uzivateli / serveru.
     *
     * @param target
     */
    public void pong (String target) {
        addCommand("PONG " + target);
    }

    /**
     * Odeslani zpravy.
     * Adresatem je budto uzivatel, nebo kanal.
     *
     * @param target 
     * @param msg
     */
    public void privMsg (String target, String msg) {
        addCommand("PRIVMSG " + target + " " + msg);
    }

    /**
     * Zmena prezdivky.
     *
     * @param nick
     */
    public void nick (String nick) {
        addCommand("NICK " + nick);
    }

    /**
     * Vypis uzivatelu na kanale(ch).
     *
     * @param channels
     */
    public void names (String channels) {

        if (channels == null || channels.trim().length() == 0)
            addCommand("NAMES");
        else
            addCommand("NAMES " + channels);

    }

    /**
     * Nastaveni Modu kanalu/uzivatele.
     *
     * @param params
     */
    public void mode (String params) {
        addCommand("MODE " + params);
    }

    /**
     * Vyhozeni uzivatele z daneho kanalu.
     * Muze obsahovat komentar (duvod vyhozeni).
     *
     * @param params
     */
    public void kick (String params) {
        addCommand("KICK " + params);
    }

    /**
     * Nastaveni/zruseni stavu nepritomnosti (AFK).
     *
     * @param reason
     */
    public void away (String reason) {

        if (reason == null)
            addCommand("AWAY");
        else
            addCommand("AWAY :" + reason);

    }

    /**
     * Získání informací o uživateli s daným nickem.
     *
     * @param who
     */
    public void whois (String who) {
        addCommand("WHOIS " + who);
    }

    /**
     * Odesílá oznámení, že uživatel provedl akci spefikovanou parametrem.
     * Akce je textovy rezetec, ktery uzivatel zada - tzn. libovolny.
     *
     * @param channel
     * @param action
     */
    public void me (String channel, String action) {
        privMsg(channel, ":" + SOH + "ACTION " + action + SOH);
    }

}
