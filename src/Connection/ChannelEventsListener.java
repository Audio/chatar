package Connection;

import Client.User;
import java.util.EventListener;


public interface ChannelEventsListener extends EventListener {

    public String getChannelName();
    public boolean contains(String nickname);

    public void messageReceived(String sender, String message);
    public void userListReceived(User[] users);

    // TODO Half OP, owner a bot?
    public void userGetsOp(String initiator, String recipient);
    public void userLoseOp(String initiator, String recipient);
    public void userGetsVoice(String initiator, String recipient);
    public void userLoseVoice(String initiator, String recipient);

    public void userChangesNick(String oldNick, String newNick);

    public void userJoined(String nickname);
    public void userLeft(String nickname);
    public void userQuit(String nickname, String reason);
    public void userKicked(String initiator, String recipient, String reason);
    public void userBanned(String initiator, String recipient);
    public void userUnbanned(String initiator, String recipient);

    public void topicChanged(String initiator, String topic);

}
