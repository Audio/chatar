package Client;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Panel se záložkami tří typu (server, channel, private chat).
 */
public class TabContainer extends JTabbedPane {

    final public static int PANEL_SERVER  = 50;
    final public static int PANEL_CHANNEL = 51;
    final public static int PANEL_PRIVATE = 52;

    /**
     * Konstruktor. Nastavuje velikost své komponenty.
     * Vytváři objekt posluchače události - při prepnutí na jinou záložku.
     *
     * @param width
     * @param height
     */
    public TabContainer(int width, int height) {

        GUI.setPreferredSize(this, width, height);
        setTabPlacement(JTabbedPane.BOTTOM);

        addChangeListener( new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                AbstractTab ref = (AbstractTab) GUI.getTabContainer().getSelectedComponent();
                if (ref == null)
                    return;
                ref.setFocus();
            }
        } );

    }

    public void addTab(int type, String address) throws ClientException {
        AbstractTab tab = null;

        switch (type) {
            case PANEL_SERVER:  { tab = new ServerTab(address); break; }
            case PANEL_CHANNEL: { tab = new ChannelTab(address); break; }
            case PANEL_PRIVATE: { tab = new PrivateChatTab(address); break; }
            default: { throw new ClientException("Snaha o přidání neexistujícího typu panelu."); }
        }

        tab.adapt(address);
        insertNewTab(tab, type);
        tab.setFocus();
        MainWindow.getInstance().getGMenuBar().toggleClosePanel(true);
    }

    public void removeTab(AbstractTab tab) {
        remove(tab);
    }

    private void insertNewTab(AbstractTab tab, int type) {
        int index;
        String tip ;
        String title = tab.getTabName();

        if (type == PANEL_SERVER) {
            index = getTabCount();
            tip = "Server " + title;
        } else {
            ServerTab s = tab.getServerTab();
            index = indexOfComponent(s) + 1;
            tip = "Server " + s.getTabName() + ", ";
            tip += (type == PANEL_CHANNEL) ? "kanál" : "uživatel";
            tip += " " + title;
        }

        insertTab(title, null, tab, tip, index);

        if (type == PANEL_SERVER)
            setBackgroundAt(index, new Color(238, 232, 170) );
    }

}
