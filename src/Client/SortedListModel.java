package Client;

import javax.swing.*;
import java.util.*;


public class SortedListModel<E> extends AbstractListModel {

    private SortedSet<E> model;


    public SortedListModel() {
        model = new TreeSet(String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    public int getSize() {
        return model.size();
    }

    @Override
    public E getElementAt(int index) {
        return (E) model.toArray()[index];
    }

    public void add(E element) {
        if ( model.add(element) )
            fireContentsChanged(this, 0, getSize() );
    }

    public void clear() {
        model.clear();
        fireContentsChanged(this, 0, getSize() );
    }

    public boolean contains(E element) {
        return model.contains(element);
    }

    public boolean remove(int index) {
        return remove( getElementAt(index) );
    }

    public boolean remove(E element) {
        boolean removed = model.remove(element);
        if (removed)
            fireContentsChanged(this, 0, getSize() );

        return removed;
    }

}
