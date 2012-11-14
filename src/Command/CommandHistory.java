package Command;

import java.util.ArrayList;


public class CommandHistory {

    private static ArrayList<String> history = new ArrayList<>();
    private static int position;
    private static final int MAX_COUNT = 10;


    public static void add(String command) {
        history.add(command);
        shrink();
        setPosition( getSize() );
    }

    private static void removeFirst() {
        history.remove(0);
    }

    public static int getSize() {
        return history.size();
    }

    private static void shrink() {
        if ( getSize() > MAX_COUNT)
            removeFirst();
    }

    public static String getPrevious() {
        if ( getSize() == 0)
            return "";

        if (position == 0) // vrátí nejstarší příkaz, pokud už jsme na konci
            setPosition(1);

        return history.get(--position);
    }

    public static String getNext() {
        if (position + 1 >= getSize() ) {
            setPosition( getSize() );
            return "";
        }

        return history.get(++position);
    }

    private static void setPosition(int newPosition) {
        position = newPosition;
    }

}
