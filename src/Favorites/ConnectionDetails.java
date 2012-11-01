package Favorites;


public class ConnectionDetails {

    public static final int DEFAULT_PORT = 6667;

    public String address;
    public int port;


    public ConnectionDetails(String fullAddress) {
        if (fullAddress == null) {
            this.address = "";
            this.port = DEFAULT_PORT;
        } else if ( fullAddress.matches(".+:\\d{3,}$") ) {
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
