package MainWindow;

import java.awt.Component;
import javax.swing.*;


public class UserListRenderer extends DefaultListCellRenderer {

    private Component addPrefixBasedIcon(Component c, User user) {
        String filename = user.getTextualRepresentation().toLowerCase().replaceAll(" ", "");
        ((JLabel) c).setIcon( new ImageIcon("img/" + filename + ".png") );
        ((JLabel) c).setText( user.getNickname() );
        return c;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        return addPrefixBasedIcon(c, (User) value);
    }

}
