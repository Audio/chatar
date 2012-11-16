package MainWindow;

import Client.GUI;
import Favorites.ConnectionDetails;
import java.awt.Dimension;
import javax.swing.*;


public class MainWindow extends JFrame {

    static final long serialVersionUID = 1L;

    private static final int INITIAL_WIDTH = 900,
                             INITIAL_HEIGHT = 600;

    private static MainWindow instance;

    private TabContainer tabContainer;
    private MainMenu menuBar;
    private NickButton nickButton;
    private InputField inputField;


    public static MainWindow getInstance() {
        if (instance == null)
            instance = new MainWindow();

        return instance;
    }

    private MainWindow() {
        resetTitle();
        GUI.setPreferredSize(this, INITIAL_WIDTH, INITIAL_HEIGHT);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage( new ImageIcon("img/app-icon.png").getImage() );

        createMenuBar();
        createContentArea();
        validate();
    }

    public final void resetTitle() {
        setTitle("Chatař - IRC klient");
    }

    private void createMenuBar() {
        menuBar = new MainMenu();
        setJMenuBar(menuBar);
    }

    private void createContentArea() {
        tabContainer = new TabContainer(INITIAL_WIDTH - 50, 400);
        JPanel inputArea = createInputArea();

        JPanel contentPanel = (JPanel) getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPanel.setLayout(layout);
        contentPanel.add(tabContainer);
        contentPanel.add(inputArea);

        layout.putConstraint(SpringLayout.NORTH, inputArea, 0, SpringLayout.SOUTH, tabContainer);
        layout.putConstraint(SpringLayout.SOUTH, contentPanel, 44, SpringLayout.SOUTH, tabContainer);

        layout.putConstraint(SpringLayout.EAST, tabContainer, 0, SpringLayout.EAST, contentPanel);
        layout.putConstraint(SpringLayout.WEST, tabContainer, 0, SpringLayout.WEST, contentPanel);

        layout.putConstraint(SpringLayout.EAST, inputArea, 0, SpringLayout.EAST, contentPanel);
        layout.putConstraint(SpringLayout.WEST, inputArea, 0, SpringLayout.WEST, contentPanel);
    }

    private JPanel createInputArea() {
        JPanel area = new JPanel();
        GUI.setPreferredSize(area, INITIAL_WIDTH - 50, 100);
        area.setLayout( new BoxLayout(area, BoxLayout.PAGE_AXIS) );

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout( new BoxLayout(innerPanel, BoxLayout.LINE_AXIS) );
        innerPanel.setBorder( BorderFactory.createEmptyBorder(8, 10, 8, 10) );

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

    public ServerTab createServerTab(ConnectionDetails address) {
        return tabContainer.createServerTab(address);
    }

    public void removeTab(AbstractTab tab) {
        tabContainer.removeTab(tab);

        if ( tabContainer.getTabCount() == 0 ) {
            menuBar.toggleConnectionActions(false);
            menuBar.toggleUserMenuBar(false);
            menuBar.togglePanelActions(false);
        }
    }

    public TabContainer getTabContainer() {
        return tabContainer;
    }

    public MainMenu getMainMenu() {
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
