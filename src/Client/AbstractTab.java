package Client;

import Connection.DeprecatedConnection;
import javax.swing.JPanel;


public abstract class AbstractTab extends JPanel {

    // TODO pryc
    public DeprecatedConnection getConnection() {
        return null;
    }

    /**
     * U serverových panelů vytváří spojení.
     * U kanalů přiřadí svoji referenci k serverovému panelu.
     * @param address
     */
    // TODO co s tim?
    public abstract void adapt(String address);

    public String getTabName() {
        return "Panel";
    }

    public ServerTab getServerTab() {
        return null;
    }

    // TODO append nebo display
    public abstract void addText(String str);

    public abstract void clearContent();

    public abstract void setFocus();

    /**
     * Zmeni uzivatelovu prezdivku na tlacitku (GInput).
     * Uzivalel ma moznost pripojeni k vice serverum zaroven,
     * proto bude mit v jeden okamzik prirazeno vice prezdivek.
     */
    // TODO nevim
    public void changeNickname() {
        // String nick = getConnection().config.nickname;
        String nick = "zatimNic";
        MainWindow.getInstance().getGInput().setNickname(nick);
    }

    /**
     * Ukončí činnost panelu, ale nezavře ho.
     */
    public void die() {
        die(null);
    }

    public abstract void die(String reason);

    /**
     * Odstraní se z tabbed panelu.
     */
    // TODO ehm, finalize?
    public void killMyself() {
        try {
            MainWindow.getInstance().removeTab(this);
            finalize();
        }
        catch (Exception e) { }
        catch (Throwable t) { }
    }

}
