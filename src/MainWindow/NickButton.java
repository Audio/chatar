package MainWindow;

import Connection.InputHandler;
import Dialog.MessageDialog;
import java.awt.event.*;
import javax.swing.JButton;


public class NickButton extends JButton {

    static final long serialVersionUID = 1L;


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
