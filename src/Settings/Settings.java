package Settings;

import Client.ClientLogger;
import Client.HTML;
import Command.Command;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.joox.Match;
import org.xml.sax.SAXException;

import static org.joox.JOOX.$;


public class Settings {

    private static Settings instance;

    private final static String FILENAME = "settings.xml";
    private final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private Match rootElement;
    private ArrayList<SettingsChangesListener> listeners;


    public static Settings getInstance() {
        if (instance == null)
            instance = new Settings();

        return instance;
    }

    private Settings() {
        try {
            this.listeners = new ArrayList<>();
            this.rootElement = $( new File(FILENAME) );
        } catch (SAXException | IOException e) {
            ClientLogger.log("Soubor s nastavením neexistuje.", ClientLogger.ERROR);
        }
    }

    public void addChangesListener(SettingsChangesListener listener) {
        listeners.add(listener);
    }

    public void removeChangesListener(SettingsChangesListener listener) {
        listeners.remove(listener);
    }

    public String getUserProperty(String property) {
        return rootElement.xpath("user/" + property).text();
    }

    public void setUserProperty(String property, String value) {
        rootElement.xpath("user/" + property).text(value);
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

    private List<String> getBlockedNicknamesList(String nicknames) {
        String[] blocked = nicknames.split(",\\s");
        List<String> names = Arrays.asList(blocked);
        return names;
    }

    public List<String> getBlockedNicknamesList() {
        return getBlockedNicknamesList( getBlockedNicknames() );
    }

    public String getBlockedNicknames() {
        return rootElement.xpath("blocked/nicknames").text();
    }

    public void setBlockedNicknames(String nicknames) {
        List<String> names = getBlockedNicknamesList(nicknames);
        String validatedNames = Arrays.toString( names.toArray() ).replaceAll("\\[|\\]", "");
        rootElement.xpath("blocked/nicknames").text(validatedNames);
    }

    public List<Command> getCommands() {
        List<Command> list = new ArrayList<>();
        Match commands = rootElement.xpath("commands").children();
        for ( Match command : commands.each() ) {
            String name = command.attr("name");
            String content = command.text();
            list.add( new Command(name, content) );
        }
        return list;
    }

    public Command getCommand(String name) {
        name = name.toLowerCase();
        Match match = rootElement.xpath("commands/command[@name='" + name + "']");
        if ( match.isEmpty() )
            return null;
        else
            return new Command(name, match.text() );
    }

    public void setCommands(List<Command> commands) {
        rootElement.find("commands command").remove();
        Match container = rootElement.find("commands");
        container.text("");

        for (Command c : commands) {
            container.append( $("<command></command>") );
            Match element = container.find("command").last();

            element.attr("name", c.name);
            element.text(c.content);
        }
    }

    public void store() {
        String content = HTML.formatXML(XML_HEADER + rootElement);
        Path path = FileSystems.getDefault().getPath(".", FILENAME);
        try {
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE,
                                                  StandardOpenOption.TRUNCATE_EXISTING);
            emitChanges();
        } catch (IOException ex) {
            ClientLogger.log("Uložení souboru s nastavením se nezdařilo.", ClientLogger.ERROR);
        }
    }

    private void emitChanges() {
        for (SettingsChangesListener listener : listeners) {
            listener.chatLoggingChanged( isEventEnabled("log-chat") );
            listener.topicVisibilityChanged( isViewEnabled("display-topic") );
            String format = isViewEnabled("timestamp-enabled") ? getViewTimestampFormat() : "";
            listener.timestampFormatChanged(format);
        }
    }

}
