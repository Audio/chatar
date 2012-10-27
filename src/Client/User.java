package Client;

import java.util.HashSet;


public class User implements Comparable<User> {

    private static final String PREFIX_OWNER = "~",
                                PREFIX_ADMIN = "&",
                                PREFIX_OPERATOR = "@",
                                PREFIX_HALF_OPERATOR = "%",
                                PREFIX_VOICE = "+";

    private static final String MODE_ADMIN = "a",
                                MODE_OPERATOR = "o",
                                MODE_HALF_OPERATOR = "h",
                                MODE_VOICE = "v";

    private HashSet<String> prefixes;
    private String nickname;


    public User(String nickname) {
        this.prefixes = new HashSet<>();
        this.nickname = nickname;
    }

    public User(org.jibble.pircbot.User botUser) {
        this( botUser.getNick() );
        this.addPrefix( botUser.getPrefix() );
    }

    public String getPrefix() {
        if ( prefixes.contains(PREFIX_OWNER) )
            return PREFIX_OWNER;

        if ( prefixes.contains(PREFIX_ADMIN) )
            return PREFIX_ADMIN;

        if ( prefixes.contains(PREFIX_OPERATOR) )
            return PREFIX_OPERATOR;

        if ( prefixes.contains(PREFIX_HALF_OPERATOR) )
            return PREFIX_HALF_OPERATOR;

        if ( prefixes.contains(PREFIX_VOICE) )
            return PREFIX_VOICE;

        return "";
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPrefixedNickname() {
        return getPrefix() + getNickname();
    }

    public String getTextRepresentation() {
        if ( prefixes.contains(PREFIX_OWNER) )
            return "Owner";

        if ( prefixes.contains(PREFIX_ADMIN) )
            return "Admin";

        if ( prefixes.contains(PREFIX_OPERATOR) )
            return "Operator";

        if ( prefixes.contains(PREFIX_HALF_OPERATOR) )
            return "Half operator";

        if ( prefixes.contains(PREFIX_VOICE) )
            return "Voice";

        return "User";
    }

    private void addPrefix(String prefix) {
        switch (prefix) {
            case PREFIX_OWNER:
            case PREFIX_ADMIN:
            case PREFIX_OPERATOR:
            case PREFIX_HALF_OPERATOR:
            case PREFIX_VOICE:
                prefixes.add(prefix);
        }
    }

    private void removePrefix(String prefix) {
        prefixes.remove(prefix);
    }

    public void addPrefixBasedOnMode(String mode) {
        addPrefix( getPrefixForMode(mode) );
    }

    public void removePrefixBasedOnMode(String mode) {
        removePrefix( getPrefixForMode(mode) );
    }

    public final String getPrefixForMode(String mode) {
        switch (mode) {
            case MODE_ADMIN:
                return PREFIX_ADMIN;
            case MODE_OPERATOR:
                return PREFIX_OPERATOR;
            case MODE_HALF_OPERATOR:
                return PREFIX_HALF_OPERATOR;
            case MODE_VOICE:
                return PREFIX_VOICE;
            default:
                return "";
        }
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
