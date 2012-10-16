package Client;

import javax.swing.JPanel;


public abstract class AbstractTab extends JPanel {

    protected ServerTab serverTab;
    protected String tabName;


    final public String getTabName() {
        return tabName;
    }

    public ServerTab getServerTab() {
        return serverTab;
    }

    // TODO append nebo display
    public abstract void addText(String str);

    public abstract void clearContent();

    public void setFocus() {
        refreshNickname();
        GUI.getTabContainer().setSelectedComponent(this);
        GUI.getMenuBar().toggleDisconectFromServer(true);
        GUI.focusInput();
    }

    public void refreshNickname() {
        String nick = getServerTab().getConnection().getNick();
        MainWindow.getInstance().getGInput().setNickname(nick);
    }

}
