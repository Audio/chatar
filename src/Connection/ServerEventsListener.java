package Connection;

import java.util.EventListener;


public interface ServerEventsListener extends EventListener {

    public void serverMessageReceived(String message);

}
