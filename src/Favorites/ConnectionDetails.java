package Favorites;


public class ConnectionDetails {

    public static final int DEFAULT_PORT = 6667;
    public static final String DEFAULT_NICK = "ChatarUser";

    public String address;
    public int port;
    public String nickname;


    private ConnectionDetails() {
        this.address = "";
        this.port = DEFAULT_PORT;
        this.nickname = DEFAULT_NICK;
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

        return cd;
    }

    public boolean isValid() {
        return !address.isEmpty();
    }

}
