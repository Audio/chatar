package Settings;


class EventsForm extends javax.swing.JPanel {

    EventsForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logChatLabel = new javax.swing.JLabel();
        clickableLinksLabel = new javax.swing.JLabel();
        rejoinLabel = new javax.swing.JLabel();
        askLabel = new javax.swing.JLabel();
        logChat = new javax.swing.JCheckBox();
        clickableLinks = new javax.swing.JCheckBox();
        rejoin = new javax.swing.JCheckBox();
        ask = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 200));

        logChatLabel.setLabelFor(logChat);
        logChatLabel.setText("Logovat chat do souboru");

        clickableLinksLabel.setLabelFor(clickableLinks);
        clickableLinksLabel.setText("Klikatelné HTTP odkazy");

        rejoinLabel.setLabelFor(rejoin);
        rejoinLabel.setText("Připojit po vyhození z kanálu");

        askLabel.setLabelFor(ask);
        askLabel.setText("Ptát se na ukončení programu");

        logChat.setBackground(new java.awt.Color(255, 255, 255));
        logChat.setNextFocusableComponent(clickableLinks);

        clickableLinks.setBackground(new java.awt.Color(255, 255, 255));
        clickableLinks.setNextFocusableComponent(rejoin);

        rejoin.setBackground(new java.awt.Color(255, 255, 255));
        rejoin.setNextFocusableComponent(ask);

        ask.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rejoinLabel)
                    .addComponent(logChatLabel)
                    .addComponent(clickableLinksLabel)
                    .addComponent(askLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logChat)
                    .addComponent(clickableLinks)
                    .addComponent(rejoin)
                    .addComponent(ask))
                .addGap(187, 187, 187))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logChatLabel)
                    .addComponent(logChat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clickableLinksLabel)
                    .addComponent(clickableLinks))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rejoinLabel)
                    .addComponent(rejoin))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(askLabel)
                    .addComponent(ask))
                .addContainerGap(80, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ask;
    private javax.swing.JLabel askLabel;
    private javax.swing.JCheckBox clickableLinks;
    private javax.swing.JLabel clickableLinksLabel;
    private javax.swing.JCheckBox logChat;
    private javax.swing.JLabel logChatLabel;
    private javax.swing.JCheckBox rejoin;
    private javax.swing.JLabel rejoinLabel;
    // End of variables declaration//GEN-END:variables

    public boolean isLogChatChecked() {
        return logChat.isSelected();
    }

    public void setLogChatChecked(boolean checked) {
        this.logChat.setSelected(checked);
    }

    public boolean isRejoinChecked() {
        return rejoin.isSelected();
    }

    public void setRejoinChecked(boolean checked) {
        this.rejoin.setSelected(checked);
    }

    public boolean isClickableLinksChecked() {
        return clickableLinks.isSelected();
    }

    public void setClickableLinksChecked(boolean checked) {
        this.clickableLinks.setSelected(checked);
    }
    public boolean isAskForQuitChecked() {
        return ask.isSelected();
    }

    public void setAskForQuitChecked(boolean checked) {
        this.ask.setSelected(checked);
    }

}
