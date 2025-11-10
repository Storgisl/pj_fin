package tests;

import service.WalletService;
import service.PersistenceService;
import model.User;
import model.Wallet;
import util.ConsoleUtils;

public class TestWalletService {
    public static void main(String[] args) {
        PersistenceService ps = new PersistenceService();
        WalletService ws = new WalletService(ps);
        User u = new User("testwallet", "123");

        ws.loadWalletFor(u);
        ws.handleAddIncome(u, "Salary 1000");
        Wallet w = ConsoleUtils.getWallet(u);
        assert w.getBalance() == 1000.0 : "Balance should be 1000";

        ws.handleAddExpense(u, "Food 200");
        assert w.getBalance() == 800.0 : "Balance should be 800";

        System.out.println("WalletService tests passed.");
    }
}

