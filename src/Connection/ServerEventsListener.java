package Connection;


public interface ServerEventsListener extends GlobalEventsListener {

    public void connected();
    public void connectionCantBeEstabilished(String reason);
    public void serverMessageReceived(int code, String message);
    public void noticeMessageReceived(String sender, String message);
    public void privateMessageWithoutListenerReceived(String sender, String message);
    public void joined(String channel);
    public void channelCountReceived(String count);
    public void myNickHasChanged(String newNickname);

}
