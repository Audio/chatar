package Client;

import javax.swing.*;


public class MainWindow extends JFrame {

    private TabContainer tabContainer;
    private GMenuBar menuBar;
    private GInput input;


    public MainWindow() {
        setTitle("Chatař - IRC klient");
        setSize(700, 500);
        GUI.setPreferredSize(this, 700, 500);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setIconImage( new ImageIcon("img/app-icon.png").getImage() );

        createMenuBar();
        createContentArea();
    }

    private void createMenuBar() {
        menuBar = new GMenuBar();
        setJMenuBar(menuBar);
        menuBar.setVisible(true);
    }

    private void createContentArea() {
        tabContainer = new TabContainer(650, 400);
        input = new GInput(650, 100);

        // umisteni panelu ve formulari
        JPanel content_panel = (JPanel) getContentPane();
        SpringLayout layout = new SpringLayout();
        content_panel.setLayout(layout);
        content_panel.add(tabContainer);
        content_panel.add(input);

        // Tab umisten nad Input
        layout.putConstraint(SpringLayout.WEST, input, 0, SpringLayout.WEST, tabContainer);
        layout.putConstraint(SpringLayout.NORTH, input, 0, SpringLayout.SOUTH, tabContainer);
        // Roztahovani vertikalni - tab
        layout.putConstraint(SpringLayout.EAST, content_panel, 0, SpringLayout.EAST, tabContainer);
        layout.putConstraint(SpringLayout.SOUTH, content_panel, 40, SpringLayout.SOUTH, tabContainer);
        // Roztahovani hirizontalni - tab, input
        layout.putConstraint(SpringLayout.EAST, content_panel, 0, SpringLayout.EAST, input);
        layout.putConstraint(SpringLayout.EAST, input, 0, SpringLayout.EAST, tabContainer);

        tabContainer.setVisible(true);
        input.setVisible(true);
        input.getTextField().requestFocusInWindow();
    }

    public TabContainer getTabContainer() {
        return tabContainer;
    }

    public GMenuBar getGMenuBar() {
        return menuBar;
    }

    public GInput getGInput() {
        return input;
    }

}
