package Client;


public class DeprecatedClientException extends Exception {

    public DeprecatedClientException(String str) {
        super(str);
        ClientLogger.log("ClientException: " + str, ClientLogger.ERROR);
    }

    public DeprecatedClientException(Throwable obj) {
        super(obj);
        ClientLogger.log("ClientException: " + obj.toString(), ClientLogger.ERROR);
    }

}
