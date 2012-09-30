package Connection;

import java.util.EventListener;


public interface ServerEventsListener extends EventListener {

    public void messageReceived(String message);

}
