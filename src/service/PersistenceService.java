package service;

import model.Wallet;

import java.io.*;

public class PersistenceService {
    private final File dir = new File("data/wallets");

    public PersistenceService() {
        if (!dir.exists()) dir.mkdirs();
    }

    public void save(String username, Wallet wallet) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, username+".dat")))) {
            oos.writeObject(wallet);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public Wallet load(String username) {
        File f = new File(dir, username+".dat");
        if (!f.exists()) return new Wallet();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Wallet) ois.readObject();
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}

