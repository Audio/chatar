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

    private ArrayList<ServerEventsListener> serverEventsListeners;
    private ArrayList<ChannelEventsListener> channelEventsListeners;
    private ArrayList<MyNickChangeListener> myNickChangeListeners;


    // TODO vyresit config
    public Connection(String server, int port) throws IOException, IrcException {
        this.config = new Config();
        this.serverEventsListeners = new ArrayList<>();
        this.channelEventsListeners = new ArrayList<>();
        this.myNickChangeListeners = new ArrayList<>();

        setName("pokusnyKrecek");
        setAutoNickChange(true);
        connect(server, port);
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

    public void addServerEventListener(ServerEventsListener listener) {
        serverEventsListeners.add(listener);
    }

    public void removeServerEventListener(ServerEventsListener listener) {
        serverEventsListeners.remove(listener);
    }

    @Override
    protected void onServerResponse(int code, String response) {
        notifyAboutServerMessageReceived(response);
    }

    public void notifyAboutServerMessageReceived(String message) {
        for (ServerEventsListener listener : serverEventsListeners)
            listener.serverMessageReceived(message);
    }


    /*        CHANNEL EVENTS           */

    public void addChannelEventListener(ChannelEventsListener listener) {
        channelEventsListeners.add(listener);
    }

    public void removeChannelEventListener(ChannelEventsListener listener) {
        channelEventsListeners.remove(listener);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        notifyAboutMessageReceived(channel, sender, message);
    }

    // TODO pouze vybranemu kanalu
    public void notifyAboutMessageReceived(String channel, String sender, String message) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.messageReceived(sender, message);
    }

    public void notifyAboutUserGetsOp(String initiator, String recipient) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userGetsOp(initiator, recipient);
    }

    public void notifyAboutUserLoseOp(String initiator, String recipient) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userLoseOp(initiator, recipient);
    }

    public void notifyAboutUserGetsVoice(String initiator, String recipient) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userGetsVoice(initiator, recipient);
    }

    public void notifyAboutUserLoseVoice(String initiator, String recipient) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userLoseVoice(initiator, recipient);
    }

    public void notifyAboutUserChangesNick(String oldNick, String newNick) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userChangesNick(oldNick, newNick);
    }

    public void notifyAboutUserJoined(String nickname) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userJoined(nickname);
    }

    public void notifyAboutUserLeft(String nickname) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userLeft(nickname);
    }

    public void notifyAboutUserQuit(String nickname, String reason) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userQuit(nickname, reason);
    }

    public void notifyAboutUserKicked(String initiator, String recipient) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userKicked(initiator, recipient);
    }

    public void notifyAboutUserBanned(String initiator, String recipient) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userBanned(initiator, recipient);
    }

    public void notifyAboutUserUnbanned(String initiator, String recipient) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.userUnbanned(initiator, recipient);
    }

    public void notifyAboutTopicChanged(String initiator, String topic) {
        for (ChannelEventsListener listener : channelEventsListeners)
            listener.topicChanged(initiator, topic);
    }

    /*        NICKNAME EVENTS          */

    public void addMyNickChangeListener(MyNickChangeListener listener) {
        myNickChangeListeners.add(listener);
    }

    public void removeMyNickChangeListener(MyNickChangeListener listener) {
        myNickChangeListeners.remove(listener);
    }

    public void notifyAboutMyNickChange(String newNickname) {
        for (MyNickChangeListener listener : myNickChangeListeners)
            listener.myNickHasChanged(newNickname);
    }

}
