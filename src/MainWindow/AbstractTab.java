package MainWindow;

import Client.HTML;
import Connection.GlobalEventsListener;
import Settings.Settings;
import java.awt.Desktop;
import java.awt.Font;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;


public abstract class AbstractTab extends JPanel implements GlobalEventsListener {

    protected static String timestampFormat;

    protected ServerTab serverTab;
    protected String tabName;
    protected JEditorPane content;


    public AbstractTab() {
        createContent();

        timestampFormat = "";
        if ( Settings.getInstance().isViewEnabled("timestamp-enabled") )
            timestampFormat = Settings.getInstance().getViewTimestampFormat();
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

        content.addHyperlinkListener( new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED
                    && Desktop.isDesktopSupported() ) {
                    try {
                        Desktop.getDesktop().browse( e.getURL().toURI() );
                    } catch (URISyntaxException | IOException ex) { /* not supported */ }
                }
            }
        });
    }

    public void appendText(String str) {
        appendText(str, content);
    }

    public void appendText(String str, JEditorPane panel) {
        if ( Settings.getInstance().isEventEnabled("clickable-links") )
            str = HTML.addHyperlinks(str);

        str = prependTimestamp(str);

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

    private String prependTimestamp(String str) {
        if ( str.isEmpty() ) {
            return str;
        } else {
            Calendar c = Calendar.getInstance();
            String timestamp = timestampFormat;
            timestamp = timestamp.replaceFirst("h", getTwoDigits( c.get(Calendar.HOUR_OF_DAY) ) );
            timestamp = timestamp.replaceFirst("m", getTwoDigits( c.get(Calendar.MINUTE) ) );
            timestamp = timestamp.replaceFirst("s", getTwoDigits( c.get(Calendar.SECOND) ) );
            return HTML.small(timestamp) + " " + str;
        }
    }

    private String getTwoDigits(int num) {
        String str = Integer.toString(num);
        return num < 10 ? "0" + str : str;
    }

    public void setFocus() {
        MainWindow.getInstance().getTabContainer().setSelectedComponent(this);
        MainWindow.getInstance().getMainMenu().toggleConnectionActions(true);
        MainWindow.getInstance().setTitle( getTabName() + " - Chatař" );
        MainWindow.getInstance().getNickButton().refreshNickname();
        MainWindow.getInstance().getInputField().requestFocus();
    }

    @Override
    public void awayStatusChanged(boolean isAway) {
        if (isAway)
            appendInfo("Nastaven stav nepřítomnosti");
        else
            appendInfo("Zrušen stav nepřítomnosti");
    }

}
