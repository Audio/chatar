package Client;

import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.event.*;


public class TabContainer extends JTabbedPane {

    public enum PanelTypes {
        PANEL_SERVER, PANEL_CHANNEL, PANEL_PRIVATE
    }


    public TabContainer(int width, int height) {

        GUI.setPreferredSize(this, width, height);
        setTabPlacement(JTabbedPane.BOTTOM);

        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                AbstractTab tab = (AbstractTab) getSelectedComponent();
                if (tab != null)
                    tab.setFocus();
            }
        });

    }

    public ServerTab createServerTab(String address) {
        ServerTab tab = new ServerTab(address);
        insertTab(tab, PanelTypes.PANEL_SERVER);
        tab.setFocus();
        MainWindow.getInstance().getGMenuBar().toggleClosePanel(true);
        return tab;
    }

    public void removeTab(AbstractTab tab) {
        remove(tab);
    }

    public void insertTab(AbstractTab tab, PanelTypes type) {
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
