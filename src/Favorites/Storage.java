package Favorites;

import Client.ClientLogger;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.joox.Match;
import org.xml.sax.SAXException;

import static org.joox.JOOX.$;


public class Storage {

    private final static String FILENAME = "favorites.xml";
    private final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private Match rootElement;


    public Storage() {
        parse();
    }

    private void parse() {
        try {
            rootElement = $( new File(FILENAME) );
        } catch (SAXException | IOException e) {
            ClientLogger.log("Soubor se seznamem oblíbených serverů neexistuje.",
                                                            ClientLogger.ERROR);
            rootElement = createRootElement();
        }
    }

    private Match createRootElement() {
        return $("<servers />");
    }

    public List<Server> load() {
        List<Server> list = new ArrayList<>();
        Match servers = rootElement.children();

        for ( Match server : servers.each() ) {
            Server s = new Server();
            List<Match> attributes = server.children().each();
            for (Match attr : attributes)
                s.set(attr.tag(), attr.text() );

            list.add(s);
        }
 
        return list;
    }

    public boolean store(List<Server> servers) {
        rootElement = createRootElement();

        for (Server server : servers) {
            rootElement.append( $("<server></server>") );
            Match element = rootElement.find("server").last();

            for (Map.Entry<String, String> entry : server.getAll().entrySet()) {
                Match attr = $("<" + entry.getKey() + " />", entry.getValue() );
                element.append(attr);
            }
        }

        String endline = System.getProperty("line.separator");
        String content = XML_HEADER + endline + rootElement;
        Path path = FileSystems.getDefault().getPath(".", FILENAME);
        try {
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE,
                                                  StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

}
