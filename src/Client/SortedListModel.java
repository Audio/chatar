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

    public User detachUser(String nickname) {
        for (User user : model) {
            if ( user.getNickname().equals(nickname) ) {
                model.remove(user);
                contentChanged();
                return user;
            }
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
