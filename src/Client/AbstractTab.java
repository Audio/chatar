package Client;

import Connection.Connection;
import javax.swing.JPanel;


public abstract class AbstractTab extends JPanel {

    protected Connection connection;
    protected ServerTab serverTab;


    public Connection getConnection() {
        return connection;
    }

    public String getTabName() {
        return "Panel";
    }

    public ServerTab getServerTab() {
        return serverTab;
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
