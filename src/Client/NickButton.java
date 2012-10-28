package Client;

import Connection.InputHandler;
import java.awt.event.*;
import javax.swing.JButton;


public class NickButton extends JButton {

    public NickButton() {
        super("Chatař");
        setToolTipText("Kliknutím nastavíte přezdívku (vyžaduje aktivní připojení na server)");
        addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractTab tab = MainWindow.getInstance().getActiveTab();
                if (tab == null)
                    return;

                String nickname = MessageDialog.showSetNicknameDialog();
                if (nickname != null)
                    InputHandler.handleNick(nickname);
            }
        });
    }

}
