package Connection;

import java.util.EventListener;


public interface PrivateMessagingListener extends EventListener {

    public String getSenderNick();
    public void privateMessageReceived(String sender, String message);

}
