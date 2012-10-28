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
public class DeprecatedConnection extends Thread {

    private String server;
    private int port;

    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private DeprecatedCommandQuery query;
    /**
     * Přidružený konfigurační objekt.
     */
    public  Config config;
    private AbstractTab tab;
    private boolean authenticated;
    private boolean closedByServer;

    private ArrayList<ChannelEventsListener> channelEventsListeners;


    public DeprecatedConnection() {
        this (null, 0);
    }

    public DeprecatedConnection(String server, int port) {
        this.server = server;
        this.port = port;
        this.query = new DeprecatedCommandQuery(this);
        this.config = new Config();
        this.authenticated = false;
        this.closedByServer = false;
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
    public DeprecatedCommandQuery getQuery() {
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
            output( HTML.small("info") +  "Přihlašuji se s přezdívkou " + config.nickname + ".");
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
                DeprecatedReply.create(reply, this);
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
        getTab().appendText(str);
    }

}
