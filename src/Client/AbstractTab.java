package Client;

import Connection.Connection;
import javax.swing.JPanel;


public abstract class AbstractTab extends JPanel {

    protected Connection connection;
    protected ServerTab serverTab;
    protected String tabName;


    public Connection getConnection() {
        return connection;
    }

    final public String getTabName() {
        return tabName;
    }

    public ServerTab getServerTab() {
        return serverTab;
    }

    // TODO append nebo display
    public abstract void addText(String str);

    public abstract void clearContent();

    public abstract void setFocus();

    // TODO volat jen pri zmene serveru (focus na servertab)
    public void refreshNickname() {
        String nick = getConnection().getNick();
        MainWindow.getInstance().getGInput().setNickname(nick);
    }

    /**
     * Odstraní se z tabbed panelu.
     */
    // TODO ehm, finalize?
    public void destroy() {
        try {
            MainWindow.getInstance().removeTab(this);
            finalize();
        }
        catch (Exception e) { }
        catch (Throwable t) { }
    }

}
