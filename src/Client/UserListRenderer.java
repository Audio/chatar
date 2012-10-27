package Client;

import java.awt.Component;
import javax.swing.*;


public class UserListRenderer extends DefaultListCellRenderer {

    private Component addPrefixBasedIcon(Component c, User user) {
        if ( user.isOwner() )
            setOwnerIcon(c);
        else if ( user.isAdmin() )
            setAdminIcon(c);
        else if ( user.isOperator() )
            setOperatorIcon(c);
        else if ( user.isHalfOperator() )
            setHalfOperatorIcon(c);
        else if ( user.isVoice() )
            setVoiceIcon(c);
        else
            setCommonUserIcon(c);

        ((JLabel) c).setText( user.getNickname() );

        return c;
    }

    private Component setOwnerIcon(Component c) {
        ((JLabel) c).setIcon( new ImageIcon("img/owner.png") );
        return c;
    }

    private Component setAdminIcon(Component c) {
        ((JLabel) c).setIcon( new ImageIcon("img/admin.png") );
        return c;
    }

    private Component setOperatorIcon(Component c) {
        ((JLabel) c).setIcon( new ImageIcon("img/operator.png") );
        return c;
    }

    private Component setHalfOperatorIcon(Component c) {
        ((JLabel) c).setIcon( new ImageIcon("img/halfoperator.png") );
        return c;
    }

    private Component setVoiceIcon(Component c) {
        ((JLabel) c).setIcon( new ImageIcon("img/voice.png") );
        return c;
    }

    private Component setCommonUserIcon(Component c) {
        ((JLabel) c).setIcon( new ImageIcon("img/user.png") );
        return c;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        return addPrefixBasedIcon(c, (User) value);
    }

}
