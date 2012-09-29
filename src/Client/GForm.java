package Client;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Hlavní okno aplikace.
 *
 * @author Martin Fouček
 */
public class GForm extends JFrame {

    /**
     * Vytvoří základní formulář celé aplikace.
     * Nastaví mu velikost a pozici (centrování na střed obrazovky).
     */
    public GForm () {

        setTitle("Chatař - IRC klient");
        setSize(700, 500);
        GUI.setMySize(this, 700, 500);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setIconImage( new ImageIcon("img/app-icon.png").getImage() );

    }

}
