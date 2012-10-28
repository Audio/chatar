package Client;

import Connection.GlobalEventsListener;
import java.awt.Font;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;


public abstract class AbstractTab extends JPanel implements GlobalEventsListener {

    protected ServerTab serverTab;
    protected String tabName;
    protected JEditorPane content;


    public AbstractTab() {
        createContent();
    }

    final public String getTabName() {
        return tabName;
    }

    public ServerTab getServerTab() {
        return serverTab;
    }

    private void createContent() {
        content = new JEditorPane();
        content.setContentType("text/html");
        content.setEditable(false);

        Font font = UIManager.getFont("Label.font");
        String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: 13pt; }";
        ((HTMLDocument) content.getDocument()).getStyleSheet().addRule(bodyRule);
    }

    public void appendText(String str) {
        appendText(str, content);
    }

    public void appendText(String str, JEditorPane panel) {
        EditorKit kit = panel.getEditorKit();
        Document doc = panel.getDocument();
        try {
            Reader reader = new StringReader(str);
            kit.read(reader, doc, doc.getLength());
            panel.setCaretPosition( doc.getLength() );
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void appendInfo(String str) {
        String prefix = HTML.small("info");
        str = HTML.italic(str);
        appendText(prefix + str);
    }

    public void appendError(String str) {
        String prefix = HTML.red( HTML.small("chyba") );
        str = HTML.italic(str);
        appendText(prefix + str);
    }

    public void appendNotice(String str) {
        str = HTML.blue( HTML.italic(str) );
        appendText(str);
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

    @Override
    public void awayStatusChanged(boolean isAway) {
        if (isAway)
            appendInfo("Nastaven stav nepřítomnosti");
        else
            appendInfo("Zrušen stav nepřítomnosti");
    }

}
