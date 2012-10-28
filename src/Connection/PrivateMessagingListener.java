package Connection;


public interface PrivateMessagingListener extends GlobalEventsListener {

    public String getNickname();
    public void privateMessageReceived(String message);
    public void userChangesNick(String newNick);
    public void userIsAway(String reason);

    public void whoisUser(String userInfo);
    public void whoisServer(String serverInfo);
    public void whoisChannels(String channelList);
    public void whoisIdle(String seconds);

}
