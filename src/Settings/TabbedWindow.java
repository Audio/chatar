package Settings;

import Client.GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public abstract class TabbedWindow extends JFrame {

    protected final int WINDOW_WIDTH = 500;
    protected final int WINDOW_HEIGHT = 290;
    protected JTabbedPane tabPanel;


    protected abstract JPanel createButtonPanel();

    protected final void createMainPanel() {
        JPanel contentPanel = (JPanel) getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPanel.setLayout(layout);

        tabPanel = new JTabbedPane(JTabbedPane.LEFT);
        tabPanel.setBorder( new EmptyBorder(10, 10, 0, 10) );
        GUI.setExactSize(tabPanel, WINDOW_WIDTH - 10, 210);

        JPanel buttonPanel = createButtonPanel();
        GUI.setExactSize(buttonPanel, WINDOW_WIDTH, 60);

        layout.putConstraint(SpringLayout.WEST, buttonPanel, 0, SpringLayout.WEST, tabPanel);
        layout.putConstraint(SpringLayout.NORTH, buttonPanel, 0, SpringLayout.SOUTH, tabPanel);

        contentPanel.add(tabPanel);
        contentPanel.add( Box.createVerticalGlue() );
        contentPanel.add(buttonPanel);
    }

    protected void close() {
        setVisible(false);
    }

}
