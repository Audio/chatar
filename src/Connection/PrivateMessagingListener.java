package Connection;

import java.util.EventListener;


public interface PrivateMessagingListener extends EventListener {

    public String getNickname();
    public void privateMessageReceived(String message);
    public void userChangesNick(String newNick);

    public void whoisUser(String userInfo);
    public void whoisServer(String serverInfo);
    public void whoisChannels(String channelList);
    public void whoisIdle(String seconds);

}
