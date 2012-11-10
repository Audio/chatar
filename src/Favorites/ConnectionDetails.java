package Favorites;

import Settings.Settings;
import java.util.*;


public class ConnectionDetails {

    public static final int DEFAULT_PORT = 6667;

    public String address;
    public int port;
    public String nickname;
    public String username;
    public List<String> channelsToJoin;
    // TODO password


    private ConnectionDetails() {
        Settings settings = Settings.getInstance();

        this.address = "";
        this.port = DEFAULT_PORT;
        this.nickname = settings.getUserProperty("nickname");
        this.username = settings.getUserProperty("username");
        this.channelsToJoin = new ArrayList<>();
    }

    public static ConnectionDetails fromAddress(String fullAddress) {
        ConnectionDetails cd = new ConnectionDetails();

        if (fullAddress != null) {
            if ( fullAddress.matches(".+:\\d{3,}$") ) {
                int upto = fullAddress.lastIndexOf(":");
                cd.address = fullAddress.substring(0, upto);
                cd.port = Integer.parseInt( fullAddress.substring(upto + 1) );
            } else {
                cd.address = fullAddress;
            }
        }

        return cd;
    }

    public static ConnectionDetails fromServer(Server server) {
        ConnectionDetails cd = new ConnectionDetails();
        cd.address = server.get("address");

        String port = server.get("port");
        if ( !port.isEmpty() )
            cd.port = Integer.parseInt(port);

        String nick = server.get("nickname");
        if ( !nick.isEmpty() )
            cd.nickname = nick;

        String autoChans = server.get("channels");
        if ( !autoChans.isEmpty() )
            cd.addChannelList(autoChans);

        return cd;
    }

    private void addChannelList(String channels) {
        String[] c = channels.split(",?\\s+");
        Collections.addAll(channelsToJoin, c);
        Collections.sort(channelsToJoin);
    }

    public boolean isValid() {
        return !address.isEmpty();
    }

}
