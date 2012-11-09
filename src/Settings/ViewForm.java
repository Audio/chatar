package Settings;


class ViewForm extends javax.swing.JPanel {

    ViewForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayTopicLabel = new javax.swing.JLabel();
        timestampEnabledLabel = new javax.swing.JLabel();
        timestampLabel = new javax.swing.JLabel();
        timestamp = new javax.swing.JTextField();
        tsFormatKeyLabel = new javax.swing.JLabel();
        tsFormatValuesLabel = new javax.swing.JLabel();
        displayTopic = new javax.swing.JCheckBox();
        timestampEnabled = new javax.swing.JCheckBox();
        tsFormatExampleLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(340, 200));

        displayTopicLabel.setLabelFor(displayTopic);
        displayTopicLabel.setText("Zobrazovat téma kanálu");

        timestampEnabledLabel.setLabelFor(timestampEnabled);
        timestampEnabledLabel.setText("Zobrazovat u zpráv časová razíka");

        timestampLabel.setLabelFor(timestamp);
        timestampLabel.setText("Časová razítka u zpráv");

        timestamp.setMaximumSize(new java.awt.Dimension(6, 20));

        tsFormatKeyLabel.setForeground(new java.awt.Color(153, 153, 153));
        tsFormatKeyLabel.setLabelFor(timestamp);
        tsFormatKeyLabel.setText("Formát času:");

        tsFormatValuesLabel.setForeground(new java.awt.Color(153, 153, 153));
        tsFormatValuesLabel.setLabelFor(timestamp);
        tsFormatValuesLabel.setText("h - hodiny, m - minuty, s - sekundy");

        displayTopic.setBackground(new java.awt.Color(255, 255, 255));
        displayTopic.setNextFocusableComponent(timestampEnabled);

        timestampEnabled.setBackground(new java.awt.Color(255, 255, 255));
        timestampEnabled.setNextFocusableComponent(timestamp);

        tsFormatExampleLabel.setForeground(new java.awt.Color(153, 153, 153));
        tsFormatExampleLabel.setLabelFor(timestamp);
        tsFormatExampleLabel.setText("příklad:   [h:m:s]");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timestampLabel)
                            .addComponent(displayTopicLabel)
                            .addComponent(timestampEnabledLabel)
                            .addComponent(tsFormatKeyLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timestampEnabled, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(displayTopic, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(timestamp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tsFormatExampleLabel)
                            .addComponent(tsFormatValuesLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(displayTopicLabel)
                    .addComponent(displayTopic))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timestampEnabledLabel)
                    .addComponent(timestampEnabled))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timestampLabel)
                    .addComponent(timestamp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(tsFormatKeyLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tsFormatValuesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tsFormatExampleLabel)
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox displayTopic;
    private javax.swing.JLabel displayTopicLabel;
    private javax.swing.JTextField timestamp;
    private javax.swing.JCheckBox timestampEnabled;
    private javax.swing.JLabel timestampEnabledLabel;
    private javax.swing.JLabel timestampLabel;
    private javax.swing.JLabel tsFormatExampleLabel;
    private javax.swing.JLabel tsFormatKeyLabel;
    private javax.swing.JLabel tsFormatValuesLabel;
    // End of variables declaration//GEN-END:variables

    public boolean isDisplayTopicChecked() {
        return displayTopic.isSelected();
    }

    public void setDisplayTopicChecked(boolean checked) {
        this.displayTopic.setSelected(checked);
    }

    public boolean isTimestampEnabled() {
        return timestampEnabled.isSelected();
    }

    public void setTimestampEnabled(boolean checked) {
        this.timestampEnabled.setSelected(checked);
    }

    public String getTimestampFormat() {
        return timestamp.getText().trim();
    }

    public void setTimestampFormat(String format) {
        this.timestamp.setText(format);
    }

}
