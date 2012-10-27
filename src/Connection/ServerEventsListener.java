package Connection;

import java.util.EventListener;


public interface ServerEventsListener extends EventListener {

    public void connected();
    public void connectionCantBeEstabilished(String reason);
    public void serverMessageReceived(String message);
    public void privateMessageWithoutListenerReceived(String sender, String message);

}
