package Settings;


class UserForm extends javax.swing.JPanel {

    UserForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nicknameLabel = new javax.swing.JLabel();
        altNicknameLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        nickname = new javax.swing.JTextField();
        altNickname = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        realnameLabel = new javax.swing.JLabel();
        realname = new javax.swing.JTextField();
        emailLabel = new javax.swing.JLabel();
        email = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(340, 200));

        nicknameLabel.setLabelFor(nickname);
        nicknameLabel.setText("Přezdívka");

        altNicknameLabel.setLabelFor(altNickname);
        altNicknameLabel.setText("Alternativní přezdívka");

        usernameLabel.setLabelFor(username);
        usernameLabel.setText("Uživatelské jméno");

        nickname.setMaximumSize(new java.awt.Dimension(6, 20));
        nickname.setNextFocusableComponent(altNickname);

        altNickname.setMaximumSize(new java.awt.Dimension(6, 20));
        altNickname.setNextFocusableComponent(username);

        username.setMaximumSize(new java.awt.Dimension(6, 20));
        username.setNextFocusableComponent(realname);

        realnameLabel.setLabelFor(realname);
        realnameLabel.setText("Skutečné jméno");

        realname.setNextFocusableComponent(email);

        emailLabel.setLabelFor(email);
        emailLabel.setText("Email");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameLabel)
                    .addComponent(nicknameLabel)
                    .addComponent(altNicknameLabel)
                    .addComponent(realnameLabel)
                    .addComponent(emailLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(altNickname, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addComponent(username, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(realname, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(email, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nickname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nicknameLabel)
                    .addComponent(nickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(altNicknameLabel)
                    .addComponent(altNickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(realnameLabel)
                    .addComponent(realname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailLabel)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField altNickname;
    private javax.swing.JLabel altNicknameLabel;
    private javax.swing.JTextField email;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField nickname;
    private javax.swing.JLabel nicknameLabel;
    private javax.swing.JTextField realname;
    private javax.swing.JLabel realnameLabel;
    private javax.swing.JTextField username;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables

    public String getNickname() {
        return nickname.getText().trim();
    }

    public void setNickname(String nickname) {
        this.nickname.setText(nickname);
    }

    public String getAltNickname() {
        return altNickname.getText().trim();
    }

    public void setAltNickname(String altNickname) {
        this.altNickname.setText(altNickname);
    }

    public String getUsername() {
        return username.getText().trim();
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public String getRealname() {
        return realname.getText().trim();
    }

    public void setRealname(String realname) {
        this.realname.setText(realname);
    }

    public String getEmail() {
        return email.getText().trim();
    }

    public void setEmail(String email) {
        this.email.setText(email);
    }

}
