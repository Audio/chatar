package Connection;

import Favorites.ConnectionDetails;
import java.io.IOException;
import java.util.ArrayList;
import org.jibble.pircbot.*;


public class Connection extends PircBot implements Runnable {

    private String server;
    private int port;

    private ServerEventsListener serverEventsListener;
    private ArrayList<ChannelEventsListener> channelEventsListeners;
    private ArrayList<PrivateMessagingListener> privateMessagingListeners;


    public Connection(ConnectionDetails cd) {
        this.server = cd.address;
        this.port = cd.port;
        this.channelEventsListeners = new ArrayList<>();
        this.privateMessagingListeners = new ArrayList<>();

        setName(cd.nickname);
        setAutoNickChange(true);
        setLogin(cd.username);
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

    public void setAway(String reason) {
        sendRawLine("AWAY :" + reason);
    }

    public void setNowAway() {
        sendRawLine("AWAY");
    }

    public void whois(String nickname) {
        sendRawLine("WHOIS " + nickname);
    }


    /*         GLOBAL EVENTS           */

    private void handleAwayStatusResponse(int code) {
        boolean isAway = (code == RPL_NOWAWAY);

        if (serverEventsListener != null)
            serverEventsListener.awayStatusChanged(isAway);

        for (ChannelEventsListener listener : channelEventsListeners)
            listener.awayStatusChanged(isAway);

        for (PrivateMessagingListener listener : privateMessagingListeners)
            listener.awayStatusChanged(isAway);
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
            serverEventsListener.serverMessageReceived(code, response);

        switch (code) {
            case RPL_WHOISUSER:
            case RPL_WHOISSERVER:
            case RPL_WHOISIDLE:
            case RPL_WHOISCHANNELS:
                handleWhoisResponse(code, response); break;
            case RPL_AWAY:
                handleUserIsAwayResponse(response); break;
            case RPL_LUSERCHANNELS:
                handleChannelCountResponse(response); break;
            case RPL_UNAWAY:
            case RPL_NOWAWAY:
                handleAwayStatusResponse(code); break;
            case ERR_CHANNELISFULL:
            case ERR_INVITEONLYCHAN:
            case ERR_BANNEDFROMCHAN:
            case ERR_BADCHANNELKEY:
                handleCannotJoinChannel(response); break;
        }
    }

    private void handleChannelCountResponse(String response) {
        String count = response.split(" ", 3)[1];
        if (serverEventsListener != null)
            serverEventsListener.channelCountReceived(count);
    }

    @Override
    protected void onNotice(String sourceNick, String sourceLogin,
                            String sourceHostname, String target, String notice) {
        if (serverEventsListener != null)
            serverEventsListener.noticeMessageReceived(sourceNick, notice);
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
            if ( listener.getChannelName().equalsIgnoreCase(name) )
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
    protected void onAction(String sender, String login, String hostname, String target, String action) {
        if ( target.startsWith("#") ) {
            ChannelEventsListener listener = getChannelEventsListener(target);
            if (listener != null)
                listener.actionReceived(sender, action);
        } else {
            PrivateMessagingListener listener = getPrivateMessagingListener(sender);
            if (listener != null)
                listener.actionReceived(action);
        }
    }

    @Override
    protected void onUserList(String channel, User[] users) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null) {
            MainWindow.User[] u = MainWindow.User.toUsers(users);
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

        boolean isMe = newNick.equals( getNick() );
        if (serverEventsListener != null && isMe)
            serverEventsListener.myNickHasChanged(newNick);
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userJoined(sender);
        else if (serverEventsListener != null)
            serverEventsListener.joined(channel);
    }

    private void handleCannotJoinChannel(String response) {
        String channel = response.split(" ")[1];
        ChannelEventsListener listener = getChannelEventsListener(channel);
        if (listener != null)
            listener.userLeft( this.getNick() );
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
            if ( listener.getNickname().equalsIgnoreCase(nickname) )
                return listener;
        }

        return null;
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        PrivateMessagingListener listener = getPrivateMessagingListener(sender);
        if (listener != null)
            listener.privateMessageReceived(message);
        else if (serverEventsListener != null)
            serverEventsListener.privateMessageWithoutListenerReceived(sender, message);
    }

    private void handleWhoisResponse(int code, String response) {
        String[] parts = response.split(" ", 3);
        String nickname = parts[1];
        response = parts[2];

        PrivateMessagingListener listener = getPrivateMessagingListener(nickname);
        if (listener == null) {
            if (serverEventsListener == null) {
                return;
            } else {
                serverEventsListener.privateMessageWithoutListenerReceived(
                                    nickname, "[zaslány údaje o uživateli]");
                listener = getPrivateMessagingListener(nickname);
            }
        }

        if (code == RPL_WHOISCHANNELS) {
            response = response.substring(1);
        } else if (code == RPL_WHOISIDLE) {
            response = response.substring(0, response.indexOf(" ") );
        }

        switch (code) {
            case RPL_WHOISUSER:
                listener.whoisUser(response); break;
            case RPL_WHOISSERVER:
                listener.whoisServer(response); break;
            case RPL_WHOISIDLE:
                listener.whoisIdle(response); break;
            case RPL_WHOISCHANNELS:
                listener.whoisChannels(response); break;
        }
    }

    private void handleUserIsAwayResponse(String response) {
        String[] parts = response.split(" ", 3);
        String reason = parts[2].substring(1);

        PrivateMessagingListener listener = getPrivateMessagingListener(parts[1]);
        if (listener != null)
            listener.userIsAway(reason);
    }

}
