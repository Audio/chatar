package MainWindow;

import Client.GUI;
import Favorites.ConnectionDetails;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.event.*;


public class TabContainer extends JTabbedPane {

    static final long serialVersionUID = 1L;


    public TabContainer(int width, int height) {
        super(JTabbedPane.BOTTOM);
        setBorder( BorderFactory.createEmptyBorder(-2, -1, 1, -3) );
        GUI.setPreferredSize(this, width, height);

        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                AbstractTab tab = (AbstractTab) getSelectedComponent();
                if (tab != null)
                    tab.setFocus();
            }
        });
    }

    public ServerTab createServerTab(ConnectionDetails address) {
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
            index = indexOfComponent(s);

            while ( ++index < getTabCount() ) {
                Component c = getComponentAt(index);
                if (c instanceof ServerTab)
                    break;
            }

            tip = "Server " + s.getTabName() + ", ";
            tip += (tab instanceof ChannelTab) ? "kanál" : "uživatel";
            tip += " " + title;
        }

        insertTab(title, null, tab, tip, index);
    }

}
