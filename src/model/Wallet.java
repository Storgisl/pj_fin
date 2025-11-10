package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Transaction> transactions = new ArrayList<>();
    private Map<String, Budget> budgets = new HashMap<>();

    public void addTransaction(Transaction t) { transactions.add(t); }
    public List<Transaction> getTransactions() { return transactions; }

    public Map<String, Budget> getBudgets() { return budgets; }

    public double getBalance() {
        double income = transactions.stream().filter(t -> t.getType() == Transaction.Type.INCOME).mapToDouble(Transaction::getAmount).sum();
        double expenses = transactions.stream().filter(t -> t.getType() == Transaction.Type.EXPENSE).mapToDouble(Transaction::getAmount).sum();
        return income - expenses;
    }
}

