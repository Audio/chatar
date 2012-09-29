package Client;


public class ClientException extends Exception {

    public ClientException(String str) {
        super(str);
        ClientLogger.log("ClientException: " + str, ClientLogger.ERROR);
    }

    public ClientException(Throwable obj) {
        super(obj);
        ClientLogger.log("ClientException: " + obj.toString(), ClientLogger.ERROR);
    }

}
