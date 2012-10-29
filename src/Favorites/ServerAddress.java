package Favorites;


public class ServerAddress {

    public static final int DEFAULT_PORT = 6667;

    public String address;
    public int port;


    public ServerAddress(String fullAddress) {
        if ( fullAddress.matches(".+:\\d{3,}$") ) {
            int upto = fullAddress.lastIndexOf(":");
            this.address = fullAddress.substring(0, upto);
            this.port = Integer.parseInt( fullAddress.substring(upto + 1) );
        } else {
            this.address = fullAddress;
            this.port = DEFAULT_PORT;
        }
    }

    public boolean isValid() {
        return !address.isEmpty();
    }

}
