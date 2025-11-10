package service;

import model.Budget;
import model.Transaction;
import model.User;
import model.Wallet;
import util.ConsoleUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class WalletService {
    private final PersistenceService persistence;

    public WalletService(PersistenceService persistence) { this.persistence = persistence; }

    public void loadWalletFor(User u) {
        Wallet w = persistence.load(u.getLogin());
        ConsoleUtils.setWallet(u, w);
        System.out.println("Wallet loaded. Balance: " + w.getBalance());
    }

    public void saveWalletFor(User u) {
        Wallet w = ConsoleUtils.getWallet(u);
        persistence.save(u.getLogin(), w);
    }

    public void handleAddIncome(User u, String arg) {
        if (arg.isBlank()) { System.out.println("Usage: add-income <category> <amount> [note]"); return; }
        String[] p = arg.split("\\s+", 3);
        if (p.length < 2) { System.out.println("Specify category and amount."); return; }
        String cat = p[0];
        double amt;
        try { amt = Double.parseDouble(p[1]); if (amt <= 0) throw new NumberFormatException(); } catch (NumberFormatException e) { System.out.println("Invalid amount."); return; }
        String note = p.length == 3 ? p[2] : "";
        Transaction t = new Transaction(Transaction.Type.INCOME, cat, amt, LocalDateTime.now(), note);
        Wallet w = ConsoleUtils.getWallet(u);
        w.addTransaction(t);
        System.out.println("Added income: " + amt + " to " + cat);
    }

    public void handleAddExpense(User u, String arg) {
        if (arg.isBlank()) { System.out.println("Usage: add-expense <category> <amount> [note]"); return; }
        String[] p = arg.split("\\s+", 3);
        if (p.length < 2) { System.out.println("Specify category and amount."); return; }
        String cat = p[0];
        double amt;
        try { amt = Double.parseDouble(p[1]); if (amt <= 0) throw new NumberFormatException(); } catch (NumberFormatException e) { System.out.println("Invalid amount."); return; }
        String note = p.length == 3 ? p[2] : "";
        Transaction t = new Transaction(Transaction.Type.EXPENSE, cat, amt, LocalDateTime.now(), note);
        Wallet w = ConsoleUtils.getWallet(u);
        w.addTransaction(t);
        System.out.println("Added expense: " + amt + " to " + cat);
    }

    public void handleSetBudget(User u, String arg) {
        if (arg.isBlank()) { System.out.println("Usage: set-budget <category> <limit>"); return; }
        String[] p = arg.split("\\s+", 2);
        if (p.length < 2) { System.out.println("Specify category and limit."); return; }
        String cat = p[0];
        double lim;
        try { lim = Double.parseDouble(p[1]); } catch (NumberFormatException e) { System.out.println("Invalid limit."); return; }
        Wallet w = ConsoleUtils.getWallet(u);
        w.getBudgets().put(cat, new Budget(cat, lim));
        System.out.println("Set budget for " + cat + " = " + lim);
    }

    public void printSummary(User u) {
        Wallet w = ConsoleUtils.getWallet(u);
        System.out.println("\n--- SUMMARY for " + u.getLogin() + " ---");
        System.out.println("Balance: " + w.getBalance());
        System.out.println("Total income: " + w.getTransactions().stream().filter(t->t.getType()==Transaction.Type.INCOME).mapToDouble(Transaction::getAmount).sum());
        System.out.println("Total expenses: " + w.getTransactions().stream().filter(t->t.getType()==Transaction.Type.EXPENSE).mapToDouble(Transaction::getAmount).sum());
        System.out.println("\nIncome by category:");
        Map<String, Double> inc = w.getTransactions().stream().filter(t->t.getType()==Transaction.Type.INCOME).collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
        inc.forEach((k,v)-> System.out.println(k+": "+v));
        System.out.println("\nExpenses by category:");
        Map<String, Double> exp = w.getTransactions().stream().filter(t->t.getType()==Transaction.Type.EXPENSE).collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
        exp.forEach((k,v)-> System.out.println(k+": "+v));
    }

    // --- Новые методы для Transfer, Export, Import ---

    public void handleTransfer(User sender, String arg, AuthService auth) {
        if (arg.isBlank()) { System.out.println("Usage: transfer <user> <amount>"); return; }
        String[] p = arg.split("\\s+");
        if (p.length < 2) { System.out.println("Specify recipient and amount"); return; }
        String recipientLogin = p[0];
        double amt;
        try { amt = Double.parseDouble(p[1]); if (amt <= 0) throw new NumberFormatException(); } catch (NumberFormatException e) { System.out.println("Invalid amount"); return; }

        User recipient = auth.login(recipientLogin, ""); // Получаем пользователя без пароля
        if (recipient == null) { System.out.println("Recipient not found"); return; }

        Wallet wSender = ConsoleUtils.getWallet(sender);
        Wallet wRecipient = ConsoleUtils.getWallet(recipient);

        if (wSender.getBalance() < amt) { System.out.println("Insufficient funds"); return; }

        Transaction tOut = new Transaction(Transaction.Type.TRANSFER, "Transfer to "+recipientLogin, amt, LocalDateTime.now(), "");
        tOut.setCounterparty(recipientLogin);
        wSender.addTransaction(tOut);

        Transaction tIn = new Transaction(Transaction.Type.TRANSFER, "Transfer from "+sender.getLogin(), amt, LocalDateTime.now(), "");
        tIn.setCounterparty(sender.getLogin());
        wRecipient.addTransaction(tIn);

        System.out.println("Transferred " + amt + " to " + recipientLogin);
    }

    public void exportWallet(User u, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(ConsoleUtils.getWallet(u));
            System.out.println("Wallet exported to " + filename);
        } catch (Exception e) { System.out.println("Export failed: " + e.getMessage()); }
    }

    public void importWallet(User u, String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Wallet w = (Wallet) ois.readObject();
            ConsoleUtils.setWallet(u, w);
            System.out.println("Wallet imported from " + filename);
        } catch (Exception e) { System.out.println("Import failed: " + e.getMessage()); }
    }
}

