package Connection;

import Client.*;
import Config.Config;
import java.io.*;
import java.util.ArrayList;
import org.jibble.pircbot.*;


// TODO threading
public class Connection extends PircBot {

    private String server;
    private int port;

    // TODO public config jo?
    public Config config;
    private AbstractTab tab;
    private boolean authenticated;

    private ArrayList<ServerEventsListener> serverEventsListeners;
    private ArrayList<ChannelEventsListener> channelEventsListeners;
    private ArrayList<MyNickChangeListener> myNickChangeListeners;


    public Connection() {
        this (null, 0);
    }

    public Connection(String server, int port) {
        this.server = server;
        this.port = port;
        this.config = new Config();
        this.authenticated = false;
        this.serverEventsListeners = new ArrayList<>();
        this.channelEventsListeners = new ArrayList<>();
        this.myNickChangeListeners = new ArrayList<>();

        setName("pokusnyHovado");
        setAutoNickChange(true);
        try {
            connect(server, port);
        } catch (Exception e) {
            System.out.println( e.getMessage() );
            e.printStackTrace();
        }
    }

    // TODO pri erroru
    /*
    new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Chyba připojení", "K vybranému serveru se nelze připojit.");
    tab.die("Spojení nelze uskutečnit.");
    */

    // TODO odezva
    /*
    throw new ConnectionException("Adresa serveru není vyplněna.");
    output( Output.HTML.mType("info") +  "Přihlašuji se s přezdívkou " + config.nickname + ".");
    GUI.getInput().setNickname(config.nickname);
    ClientLogger.log("Nelze se připojit: " + e.getMessage(), ClientLogger.ERROR);
    */

    /**
     * Vraci informaci, zda je prezdivka predana parametrem
     * shoda s uzivatelovou aktualne pouzitou prezdivkou.
     */
    // TODO asi pryc
    public boolean isMe(String nickname) {
        return nickname.equals(config.nickname);
    }

    // TODO asi pryc
    public boolean isAuthenticated() {
        return authenticated;
    }

    // TODO asi pryc
    public void authenticate() {
        authenticated = true;
    }

    // TODO pri send:
    /*
    if ( !isConnected() )
        throw new ConnectionException("Klient není připojen k serveru.");
    */

    /**
     * Smerovani vystupu CommandQuery do prislusneho panelu.
     */
    public void setTab(AbstractTab tab) {
        this.tab = tab;
    }

    /**
     * Vraci referenci na panel, do ktereho momentalne smeruje vystup.
     */
    public AbstractTab getTab() {
        return tab;
    }

    /**
     * Vraci referenci na panel (SERVER), do ktereho momentalne smeruje vystup,
     * anebo smeruje vystup do nektereho z jeho kanalu.
     */
    public ServerTab getServerTab() {
        return tab.getServerTab();
    }

    /**
     * Tunel pro vypis vystupniho textu do prislusneho panelu.
     */
    public void output(String str) {
        getTab().addText(str);
    }

    /*         SERVER EVENTS           */

    @Override
    protected void onServerResponse(int code, String response) {
        notifyAboutMessageReceived(response);
    }

    public void addServerEventListener(ServerEventsListener listener) {
        serverEventsListeners.add(listener);
    }

    public void removeServerEventListener(ServerEventsListener listener) {
        serverEventsListeners.remove(listener);
    }

    public void notifyAboutMessageReceived(String message) {
        for (ServerEventsListener listener : serverEventsListeners)
            listener.messageReceived(message);
    }


    /*        CHANNEL EVENTS           */

    public void addChannelEventListener(ChannelEventsListener listener) {
        channelEventsListeners.add(listener);
    }

    public void removeChannelEventListener(ChannelEventsListener listener) {
        channelEventsListeners.remove(listener);
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
