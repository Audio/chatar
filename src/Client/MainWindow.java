package Client;

import java.awt.Dimension;
import javax.swing.*;


public class MainWindow extends JFrame {

    private static MainWindow instance;

    private TabContainer tabContainer;
    private GMenuBar menuBar;
    private NickButton nickButton;
    private InputField inputField;


    public static MainWindow getInstance() {
        if (instance == null)
            instance = new MainWindow();

        return instance;
    }

    private MainWindow() {
        resetTitle();
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

    public final void resetTitle() {
        setTitle("Chatař - IRC klient");
    }

    private void createMenuBar() {
        menuBar = new GMenuBar();
        setJMenuBar(menuBar);
        menuBar.setVisible(true);
    }

    private void createContentArea() {
        tabContainer = new TabContainer(650, 400);
        JPanel inputArea = createInputArea();

        // umisteni panelu ve formulari
        JPanel contentPanel = (JPanel) getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPanel.setLayout(layout);
        contentPanel.add(tabContainer);
        contentPanel.add(inputArea);

        // Tab umisten nad Input
        layout.putConstraint(SpringLayout.WEST, inputArea, 0, SpringLayout.WEST, tabContainer);
        layout.putConstraint(SpringLayout.NORTH, inputArea, 0, SpringLayout.SOUTH, tabContainer);
        // Roztahovani vertikalni - tab
        layout.putConstraint(SpringLayout.EAST, contentPanel, 0, SpringLayout.EAST, tabContainer);
        layout.putConstraint(SpringLayout.SOUTH, contentPanel, 40, SpringLayout.SOUTH, tabContainer);
        // Roztahovani hirizontalni - tab, input
        layout.putConstraint(SpringLayout.EAST, contentPanel, 0, SpringLayout.EAST, inputArea);
        layout.putConstraint(SpringLayout.EAST, inputArea, 0, SpringLayout.EAST, tabContainer);

        tabContainer.setVisible(true);
        inputArea.setVisible(true);
        inputField.requestFocusInWindow();
    }

    private JPanel createInputArea() {
        JPanel area = new JPanel();
        GUI.setPreferredSize(area, 650, 100);
        area.setLayout( new BoxLayout(area, BoxLayout.PAGE_AXIS) );

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout( new BoxLayout(innerPanel, BoxLayout.LINE_AXIS) );
        innerPanel.setBorder( BorderFactory.createEmptyBorder(5, 10, 5, 10) );

        nickButton = new NickButton();
        inputField = new InputField();

        innerPanel.add(nickButton);
        innerPanel.add( Box.createHorizontalGlue() );
        innerPanel.add( Box.createRigidArea( new Dimension(10,0) ) );
        innerPanel.add( Box.createHorizontalGlue() );
        innerPanel.add(inputField);

        area.add(innerPanel);
        return area;
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

    public NickButton getNickButton() {
        return nickButton;
    }

    public InputField getInputField() {
        return inputField;
    }

    public AbstractTab getActiveTab() {
        return (AbstractTab) tabContainer.getSelectedComponent();
    }

}
