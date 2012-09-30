package Client;

import Connection.CommandQuery;
import Connection.Connection;
import javax.swing.JPanel;

/**
 * Abstraktní třída GTabWindow představuje předka tří používaných panelů
 * v tabbed panelu (serverový panel, kanál a soukromý chat).
 *
 * @author Martin Fouček
 */
public abstract class GTabWindow extends JPanel {

    /**
     * Vrací referenci na objekt Connection,
     * přes který komunikuje.
     *
     * @return
     */
    public Connection getConnection () {
        return null;
    }

    /**
     * U serverových panelů vytváří spojení.
     * U kanalů přiřadí svoji referenci k serverovému panelu.
     * @param address
     */
    public abstract void adapt (String address);

    /**
     * Defaultní název mistnosti, pokud si ho panel nenastaví sám.
     *
     * @return
     */
    public String getTabName () {
        return "Panel";
    }

    /**
     * Vrací odkaz na spojení.
     * Rozdílna implementace u serverových panelů a ostatních.
     *
     * @return
     */
    public CommandQuery getQuery () {
        return null;
    }

    /**
     * Vrací referenci na server, přes který je kanál napojen.
     * V případě serverové místnosti vrací ref. na sebe.
     *
     * @return
     */
    public ServerTab getServer () {
        return null;
    }

    /**
     * Přidává text do svého panelu.
     *
     * @param str 
     */
    public abstract void addText(String str);

    /**
     * Určí sama sebe jako aktualní panel.
     * Použito mj. pri zpracování vstupu od uživatele.
     */
    public abstract void setFocus ();

    /**
     * Zmeni uzivatelovu prezdivku na tlacitku (GInput).
     * Uzivalel ma moznost pripojeni k vice serverum zaroven,
     * proto bude mit v jeden okamzik prirazeno vice prezdivek.
     */
    public void changeNickname () {
        String nick = getConnection().config.nickname;
        GUI.getInput().setNickname(nick);
    }

    /**
     * Ukončí činnost panelu, ale nezavře ho.
     */
    public void die () {
        die(null);
    }

    /**
     * Ukončí činnost panelu, ale nezavře ho.
     *
     * @param reason
     */
    public abstract void die (String reason);

    /**
     * Odstraní se z tabbed panelu.
     */
    public void killMyself () {

        try {
            GUI.removeTab(this);
            finalize();
        }
        catch (Exception e) { }
        catch (Throwable t) { }

    }

    /**
     * Vymaže obsah panelu (chat, odpovědi ze serveru apod).
     */
    public abstract void clearContent ();

}
