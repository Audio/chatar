package MainWindow;

import Client.GUI;
import Favorites.ServerAddress;
import javax.swing.JTabbedPane;
import javax.swing.event.*;


public class TabContainer extends JTabbedPane {

    static final long serialVersionUID = 1L;


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

    public ServerTab createServerTab(ServerAddress address) {
        ServerTab tab = new ServerTab(address);
        insertTab(tab);
        tab.setFocus();
        MainWindow.getInstance().getMainMenu().togglePanelActions(true);
        return tab;
    }

    public void removeTab(AbstractTab tab) {
        remove(tab);

        if ( getComponentCount() == 0 )
            MainWindow.getInstance().resetTitle();
    }

    public void insertTab(AbstractTab tab) {
        int index;
        String tip ;
        String title = tab.getTabName();

        if (tab instanceof ServerTab) {
            index = getTabCount();
            tip = "Server " + title;
        } else {
            ServerTab s = tab.getServerTab();
            index = indexOfComponent(s) + 1;
            tip = "Server " + s.getTabName() + ", ";
            tip += (tab instanceof ChannelTab) ? "kanál" : "uživatel";
            tip += " " + title;
        }

        insertTab(title, null, tab, tip, index);
    }

}