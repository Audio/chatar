package MainWindow;

import javax.swing.*;
import java.util.*;


public class SortedListModel<E> extends AbstractListModel<E> {

    static final long serialVersionUID = 1L;

    private SortedSet<User> model;


    public SortedListModel() {
        model = new TreeSet<>();
    }

    @Override
    public int getSize() {
        return model.size();
    }

    @Override
    public synchronized E getElementAt(int index) {
        return (E) model.toArray()[index];
    }

    public synchronized User detachUser(String nickname) {
        for (User user : model) {
            if ( user.getNickname().equals(nickname) ) {
                model.remove(user);
                contentChanged();
                return user;
            }
        }

        return null;
    }

    public synchronized void add(User element) {
        if ( model.add(element) )
            contentChanged();
    }

    public synchronized void clear() {
        model.clear();
        contentChanged();
    }

    public boolean contains(String nickname) {
        for (User user : model) {
            if ( user.getNickname().equalsIgnoreCase(nickname) )
                return true;
        }

        return false;
    }

    public String getCompleteNickname(String partialNick) {
        String lowerNick = partialNick.toLowerCase();
        for (User user : model) {
            if ( user.getNickname().toLowerCase().startsWith(lowerNick) )
                return user.getNickname();
        }

        return partialNick;
    }

    public String getNickAfter(String nickname) {
        boolean returnNext = false;
        for (User user : model) {
            if (returnNext)
                return user.getNickname();

            if ( user.getNickname().equalsIgnoreCase(nickname) )
                returnNext = true;
        }

        return nickname;
    }

    private void contentChanged() {
        fireContentsChanged(this, 0, getSize() );
    }

}
