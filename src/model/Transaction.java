package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type { INCOME, EXPENSE, TRANSFER }

    private Type type;
    private String category;
    private double amount;
    private LocalDateTime dateTime;
    private String note;
    private String counterparty; // для перевода

    public Transaction(Type type, String category, double amount, LocalDateTime dateTime, String note) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.dateTime = dateTime;
        this.note = note;
    }

    public Type getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getNote() { return note; }
    public String getCounterparty() { return counterparty; }

    public void setCounterparty(String counterparty) { this.counterparty = counterparty; }
}

