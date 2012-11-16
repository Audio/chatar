package Command;

import Client.GUI;
import MainWindow.MainWindow;
import Dialog.PopupWindowDimensions;
import Settings.Settings;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;


public class CustomCommandsWindow extends JFrame implements WindowListener, PopupWindowDimensions, TableModelListener {

    static final long serialVersionUID = 1L;

    private static CustomCommandsWindow instance;

    private Settings settings;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Command> commands;
    private JButton delete;


    public static CustomCommandsWindow getInstance() {
        if (instance == null)
            instance = new CustomCommandsWindow();

        return instance;
    }

    private CustomCommandsWindow() {
        this.settings = Settings.getInstance();

        setAlwaysOnTop(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setIconImage( new ImageIcon("img/commands-icon.png").getImage() );
        setResizable(false);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Vlastní příkazy");

        createMainPanel();
        reloadCommandsTable();

        addWindowListener(this);
    }

    protected final void createMainPanel() {
        JPanel contentPanel = (JPanel) getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPanel.setLayout(layout);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Název");
        tableModel.addColumn("Příkaz");
        tableModel.addTableModelListener(this);

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.getSelectionModel().addListSelectionListener( new TableSelectionListener () );

        int columnPart = WINDOW_WIDTH / 5;
        table.getColumnModel().getColumn(0).setPreferredWidth(columnPart);
        table.getColumnModel().getColumn(1).setPreferredWidth(columnPart * 4);
        table.setRowMargin(10);
        table.setRowHeight(30);

        JScrollPane tablePanel = new JScrollPane(table);
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder( BorderFactory.createEmptyBorder() );
        GUI.setExactSize(tablePanel, WINDOW_WIDTH - 10, 210);

        JPanel buttonPanel = createButtonPanel();
        GUI.setExactSize(buttonPanel, WINDOW_WIDTH, 60);

        layout.putConstraint(SpringLayout.WEST, buttonPanel, 0, SpringLayout.WEST, tablePanel);
        layout.putConstraint(SpringLayout.NORTH, buttonPanel, 0, SpringLayout.SOUTH, tablePanel);

        contentPanel.add(tablePanel);
        contentPanel.add( Box.createVerticalGlue() );
        contentPanel.add(buttonPanel);
    }

    protected JPanel createButtonPanel() {
        JButton create = new JButton("Přidat");
        create.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAddCommand();
            }
        });

        delete = new JButton("Odebrat");
        delete.setEnabled(false);
        delete.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionDeleteCurrent();
            }
        });

        JButton cancel = new JButton("Storno");
        cancel.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });

        JButton save = new JButton("Uložit změny");
        save.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
                close();
            }
        });

        Box buttons = Box.createHorizontalBox();
        buttons.add( Box.createRigidArea( new Dimension(10, 0)) );
        buttons.add(create);
        buttons.add( Box.createRigidArea( new Dimension(10, 0)) );
        buttons.add(delete);
        buttons.add( Box.createRigidArea( new Dimension(WINDOW_WIDTH - 340, 0)) );
        buttons.add(cancel);
        buttons.add( Box.createRigidArea( new Dimension(10, 0)) );
        buttons.add(save);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.X_AXIS) );
        buttonPanel.add(buttons);

        return buttonPanel;
    }

    private void reloadCommandsTable() {
        while ( tableModel.getRowCount() > 0 )
            tableModel.removeRow(0);

        commands = settings.getCommands();
        for (Command c : commands) {
            String[] row = {c.name, c.content};
            tableModel.addRow(row);
        }
    }

    private void actionAddCommand() {
        String name = "";
        String content = "";
        commands.add( new Command(name, content) );
        String row[] = {name, content};
        tableModel.addRow(row);
        table.editCellAt(table.getRowCount() - 1, 0);
        table.requestFocus();
    }

    void actionDeleteCurrent() {
        int row = table.getSelectedRow();
        if (row > -1) {
            commands.remove(row);
            tableModel.removeRow(row);
        }
        delete.setEnabled(false);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if ( e.getType() == TableModelEvent.UPDATE ) {
            int row = e.getFirstRow();
            String newName = (String) tableModel.getValueAt(row, 0);
            String newContent = (String) tableModel.getValueAt(row, 1);
            Command command = commands.get(row);
            command.name = newName;
            command.content = newContent;
        }
    }

    private void saveChanges() {
        cleanUpTable();
        settings.setCommands(commands);
        settings.store();
    }

    private void cleanUpTable() {
        for (int i = 0; i < commands.size(); ++i) {
            commands.get(i).name = commands.get(i).name.toLowerCase();
            if ( commands.get(i).name.isEmpty() ) {
                commands.remove(i);
                tableModel.removeRow(i);
                --i;
            }
        }
    }

    private void actionCancel() {
        reloadCommandsTable();
        close();
    }

    private void close() {
        delete.setEnabled(false);
        setVisible(false);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        setLocationRelativeTo( MainWindow.getInstance() );
    }

    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }

    private class TableSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if ( !e.getValueIsAdjusting() )
                delete.setEnabled(true);
        }

    }

}
