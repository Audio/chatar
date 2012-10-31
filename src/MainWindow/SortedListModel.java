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
            if ( user.getNickname().equals(nickname) )
                return true;
        }

        return false;
    }

    private void contentChanged() {
        fireContentsChanged(this, 0, getSize() );
    }

}