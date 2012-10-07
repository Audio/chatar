package Client;

import javax.swing.*;


public class MainWindow extends JFrame {

    private static MainWindow instance;

    private TabContainer tabContainer;
    private GMenuBar menuBar;
    private Input input;


    public static MainWindow getInstance() {
        if (instance == null)
            instance = new MainWindow();

        return instance;
    }

    private MainWindow() {
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
        input = new Input(650, 100);

        // umisteni panelu ve formulari
        JPanel contentPanel = (JPanel) getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPanel.setLayout(layout);
        contentPanel.add(tabContainer);
        contentPanel.add(input);

        // Tab umisten nad Input
        layout.putConstraint(SpringLayout.WEST, input, 0, SpringLayout.WEST, tabContainer);
        layout.putConstraint(SpringLayout.NORTH, input, 0, SpringLayout.SOUTH, tabContainer);
        // Roztahovani vertikalni - tab
        layout.putConstraint(SpringLayout.EAST, contentPanel, 0, SpringLayout.EAST, tabContainer);
        layout.putConstraint(SpringLayout.SOUTH, contentPanel, 40, SpringLayout.SOUTH, tabContainer);
        // Roztahovani hirizontalni - tab, input
        layout.putConstraint(SpringLayout.EAST, contentPanel, 0, SpringLayout.EAST, input);
        layout.putConstraint(SpringLayout.EAST, input, 0, SpringLayout.EAST, tabContainer);

        tabContainer.setVisible(true);
        input.setVisible(true);
        input.getTextField().requestFocusInWindow();
    }

    public ServerTab createServerTab(String address) {
        return tabContainer.createServerTab(address);
    }

    public void removeTab(AbstractTab tab) {
        tabContainer.removeTab(tab);

        if ( tabContainer.getTabCount() == 0 ) {
            menuBar.toggleDisconectFromAll(false);
            menuBar.toggleDisconectFromServer(false);
            menuBar.toggleUserMenuBar(false);
            menuBar.toggleClosePanel(false);
        }
    }

    public TabContainer getTabContainer() {
        return tabContainer;
    }

    public GMenuBar getGMenuBar() {
        return menuBar;
    }

    public Input getGInput() {
        return input;
    }

    public AbstractTab getActiveTab() {
        return (AbstractTab) tabContainer.getSelectedComponent();
    }

}
