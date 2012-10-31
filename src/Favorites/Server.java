package Favorites;

import java.util.HashMap;


public class Server {

    // title, address, port, nickname, channels
    HashMap<String, String> attributes;


    public Server() {
        attributes = new HashMap<>();
    }

    public String get(String key) {
        return attributes.get(key);
    }

    public void set(String key, String value) {
        attributes.put(key, value);
    }

    public HashMap<String, String> getAll() {
        return attributes;
    }

}
