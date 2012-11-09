package Config;

import Client.ClientLogger;
import Client.HTML;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import org.joox.Match;
import org.xml.sax.SAXException;

import static org.joox.JOOX.$;


public class Config {

    private final static String FILENAME = "settings.xml";
    private final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private Match rootElement;


    public Config () {
        parse();
    }

    private void parse() {
        try {
            rootElement = $( new File(FILENAME) );
        } catch (SAXException | IOException e) {
            ClientLogger.log("Soubor s nastavením neexistuje.", ClientLogger.ERROR);
        }
    }

    public String getUserProperty(String property) {
        return rootElement.xpath("user/" + property).text();
    }

    public void setUserProperty(String property, String value) {
        // TODO fakt set
    }

    public boolean isEventEnabled(String event) {
        return rootElement.xpath("events/" + event).attr("value").equals("true");
    }

    public void setEventEnabled(String event, boolean enabled) {
        String value = enabled ? "true" : "false";
        rootElement.xpath("events/" + event).attr("value", value);
    }

    public boolean isViewEnabled(String view) {
        return rootElement.xpath("view/" + view).attr("value").equals("true");
    }

    public void setViewEnabled(String view, boolean enabled) {
        String value = enabled ? "true" : "false";
        rootElement.xpath("view/" + view).attr("value", value);
    }

    public String getViewTimestampFormat() {
        return rootElement.xpath("view/timestamp-format").text();
    }

    public void setViewTimestampFormat(String format) {
        rootElement.xpath("view/timestamp-format").text(format);
    }

    public List<String> getBlockedNicknames() {
        return null; // TODO fakt get
    }

    public void setBlockedNicknames(List<String> nicknames) {
        // TODO fakt set
    }

    public List<String> getCommands() {
        return null; // TODO fakt get
    }

    public void setCommands(List<String> commands) {
        // TODO fakt set
    }

    public void store() {
        String content = HTML.formatXML(XML_HEADER + rootElement);
        Path path = FileSystems.getDefault().getPath(".", FILENAME);
        try {
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE,
                                                  StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            ClientLogger.log("Uložení souboru s nastavením se nezdařilo.", ClientLogger.ERROR);
        }
    }

}
