package stockbroker.model;

import java.util.HashMap;
import java.util.Map;

public class Account {
    private Double balance;
    private final Map<String, Integer> portfolio;

    public Account(Double initialBalance) {
        this.balance = initialBalance;
        this.portfolio = new HashMap<>();
    }

    public Double getBalance() {
        return balance;
    }

    public Map<String, Integer> getPortfolio() {
        return portfolio;
    }

    public int getShareCount(String symbol) {
        return portfolio.getOrDefault(symbol, 0);
    }

    public boolean hasBalance(double amount) {
        return balance >= amount;
    }

    public boolean hasShares(String symbol, int quantity) {
        return getShareCount(symbol) >= quantity;
    }

    public void debit(double amount) {
        if (!hasBalance(amount)) {
            throw new IllegalStateException("Insufficient balance");
        }
        balance -= amount;
    }

    public void credit(double amount) {
        balance += amount;
    }

    public void addShares(String symbol, int quantity) {
        int currentShares = getShareCount(symbol);
        portfolio.put(symbol, currentShares + quantity);
    }

    public void removeShares(String symbol, int quantity) {
        int held = getShareCount(symbol);
        if (held < quantity) {
            throw new IllegalStateException("Insufficient shares for " + symbol);
        }
        int remaining = held - quantity;
        if (remaining == 0) {
            portfolio.remove(symbol);
        } else {
            portfolio.put(symbol, remaining);
        }
    }
}
