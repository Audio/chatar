package Client;

import java.util.*;


public class User implements Comparable<User> {

    private static final String PREFIX_OWNER = "~",
                                PREFIX_ADMIN = "&",
                                PREFIX_OPERATOR = "@",
                                PREFIX_HALF_OPERATOR = "%",
                                PREFIX_VOICE = "+";

    private static final String MODE_OWNER = "q",
                                MODE_ADMIN = "a",
                                MODE_OPERATOR = "o",
                                MODE_HALF_OPERATOR = "h",
                                MODE_VOICE = "v";

    private static final String TEXT_OWNER = "Owner",
                                TEXT_ADMIN = "Admin",
                                TEXT_OPERATOR = "Operator",
                                TEXT_HALF_OPERATOR = "Half operator",
                                TEXT_VOICE = "Voice",
                                TEXT_USER = "User";

    private static final List<String> PRIORITIES = java.util.Arrays.asList(
                                        PREFIX_OWNER, PREFIX_ADMIN, PREFIX_OPERATOR,
                                        PREFIX_HALF_OPERATOR, PREFIX_VOICE, "");

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

    public static String getTextualRepresentation(String mode) {
        switch (mode) {
            case MODE_OWNER:
                return TEXT_OWNER;
            case MODE_ADMIN:
                return TEXT_ADMIN;
            case MODE_OPERATOR:
                return TEXT_OPERATOR;
            case MODE_HALF_OPERATOR:
                return TEXT_HALF_OPERATOR;
            case MODE_VOICE:
                return TEXT_VOICE;
            default:
                return TEXT_USER;
        }
    }

    public String getTextualRepresentation() {
        if ( prefixes.contains(PREFIX_OWNER) )
            return TEXT_OWNER;

        if ( prefixes.contains(PREFIX_ADMIN) )
            return TEXT_ADMIN;

        if ( prefixes.contains(PREFIX_OPERATOR) )
            return TEXT_OPERATOR;

        if ( prefixes.contains(PREFIX_HALF_OPERATOR) )
            return TEXT_HALF_OPERATOR;

        if ( prefixes.contains(PREFIX_VOICE) )
            return TEXT_VOICE;

        return TEXT_USER;
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

    private String getPrefixForMode(String mode) {
        switch (mode) {
            case MODE_OWNER:
                return PREFIX_OWNER;
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
        int prefixComparation = comparePrefixes( getPrefix() , other.getPrefix() );
        if (prefixComparation == 0)
            return getNickname().compareToIgnoreCase( other.getNickname() );
        else
            return prefixComparation;
    }

    private int comparePrefixes(String prefix1, String prefix2) {
        int p1 = PRIORITIES.indexOf(prefix1);
        int p2 = PRIORITIES.indexOf(prefix2);
        return Integer.compare(p1, p2);
    }

}
