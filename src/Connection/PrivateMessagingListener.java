package Connection;

import java.util.EventListener;


public interface PrivateMessagingListener extends EventListener {

    public String getNickname();
    public void privateMessageReceived(String sender, String message);
    public void userChangesNick(String newNick);

}
