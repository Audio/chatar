package Client;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Panel se záložkami tří typu (server, channel, private chat).
 *
 * @author Martin Fouček
 */
public class GTab extends JTabbedPane {

    // typy vlozitelnych panelu
    /**
     * Typ vložitelného panelu - SERVER.
     */
    final public static int PANEL_SERVER  = 50;
    /**
     * Typ vložitelného panelu - KANÁL.
     */
    final public static int PANEL_CHANNEL = 51;
    /**
     * Typ vložitelného panelu - SOUKROMÝ CHAT.
     */
    final public static int PANEL_PRIVATE = 52;

    /**
     * Konstruktor. Nastavuje velikost své komponenty.
     * Vytváři objekt posluchače události - při prepnutí na jinou záložku.
     *
     * @param width
     * @param height
     */
    public GTab (int width, int height) {

        GUI.setMySize(this, width, height);
        setTabPlacement(JTabbedPane.BOTTOM);

        addChangeListener( new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                GTabWindow ref = (GTabWindow) GUI.getTab().getSelectedComponent();
                if (ref == null)
                    return;
                ref.setFocus();
            }
        } );

    }

    /**
     * Továrna na výrobu panelů :) (záložek - tabs).
     *
     * @param type
     * @param address
     * @throws Client.ClientException
     */
    public void addTab (int type, String address) throws ClientException {

        GTabWindow new_tab = null;

        switch (type) {
            case PANEL_SERVER:  { new_tab = new GTabServer(address); break; }
            case PANEL_CHANNEL: { new_tab = new GTabChannel(address); break; }
            case PANEL_PRIVATE: { new_tab = new GTabPrivateChat(address); break; }
            default: { throw new ClientException("Snaha o přidání neexistujícího typu panelu."); }
        }

        new_tab.adapt(address);
        insertNewTab(new_tab, type);
        new_tab.setFocus();
        GUI.getMenuBar().toggleClosePanel(true);

    }

    /**
     * Odstranění zvolené komponenty (panelu).
     *
     * @param c
     */
    public void removeTab (Component c) {
        remove(c);
    }

    /**
     * Vkládá komponenty v pořadí: server, jeho mistnosti, dalsi server..
     * Serverový panel přidává na konec, ostatní panely přidává za serverový
     * (za ten, přes který komunikují).
     *
     * @param new_tab
     * @param type
     */
    private void insertNewTab (GTabWindow new_tab, int type) {

        int index = 0;
        String tip = null;
        String title = new_tab.getTabName();

        if (type == PANEL_SERVER) {
            index = getTabCount();
            tip = "Server " + title;
        }
        else {
            GTabServer s = new_tab.getServer();
            index = indexOfComponent(s) + 1;
            tip = "Server " + s.getTabName() + ", ";
            tip += (type == PANEL_CHANNEL) ? "kanál" : "uživatel";
            tip += " " + title;
        }

        insertTab(title, null, new_tab, tip, index);

        // Panel serveru bude označen (pro přehlednost)
        if (type == PANEL_SERVER)
            setBackgroundAt(index, new Color(238, 232, 170) );

    }

}
