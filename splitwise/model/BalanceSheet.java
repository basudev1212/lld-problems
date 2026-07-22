package model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceSheet {
    private final User owner;
    // Personal: positive => otherUser owes owner.
    // Group: positive => user is a net creditor in the group.
    private final Map<User, Double> balances;

    private BalanceSheet(User owner) {
        this.owner = owner;
        this.balances = new ConcurrentHashMap<>();
    }

    public static BalanceSheet forUser(User owner) {
        return new BalanceSheet(owner);
    }

    public static BalanceSheet forGroup() {
        return new BalanceSheet(null);
    }

    public Map<User, Double> getBalances() {
        return balances;
    }

    public synchronized void adjustBalance(User otherUser, double amount) {
        if (owner == null) {
            throw new IllegalStateException("Use applyGroupSplit for group balance sheets.");
        }
        if (owner.equals(otherUser)) {
            return;
        }
        addToBalance(otherUser, amount);
    }

    public synchronized void applyGroupSplit(User paidBy, User participant, double amount) {
        if (owner != null) {
            throw new IllegalStateException("applyGroupSplit is only for group balance sheets.");
        }
        if (paidBy.equals(participant)) {
            return;
        }
        addToBalance(paidBy, amount);
        addToBalance(participant, -amount);
    }

    private void addToBalance(User user, double amount) {
        double currentBalance = balances.getOrDefault(user, 0.0);
        balances.put(user, currentBalance + amount);
    }

    public void showBalances(String title) {
        System.out.println("--- " + title + " ---");
        if (balances.isEmpty()) {
            System.out.println("All settled up!");
            System.out.println("---------------------------------");
            return;
        }

        if (owner != null) {
            showPersonalBalances();
        } else {
            showGroupBalances();
        }
        System.out.println("---------------------------------");
    }

    private void showPersonalBalances() {
        double totalOwedToMe = 0;
        double totalIOwe = 0;

        for (Map.Entry<User, Double> entry : balances.entrySet()) {
            User otherUser = entry.getKey();
            double amount = entry.getValue();

            if (amount > 0.01) {
                System.out.println(otherUser.getName() + " owes " + owner.getName() + " Rs "
                        + String.format("%.2f", amount));
                totalOwedToMe += amount;
            } else if (amount < -0.01) {
                System.out.println(owner.getName() + " owes " + otherUser.getName() + " Rs "
                        + String.format("%.2f", -amount));
                totalIOwe += -amount;
            }
        }

        System.out.println("Total owed to " + owner.getName() + ": Rs "
                + String.format("%.2f", totalOwedToMe));
        System.out.println("Total " + owner.getName() + " owes: Rs "
                + String.format("%.2f", totalIOwe));
    }

    private void showGroupBalances() {
        for (Map.Entry<User, Double> entry : balances.entrySet()) {
            double amount = entry.getValue();
            if (amount > 0.01) {
                System.out.println(entry.getKey().getName() + " gets back Rs "
                        + String.format("%.2f", amount));
            } else if (amount < -0.01) {
                System.out.println(entry.getKey().getName() + " owes Rs "
                        + String.format("%.2f", -amount));
            }
        }
    }
}
