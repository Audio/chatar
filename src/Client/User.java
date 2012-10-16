package Client;


public class User implements Comparable<User> {

    private static final String PREFIX_OWNER = "~",
                                PREFIX_ADMIN = "&",
                                PREFIX_OPERATOR = "@",
                                PREFIX_HALF_OPERATOR = "%",
                                PREFIX_VOICE = "+";

    private String prefix;
    private String nickname;


    public User(String nickname) {
        this.prefix = "";
        this.nickname = nickname;
    }

    public User(org.jibble.pircbot.User botUser) {
        this.prefix = botUser.getPrefix();
        this.nickname = botUser.getNick();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPrefixedNickname() {
        return prefix + nickname;
    }

    public boolean isOwner() {
        return prefix.equals(PREFIX_OWNER);
    }

    public boolean isAdmin() {
        return prefix.equals(PREFIX_ADMIN);
    }

    public boolean isOperator() {
        return prefix.equals(PREFIX_OPERATOR);
    }

    public boolean isHalfOperator() {
        return prefix.equals(PREFIX_HALF_OPERATOR);
    }

    public boolean isVoice() {
        return prefix.equals(PREFIX_VOICE);
    }

    public void setOwner() {
        prefix = PREFIX_OWNER;
    }

    public void setAdmin() {
        prefix = PREFIX_ADMIN;
    }

    public void setOperator() {
        prefix = PREFIX_OPERATOR;
    }

    public void setHalfOperator() {
        prefix = PREFIX_HALF_OPERATOR;
    }

    public void setVoice() {
        prefix = PREFIX_VOICE;
    }

    public void setCommon() {
        prefix = "";
    }

    public static User[] toUsers(org.jibble.pircbot.User[] botUsers) {
        User[] users = new User[botUsers.length];
        for (int i = 0; i < botUsers.length; ++i)
            users[i] = new User(botUsers[i]);

        return users;
    }

    @Override
    public String toString() {
        return nickname;
    }

    @Override
    public int compareTo(User other) {
        return getNickname().compareToIgnoreCase( other.getNickname() );
    }

}
