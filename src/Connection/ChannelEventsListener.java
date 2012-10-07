package Connection;

import java.util.EventListener;


public interface ChannelEventsListener extends EventListener {

    public void messageReceived(String sender, String message);

    // TODO Half OP?
    public void userGetsOp(String initiator, String recipient);
    public void userLoseOp(String initiator, String recipient);
    public void userGetsVoice(String initiator, String recipient);
    public void userLoseVoice(String initiator, String recipient);

    public void userChangesNick(String oldNick, String newNick);

    public void userJoined(String nickname);
    public void userLeft(String nickname);
    public void userQuit(String nickname, String reason);
    public void userKicked(String initiator, String recipient);
    public void userBanned(String initiator, String recipient);
    public void userUnbanned(String initiator, String recipient);

    public void topicChanged(String initiator, String topic);

    // TODO onUserList

}
