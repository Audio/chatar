package Client;

import javax.swing.*;
import java.util.*;


public class SortedListModel extends AbstractListModel {

    private SortedSet<User> model;


    public SortedListModel() {
        model = new TreeSet();
    }

    @Override
    public int getSize() {
        return model.size();
    }

    // TODO obcas to hazi java.lang.ArrayIndexOutOfBoundsException: 0
    @Override
    public User getElementAt(int index) {
        return (User) model.toArray()[index];
    }

    public User getUser(String nickname) {
        for (User user : model) {
            if ( user.getNickname().equals(nickname) )
                return user;
        }

        return null;
    }

    public void add(User element) {
        if ( model.add(element) )
            contentChanged();
    }

    public void clear() {
        model.clear();
        contentChanged();
    }

    public boolean contains(User element) {
        return model.contains(element);
    }

    public boolean remove(int index) {
        return remove( getElementAt(index) );
    }

    public boolean remove(User element) {
        boolean removed = model.remove(element);
        if (removed)
            contentChanged();

        return removed;
    }

    public void contentChanged() {
        fireContentsChanged(this, 0, getSize() );
    }

}
