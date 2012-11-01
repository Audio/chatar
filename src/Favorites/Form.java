package Favorites;

import java.awt.Color;


class Form extends javax.swing.JPanel {

    private FavoritesWindow window;


    Form(FavoritesWindow window) {
        this.window = window;
        initComponents();
        setBackground(Color.WHITE);
        channels.setFont( title.getFont() );
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        nicknameLabel = new javax.swing.JLabel();
        channelsLabel = new javax.swing.JLabel();
        delete = new javax.swing.JButton();
        title = new javax.swing.JTextField();
        address = new javax.swing.JTextField();
        nickname = new javax.swing.JTextField();
        portLabel = new javax.swing.JLabel();
        port = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        channels = new javax.swing.JTextArea();

        setPreferredSize(new java.awt.Dimension(500, 200));

        titleLabel.setLabelFor(title);
        titleLabel.setText("Název v Oblíbených:");

        addressLabel.setLabelFor(address);
        addressLabel.setText("Adresa serveru:");

        nicknameLabel.setLabelFor(nickname);
        nicknameLabel.setText("Přezdívka:");

        channelsLabel.setLabelFor(channels);
        channelsLabel.setText("<html>Automaticky připojit<br>do těchto kanálů:<br><br>(oddělujte čárkou)</html>");

        delete.setText("Odebrat");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        title.setMaximumSize(new java.awt.Dimension(6, 20));
        title.setNextFocusableComponent(address);
        title.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                titleHasChanged(evt);
            }
        });

        address.setMaximumSize(new java.awt.Dimension(6, 20));
        address.setNextFocusableComponent(port);

        nickname.setMaximumSize(new java.awt.Dimension(6, 20));

        portLabel.setLabelFor(port);
        portLabel.setText("Port:");

        port.setMaximumSize(new java.awt.Dimension(6, 20));
        port.setNextFocusableComponent(nickname);

        channels.setColumns(20);
        channels.setLineWrap(true);
        channels.setRows(5);
        jScrollPane1.setViewportView(channels);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nicknameLabel)
                    .addComponent(titleLabel)
                    .addComponent(addressLabel)
                    .addComponent(channelsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nickname, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(portLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(port, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(delete))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delete)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(titleLabel)
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portLabel)
                    .addComponent(port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nicknameLabel)
                    .addComponent(nickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(channelsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        window.actionDeleteCurrent();
    }//GEN-LAST:event_deleteActionPerformed

    private void titleHasChanged(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_titleHasChanged
        String newTitle = getTitle();
        if ( !newTitle.isEmpty() )
            window.serverTitleHasChanged(newTitle);
    }//GEN-LAST:event_titleHasChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField address;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextArea channels;
    private javax.swing.JLabel channelsLabel;
    private javax.swing.JButton delete;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nickname;
    private javax.swing.JLabel nicknameLabel;
    private javax.swing.JTextField port;
    private javax.swing.JLabel portLabel;
    private javax.swing.JTextField title;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    void focusTitle() {
        title.requestFocus();
        title.selectAll();
    }

    String getTitle() {
        return title.getText().trim();
    }

    void setTitle(String value) {
        title.setText(value);
    }

    String getAddress() {
        return address.getText().trim();
    }

    void setAddress(String value) {
        address.setText(value);
    }

    String getPort() {
        return port.getText().trim();
    }

    void setPort(String value) {
        port.setText(value);
    }

    String getNickname() {
        return nickname.getText().trim();
    }

    void setNickname(String value) {
        nickname.setText(value);
    }

    String getChannels() {
        return channels.getText().trim();
    }

    void setChannels(String value) {
        channels.setText(value);
    }

}
