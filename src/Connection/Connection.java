package Connection;

import Client.*;
import Config.Config;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Objekt třídy Connection komunikuje přes socketové spojení s IRC serverem.
 * Příkazy, které má odeslat, získává z objektu CommandQuery.
 * V pravidelných intervalech načítá zprávy ze server, které následně
 * přehdává ke zpracování třídě Reply.
 *
 * Objekt běží jako samostatné vlákno, aby nezpomaloval vykreslování grafických
 * komponent aplikace.
 *
 * @author Martin Fouček
 */
public class Connection extends Thread {

    private String server;
    private int port;

    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private CommandQuery query;
    /**
     * Přidružený konfigurační objekt.
     */
    public  Config config;
    private AbstractTab tab;
    private boolean authenticated;
    private boolean closedByServer;

    private ArrayList<MyNickChangeListener> myNickChangeListeners;
    private ArrayList<ChannelEventsListener> channelEventsListeners;


    public Connection() {
        this (null, 0);
    }

    public Connection(String server, int port) {
        this.server = server;
        this.port = port;
        this.query = new CommandQuery(this);
        this.config = new Config();
        this.authenticated = false;
        this.closedByServer = false;
        this.myNickChangeListeners = new ArrayList<>();
        this.channelEventsListeners = new ArrayList<>();
    }

    @Override
    public void run() {

        try {
            for (;;) {

                try {
                    loadReply();
                } catch (Exception e) {
                    new MessageDialog(MessageDialog.GROUP_MESSAGE, MessageDialog.TYPE_ERROR, "Chyba připojení", "K vybranému serveru se nelze připojit.");
                    tab.die("Spojení nelze uskutečnit.");
                }

                Thread.sleep(1000);

            }
        }
        catch( InterruptedException e ) { }

    }

    /**
     * Vraci referenci na objekt CommandQuery,
     * pres ktery komunikuje.
     */
    public CommandQuery getQuery() {
        return query;
    }

    /**
     * Navazuje spojeni s vybranym serverem.
     * Po navazani socketoveho spojeni odesila prikazy pro autentizaci.
     *
     * @throws java.lang.Exception
     */
    public void connect() throws Exception {

        if (server == null)
            throw new Exception("Adresa serveru není vyplněna.");

        try {
            socket = new Socket(server, port);
            in = new BufferedReader( new InputStreamReader( socket.getInputStream() ));
            out = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() ) );

            socket.setSoTimeout(100);

            loadReply();
            query.login();
            output( Output.HTML.mType("info") +  "Přihlašuji se s přezdívkou " + config.nickname + ".");
            GUI.getInput().setNickname(config.nickname);

        }
        catch (Exception e) {
            ClientLogger.log("Nelze se připojit: " + e.getMessage(), ClientLogger.ERROR);
        }

    }

    /**
     * Uzavira socketove spojeni.
     * Predtim odesle serveru prikaz k ukonceni spojeni (QUIT).
     *
     * @throws java.lang.Exception
     */
    public void close() throws Exception {

        out.close();
        in.close();
        socket.close();

    }

    /**
     * Informace, zda je otevřeno spojení;
     */
    public boolean isConnected() {
        return (socket != null && socket.isConnected() && !closedByServer);
    }

    /**
     * Vraci informaci, zda je prezdivka predana parametrem
     * shoda s uzivatelovou aktualne pouzitou prezdivkou.
     */
    public boolean isMe(String nickname) {
        return nickname.equals(config.nickname);
    }

    /**
     * Vraci informaci, zda je uzivatel uspesne prihlaseny k serveru.
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Nastavuje priznak uspesneho prihlaseni k serveru.
     * Dalsi info: Reply/handleCode451()
     */
    public void authenticate() {
        authenticated = true;
    }

    /**
     * Odesila serveru zpravy (prikazy).
     *
     * @throws java.lang.Exception
     */
    public void send(String str) throws Exception {

        if ( !isConnected() )
            throw new Exception("Klient není připojen k serveru.");

        query.setBusy();

        str += "\r\n";

        out.write(str);
        out.flush();

        query.setBusy(false);

        maybeQuit(str);

        loadReply();

    }

    /**
     * Od serveru nacita odpovedi. Nasledne je predava na zpracovani
     * objektu tridy Reply.
     *
     * @throws java.lang.Exception
     */
    public void loadReply() throws Exception {

        if ( !isConnected() )
            throw new Exception("Klient není připojen k serveru.");

        if ( query.isBusy() )
            return;

        query.setBusy();

        String reply = null;

        do {
            reply = null;
            try {
                reply = in.readLine();
            }
            catch (Exception e) {
                // vyprsel cas
                query.goOn();
            }
            // zpracovani odpovedi
            if (reply != null)
                Reply.create(reply, this);
        } while (reply != null);

        query.goOn();

    }

    /**
     * Socketové spojení se ukončuje až po odeslání příkazu QUIT.
     * Je důležité, aby se příkaz QUIT stihl odeslat.
     */
    private void maybeQuit(String command) {

        if ( !command.startsWith("QUIT") )
            return;

        try {
            close();
        } catch (Exception e) { }

    }

    /**
     * Nastavuje port pro navazani spojeni.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Nastavuje adresu pro navazani spojeni.
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Smerovani vystupu CommandQuery do prislusneho panelu.
     */
    public void setTab(AbstractTab tab) {
        this.tab = tab;
    }

    /**
     * Server v průběhu komunikace může uzavřít spojení a oznámit
     * to pouze chybovou hláškou.
     */
    public void setClosedByServer() {
        closedByServer = true;
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

}
