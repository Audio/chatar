package Config;

import java.util.ArrayList;

/**
 * Implementace historie příkazů (GInput/textový vstup).
 * Sdružuje seznam posledních použitých příkazů. Procházení je možné
 * při použití směrových šipek klávesnice NAHORU (dříve použité příkazy)
 * a DOLU (nedávno použité příkazy). Historie příkazů není dále uchovávána
 * po ukončení programu a má globální platnost, tj. pro všechna připojení
 * k serverům je použita jediná historie.
 *
 * @author Martin Fouček
 */
public class CommandHistory {

    private static ArrayList<String> history;
    private static int position;
    private static int maxCount;

    /**
     * Inicializace.
     */
    static {
        history = new ArrayList<String>();
        maxCount = 10;
    }

    /**
     * Vloži textový příkaz do historie (na konec).
     *
     * @param command
     */
    public static void add (String command) {
        history.add(command);
        cut();
        setPosition( getSize() );
    }

    /**
     * Odstraní první vložený příkaz.
     */
    private static void removeFirst () {
        history.remove(0);
    }

    /**
     * Vrací počet uložených příkazů.
     *
     * @return
     */
    public static int getSize () {
        return history.size();
    }

    /**
     * Do historie je možné uložit maximálně maxCount příkazů.
     * Při překročení této hranice je historie "zkrácena" o nejstarší
     * vložený příkaz.
     * Provádí se při každém vkládání.
     */
    private static void cut () {
        if ( getSize() > maxCount)
            removeFirst();
    }

    /**
     * Vrací prvek před ukazatelem.
     *
     * @return command
     */
    public static String getOlder () {

        if ( getSize() == 0)
            return "";

        if (position == 0) // vrátí nejstarší příkaz, pokud už jsme na konci
            setPosition(1);

        return history.get(--position);

    }

    /**
     * Vrací nasledujici prvek.
     *
     * @return command
     */
    public static String getNewer () {

        if (position + 1 >= getSize() ) {
            setPosition( getSize() );
            return "";
        }

        return history.get(++position);

    }

    /**
     * Nastavuje ukazatel v historii až na následující příkaz.
     *
     * @param new_position
     */
    private static void setPosition (int new_position) {
        position = new_position;
    }

    /**
     * Výpis obsahu (seznam příkazů).
     *
     * @return content of arraylist
     */
    public static String getString () {
        return history.toString();
    }

}
