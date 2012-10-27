package Client;

import java.io.*;
import javax.swing.*;
import javax.swing.text.*;


public abstract class AbstractTab extends JPanel {

    protected ServerTab serverTab;
    protected String tabName;
    protected JEditorPane content;


    final public String getTabName() {
        return tabName;
    }

    public ServerTab getServerTab() {
        return serverTab;
    }

    public void appendText(String str) {
        EditorKit kit = content.getEditorKit();
        Document doc = content.getDocument();
        try {
            Reader reader = new StringReader(str);
            kit.read(reader, doc, doc.getLength());
            content.setCaretPosition( doc.getLength() );
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void appendInfo(String str) {
        String prefix = HTML.mType("info");
        appendText(prefix + str);
    }

    public void appendError(String str) {
        String prefix = HTML.red( HTML.mType("error") );
        appendText(prefix + str);
    }

    public void clearContent() {
        content.setText(null);
    }

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
