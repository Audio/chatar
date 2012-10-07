package Connection;

import Config.Config;
import java.io.IOException;
import java.util.ArrayList;
import org.jibble.pircbot.*;


// TODO threading
public class Connection extends PircBot {

    // TODO public config jo? bere se z nej nickname asi
    // TODO config tu nema co delat bych rek
    public Config config;

    private ServerEventsListener serverEventsListener;
    private ArrayList<ChannelEventsListener> channelEventsListeners;
    private ArrayList<PrivateMessagingListener> privateMessagingListeners;


    // TODO vyresit config
    public Connection() throws IOException, IrcException {
        this.config = new Config();
        this.channelEventsListeners = new ArrayList<>();
        this.privateMessagingListeners = new ArrayList<>();

        setName("pokusnyKrecek");
        setAutoNickChange(true);
    }

    // TODO odezva
    /*
    throw new ConnectionException("Adresa serveru není vyplněna.");
    output( Output.HTML.mType("info") +  "Přihlašuji se s přezdívkou " + config.nickname + ".");
    GUI.getInput().setNickname(config.nickname);
    ClientLogger.log("Nelze se připojit: " + e.getMessage(), ClientLogger.ERROR);
    */

    // TODO pri send:
    /*
    if ( !isConnected() )
        throw new ConnectionException("Klient není připojen k serveru.");
    */


    /*         SERVER EVENTS           */

    public void setServerEventsListener(ServerEventsListener listener) {
        serverEventsListener = listener;
    }

    public void removeServerEventsListener() {
        serverEventsListener = null;
    }

    @Override
    protected void onServerResponse(int code, String response) {
        if (serverEventsListener != null)
            serverEventsListener.serverMessageReceived(response);
    }

    /*        CHANNEL EVENTS           */

    public void addChannelEventsListener(ChannelEventsListener listener) {
        channelEventsListeners.add(listener);
    }

    public void removeChannelEventsListener(ChannelEventsListener listener) {
        channelEventsListeners.remove(listener);
    }

    private ChannelEventsListener getChannelEventsListener(String name) {
        for (ChannelEventsListener listener : channelEventsListeners) {
            if ( listener.getChannelName().equals(name) )
                return listener;
        }

        return null;
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.messageReceived(sender, message);
    }

    @Override
    protected void onUserList(String channel, User[] users) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userListReceived(users);
    }

    public void notifyAboutUserGetsOp(String channel, String initiator, String recipient) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userGetsOp(initiator, recipient);
    }

    public void notifyAboutUserLoseOp(String channel, String initiator, String recipient) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userLoseOp(initiator, recipient);
    }

    public void notifyAboutUserGetsVoice(String channel, String initiator, String recipient) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userGetsVoice(initiator, recipient);
    }

    public void notifyAboutUserLoseVoice(String channel, String initiator, String recipient) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userLoseVoice(initiator, recipient);
    }

    public void notifyAboutUserChangesNick(String channel, String oldNick, String newNick) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userChangesNick(oldNick, newNick);
    }

    public void notifyAboutUserJoined(String channel, String nickname) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userJoined(nickname);
    }

    public void notifyAboutUserLeft(String channel, String nickname) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userLeft(nickname);
    }

    public void notifyAboutUserQuit(String nickname, String reason) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userQuit(nickname, reason);
    }

    public void notifyAboutUserKicked(String channel, String initiator, String recipient) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userKicked(initiator, recipient);
    }

    public void notifyAboutUserBanned(String channel, String initiator, String recipient) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userBanned(initiator, recipient);
    }

    public void notifyAboutUserUnbanned(String channel, String initiator, String recipient) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userUnbanned(initiator, recipient);
    }

    @Override
    protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
        for (ChannelEventsListener listener : channelEventsListeners) {
            if ( listener.getChannelName().equals(channel) ) {
                listener.topicChanged(setBy, topic);
                break;
            }
        }
    }

    /*    PRIVATE MESSAGING EVENTS     */

    public void addPrivateMessagingListener(PrivateMessagingListener listener) {
        privateMessagingListeners.add(listener);
    }

    public void removePrivateMessagingListener(PrivateMessagingListener listener) {
        privateMessagingListeners.remove(listener);
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        for (PrivateMessagingListener listener : privateMessagingListeners) {
            if ( listener.getSenderNick().equals(sender) ) {
                listener.privateMessageReceived(sender, message);
                return;
            }
        }

        if (serverEventsListener != null)
            serverEventsListener.privateMessageWithoutListenerReceived(sender, message);
    }

}
