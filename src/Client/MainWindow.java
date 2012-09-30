package Client;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class MainWindow extends JFrame {

    public MainWindow() {

        setTitle("Chatař - IRC klient");
        setSize(700, 500);
        GUI.setPreferredSize(this, 700, 500);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setIconImage( new ImageIcon("img/app-icon.png").getImage() );

    }

}
