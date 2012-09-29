package Config;

import Client.ClientLogger;
import java.io.*;

/**
 * Kazde pripojeni ma vlastni konfiguracni nastaveni.
 * Dale probiha nacteni nastaveni z ulozeneho konfiguracniho souboru
 * (na záznamové médium).
 *
 * @author Martin Fouček
 */
public class Config implements Serializable {

    /**
     * Uživatelova přezdívka.
     */
    public String nickname;
    /**
     * Uživatelské jméno.
     */
    public String username;
    /**
     * Hostname uživatele.
     */
    public String hostname;
    /**
     * Název uživatelova serveru.
     */
    public String servername;
    /**
     * Skutečné uživatelovo jméno.
     */
    public String realname;
    /**
     * Heslo pro připojení na server.
     */
    public String password;

    // manipulace se souborem
    private final static String filename = "options.bin";
    private static final long serialVersionUID = 2389021989L;

    /**
     * Pokyn k nacteni globalniho nastaveni - jiz pri inicializaci.
     */
    public Config () {
        loadFromFile();
    }

    /**
     * Vrati nahodne dvouciferne cislo.
     *
     * @return douciferne cislo
     */
    public int random () {
        int ret = 1;
        while (ret < 10)
            ret = (int) Math.round( Math.random() * 100 );
        return ret;
    }

    /**
     * Nastavuje defaultni hodnoty.
     */
    private void setDefaults () {

        nickname   = "chatar" + random();
        username   = "guest";
        hostname   = "fakehost";
        servername = "lokal";
        realname   = "Jara Cimrman";
        password   = "nic";

    }

    /**
     * Nacteni konfiguracniho souboru.
     */
    public void loadFromFile () {

        try {
            FileInputStream in = new FileInputStream(filename);
            ObjectInputStream objs = new ObjectInputStream(in);
            nickname   = (String) objs.readObject();
            username   = (String) objs.readObject();
            hostname   = (String) objs.readObject();
            servername = (String) objs.readObject();
            realname   = (String) objs.readObject();
            password   = (String) objs.readObject();
            objs.close();
            in.close();
        }
        catch (Exception e) {
            ClientLogger.log(
                    "Chyba pri nacitani " + filename + ": " + e.getClass().getSimpleName(),
                    ClientLogger.ERROR);
            setDefaults();
            saveToFile();
        }

    }

    /**
     * Ulozi nastaveni do souboru.
     */
    public void saveToFile () {

        try {
            FileOutputStream out = new FileOutputStream(filename);
            ObjectOutputStream objs = new ObjectOutputStream(out);
            objs.writeObject(nickname);
            objs.writeObject(username);
            objs.writeObject(hostname);
            objs.writeObject(servername);
            objs.writeObject(realname);
            objs.writeObject(password);
            objs.flush();
            objs.close();
            out.close();
        }
        catch (IOException e) {
            ClientLogger.log(e.getMessage(), ClientLogger.ERROR);
        }

    }

}