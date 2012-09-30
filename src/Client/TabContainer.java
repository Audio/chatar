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

    public enum PanelTypes {
        PANEL_SERVER, PANEL_CHANNEL, PANEL_PRIVATE
    }

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

    public void addTab(PanelTypes type, String address) throws ClientException {
        AbstractTab tab = null;

        switch (type) {
            case PANEL_SERVER:  { tab = new ServerTab(address); break; }
            case PANEL_CHANNEL: { tab = new ChannelTab(address); break; }
            case PANEL_PRIVATE: { tab = new PrivateChatTab(address); break; }
        }

        tab.adapt(address);
        insertNewTab(tab, type);
        tab.setFocus();
        MainWindow.getInstance().getGMenuBar().toggleClosePanel(true);
    }

    public void removeTab(AbstractTab tab) {
        remove(tab);
    }

    private void insertNewTab(AbstractTab tab, PanelTypes type) {
        int index;
        String tip ;
        String title = tab.getTabName();

        if (type == PanelTypes.PANEL_SERVER) {
            index = getTabCount();
            tip = "Server " + title;
        } else {
            ServerTab s = tab.getServerTab();
            index = indexOfComponent(s) + 1;
            tip = "Server " + s.getTabName() + ", ";
            tip += (type == PanelTypes.PANEL_CHANNEL) ? "kanál" : "uživatel";
            tip += " " + title;
        }

        insertTab(title, null, tab, tip, index);

        if (type == PanelTypes.PANEL_SERVER)
            setBackgroundAt(index, new Color(238, 232, 170) );
    }

}
