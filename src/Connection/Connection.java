package Connection;

import Config.Config;
import java.io.IOException;
import java.util.ArrayList;
import org.jibble.pircbot.*;


public class Connection extends PircBot implements Runnable {

    private String server;
    private int port;
    // TODO public config jo? bere se z nej nickname asi
    // TODO config tu nema co delat bych rek
    public Config config;

    private ServerEventsListener serverEventsListener;
    private ArrayList<ChannelEventsListener> channelEventsListeners;
    private ArrayList<PrivateMessagingListener> privateMessagingListeners;


    // TODO vyresit config
    public Connection(String server, int port) {
        this.server = server;
        this.port = port;
        this.config = new Config();
        this.channelEventsListeners = new ArrayList<>();
        this.privateMessagingListeners = new ArrayList<>();

        setName("pokusnyKrecek");
        setAutoNickChange(true);
    }

    @Override
    public void run() {
        try {
            connect(server, port);
            if (serverEventsListener != null)
                serverEventsListener.connected();
        } catch (IOException | IrcException e) {
            if (serverEventsListener != null)
                serverEventsListener.connectionCantBeEstabilished( e.getMessage() );
        }
    }


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
        if (listener != null) {
            Client.User[] u = Client.User.toUsers(users);
            listener.userListReceived(u);
        }
    }

    @Override
    protected void onMode(String channel, String sourceNick, String sourceLogin,
                          String sourceHostname, String mode) {

        // User mode:  +o nickname
        // Channel mode: +m
        String change = mode.substring(0, 1);
        boolean granted = change.equals("+");

        mode = mode.trim().substring(1);
        if ( mode.indexOf(" ") > -1 ) {
            String[] parts = mode.split(" ");
            String type = parts[0];
            String nickname = parts[1];

            ChannelEventsListener listener = getChannelEventsListener(channel);
            if (listener != null) {
                if (granted)
                    listener.userModeGranted(sourceNick, nickname, type);
                else
                    listener.userModeRevoked(sourceNick, nickname, type);
            }
        }
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        for (ChannelEventsListener listener : channelEventsListeners) {
            if ( listener.contains(oldNick) )
                listener.userChangesNick(oldNick, newNick);
        }

        PrivateMessagingListener listener = getPrivateMessagingListener(oldNick);
        if (listener != null)
            listener.userChangesNick(newNick);
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userJoined(sender);
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userLeft(sender);
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        for (ChannelEventsListener listener : channelEventsListeners) {
            if ( listener.contains(sourceNick) )
                listener.userQuit(sourceNick, reason);
        }
    }

    @Override
    protected void onKick(String channel, String kickerNick, String kickerLogin,
                          String kickerHostname, String recipientNick, String reason) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userKicked(kickerNick, recipientNick, reason);
    }

    private void notifyAboutUserBanned(String channel, String initiator, String recipient) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userBanned(initiator, recipient);
    }

    private void notifyAboutUserUnbanned(String channel, String initiator, String recipient) {
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

    private PrivateMessagingListener getPrivateMessagingListener(String nickname) {
        for (PrivateMessagingListener listener : privateMessagingListeners) {
            if ( listener.getNickname().equals(nickname) )
                return listener;
        }

        return null;
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        for (PrivateMessagingListener listener : privateMessagingListeners) {
            if ( listener.getNickname().equals(sender) ) {
                listener.privateMessageReceived(message);
                return;
            }
        }

        if (serverEventsListener != null)
            serverEventsListener.privateMessageWithoutListenerReceived(sender, message);
    }

}
