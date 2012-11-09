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

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 200));

        displayTopicLabel.setLabelFor(displayTopic);
        displayTopicLabel.setText("Zobrazovat téma kanálu");

        timestampEnabledLabel.setLabelFor(timestampEnabled);
        timestampEnabledLabel.setText("Zobrazovat u zpráv časová razíka");

        timestampLabel.setLabelFor(timestamp);
        timestampLabel.setText("Časová razítka u zpráv");

        timestamp.setMaximumSize(new java.awt.Dimension(6, 20));

        tsFormatKeyLabel.setForeground(new java.awt.Color(153, 153, 153));
        tsFormatKeyLabel.setLabelFor(timestamp);
        tsFormatKeyLabel.setText("Formát času");

        tsFormatValuesLabel.setForeground(new java.awt.Color(153, 153, 153));
        tsFormatValuesLabel.setLabelFor(timestamp);
        tsFormatValuesLabel.setText("h - hodiny, m - minuty, s - sekundy");

        displayTopic.setBackground(new java.awt.Color(255, 255, 255));
        displayTopic.setNextFocusableComponent(timestampEnabled);

        timestampEnabled.setBackground(new java.awt.Color(255, 255, 255));
        timestampEnabled.setNextFocusableComponent(timestamp);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timestampLabel)
                    .addComponent(displayTopicLabel)
                    .addComponent(timestampEnabledLabel)
                    .addComponent(tsFormatKeyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(timestampEnabled)
                        .addComponent(displayTopic))
                    .addComponent(tsFormatValuesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timestamp, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(displayTopicLabel)
                    .addComponent(displayTopic))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(timestampEnabledLabel)
                    .addComponent(timestampEnabled))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timestampLabel)
                    .addComponent(timestamp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tsFormatKeyLabel)
                    .addComponent(tsFormatValuesLabel))
                .addContainerGap(80, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox displayTopic;
    private javax.swing.JLabel displayTopicLabel;
    private javax.swing.JTextField timestamp;
    private javax.swing.JCheckBox timestampEnabled;
    private javax.swing.JLabel timestampEnabledLabel;
    private javax.swing.JLabel timestampLabel;
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
