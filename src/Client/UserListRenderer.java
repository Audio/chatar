package Client;

import java.awt.Component;
import javax.swing.*;


public class UserListRenderer extends DefaultListCellRenderer {

    public static final String PREFIX_OWNER = "~",
                               PREFIX_ADMIN = "&",
                               PREFIX_OPERATOR = "@",
                               PREFIX_HALF_OPERATOR = "%",
                               PREFIX_VOICE = "+";


    private Component addPrefixBasedIcon(Component c, String nick) {
        String prefix = nick.substring(0, 1);
        switch (prefix) {
            case PREFIX_OWNER:          setOwnerIcon(c); break;
            case PREFIX_ADMIN:          setAdminIcon(c); break;
            case PREFIX_OPERATOR:       setOperatorIcon(c); break;
            case PREFIX_HALF_OPERATOR:  setHalfOperatorIcon(c); break;
            case PREFIX_VOICE:          setVoiceIcon(c); break;
            default:                    setCommonUserIcon(c);
        }

        ((JLabel) c).setText( removePrefix(nick) );

        return c;
    }

    private boolean isOwner(String nick) {
        return nick.startsWith(PREFIX_OWNER);
    }

    private boolean isAdmin(String nick) {
        return nick.startsWith(PREFIX_ADMIN);
    }

    private boolean isOperator(String nick) {
        return nick.startsWith(PREFIX_OPERATOR);
    }

    private boolean isHalfOperator(String nick) {
        return nick.startsWith(PREFIX_HALF_OPERATOR);
    }

    private boolean isVoice(String nick) {
        return nick.startsWith(PREFIX_VOICE);
    }

    private Component setCommonUserIcon(Component c) {
        ((JLabel) c).setIcon( new ImageIcon("img/user.png") );
        return c;
    }

    private Component setOwnerIcon(Component c) {
        // TODO owner icon
        ((JLabel) c).setIcon( new ImageIcon("img/operator.png") );
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

    private Component setAdminIcon(Component c) {
        ((JLabel) c).setIcon( new ImageIcon("img/bot.png") );
        return c;
    }

    public String removePrefix(String nick) {
        if ( isOwner(nick) || isOperator(nick) || isHalfOperator(nick) || isVoice(nick) || isAdmin(nick) )
            return nick.substring(1);

        return nick;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        return addPrefixBasedIcon(c, (String) value);
    }

}
