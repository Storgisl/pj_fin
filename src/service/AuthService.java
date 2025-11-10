package service;

import model.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final File usersFile = new File("data/users.json");
    private Map<String, User> users = new HashMap<>();

    public AuthService() {
        try {
            if (!usersFile.getParentFile().exists()) usersFile.getParentFile().mkdirs();
            if (usersFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usersFile))) {
                    users = (Map<String, User>) ois.readObject();
                }
            }
        } catch (Exception e) { System.out.println("Failed to load users: " + e.getMessage()); }
    }

    public boolean register(String login, String password) {
        if (login == null || login.isBlank() || password == null || password.isBlank()) return false;
        if (users.containsKey(login)) return false;
        User u = new User(login, password);
        users.put(login, u);
        save();
        return true;
    }

    public User login(String login, String password) {
        if (!users.containsKey(login)) return null;
        User u = users.get(login);
        if (u.getPasswordHash().equals(password)) return new User(u.getLogin(), u.getPasswordHash());
        return null;
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile))) {
            oos.writeObject(users);
        } catch (Exception e) { System.out.println("Failed to save users: " + e.getMessage()); }
    }
}

