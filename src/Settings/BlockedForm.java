package Settings;


class BlockedForm extends javax.swing.JPanel {

    BlockedForm() {
        initComponents();
        blocked.setFont( infoLabel1.getFont() );
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel1 = new javax.swing.JLabel();
        infoLabel2 = new javax.swing.JLabel();
        infoLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        blocked = new javax.swing.JTextArea();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(340, 200));

        infoLabel1.setLabelFor(blocked);
        infoLabel1.setText("Nezobrazovat zprávy od těchto uživatelů:");

        infoLabel2.setForeground(new java.awt.Color(153, 153, 153));
        infoLabel2.setLabelFor(blocked);
        infoLabel2.setText("(platí pro všechny servery)");

        infoLabel3.setForeground(new java.awt.Color(153, 153, 153));
        infoLabel3.setLabelFor(blocked);
        infoLabel3.setText("Přezdívky uživatelů oddělujte čárkami");

        blocked.setColumns(20);
        blocked.setRows(5);
        jScrollPane1.setViewportView(blocked);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(infoLabel1)
                            .addComponent(infoLabel3)
                            .addComponent(infoLabel2))
                        .addGap(0, 118, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(infoLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoLabel3)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea blocked;
    private javax.swing.JLabel infoLabel1;
    private javax.swing.JLabel infoLabel2;
    private javax.swing.JLabel infoLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    public String getBlockedNicknames() {
        return blocked.getText().trim();
    }

    public void setBlockedNicknames(String nicknames) {
        this.blocked.setText(nicknames);
    }

}
