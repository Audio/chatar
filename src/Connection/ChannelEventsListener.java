package Connection;

import MainWindow.User;


public interface ChannelEventsListener extends GlobalEventsListener {

    public String getChannelName();
    public boolean contains(String nickname);

    public void messageReceived(String sender, String message);
    public void userListReceived(User[] users);

    public void userModeGranted(String initiator, String recipient, String mode);
    public void userModeRevoked(String initiator, String recipient, String mode);

    public void userChangesNick(String oldNick, String newNick);

    public void userJoined(String nickname);
    public void userLeft(String nickname);
    public void userQuit(String nickname, String reason);
    public void userKicked(String initiator, String recipient, String reason);
    public void userBanned(String initiator, String recipient);
    public void userUnbanned(String initiator, String recipient);

    public void topicChanged(String initiator, String topic);

}
