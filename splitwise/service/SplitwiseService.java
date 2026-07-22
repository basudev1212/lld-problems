package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Expense;
import model.Group;
import model.Split;
import model.Transaction;
import model.User;

public class SplitwiseService {
    private static SplitwiseService instance;

    private final Map<String, User> users;
    private final Map<String, Group> groups;
    private final Map<String, Expense> expenses;

    public SplitwiseService() {
        this.users = new HashMap<>();
        this.groups = new HashMap<>();
        this.expenses = new HashMap<>();
    }

    public static synchronized SplitwiseService getInstance() {
        if (instance == null) {
            instance = new SplitwiseService();
        }
        return instance;
    }

    public User addUser(String name) {
        User user = new User(name);
        users.put(user.getId(), user);
        return user;
    }

    public Group addGroup(String name, List<User> members) {
        Group group = new Group(name, members);
        groups.put(group.getId(), group);
        return group;
    }

    public User getUser(String id) {
        User user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        return user;
    }

    public Group getGroup(String id) {
        Group group = groups.get(id);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + id);
        }
        return group;
    }

    public Expense getExpense(String expenseId) {
        Expense expense = expenses.get(expenseId);
        if (expense == null) {
            throw new IllegalArgumentException("Expense not found: " + expenseId);
        }
        return expense;
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses.values());
    }

    public List<Expense> getGroupExpenses(String groupId) {
        List<Expense> groupExpenses = new ArrayList<>();
        for (Expense expense : expenses.values()) {
            if (expense.getGroup() != null && expense.getGroup().getId().equals(groupId)) {
                groupExpenses.add(expense);
            }
        }
        return groupExpenses;
    }

    public List<Expense> getPersonalExpenses() {
        List<Expense> personalExpenses = new ArrayList<>();
        for (Expense expense : expenses.values()) {
            if (!expense.isGroupExpense()) {
                personalExpenses.add(expense);
            }
        }
        return personalExpenses;
    }

    public synchronized void createExpense(Expense expense) {
        expenses.put(expense.getId(), expense);

        if (expense.isGroupExpense()) {
            applyGroupExpense(expense);
        } else {
            applyPersonalExpense(expense);
        }
    }

    public synchronized void settleUp(String payerId, String payeeId, double amount) {
        User payer = getUser(payerId);
        User payee = getUser(payeeId);

        payee.getPersonalBalanceSheet().adjustBalance(payer, -amount);
        payer.getPersonalBalanceSheet().adjustBalance(payee, amount);
    }

    public synchronized void settleGroupUp(String groupId, String payerId, String payeeId, double amount) {
        Group group = getGroup(groupId);
        User payer = getUser(payerId);
        User payee = getUser(payeeId);
        validateGroupMembers(group, payer, payee);

        group.getBalanceSheet().applyGroupSplit(payer, payee, amount);
    }

    public void showAllExpenses() {
        System.out.println("--- All Expenses ---");
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        for (Expense expense : expenses.values()) {
            System.out.println("  " + expense);
        }
    }

    public void showGroupExpenses(String groupId) {
        Group group = getGroup(groupId);
        System.out.println("--- Expenses for " + group.getName() + " ---");
        List<Expense> groupExpenses = getGroupExpenses(groupId);
        if (groupExpenses.isEmpty()) {
            System.out.println("No group expenses yet.");
            return;
        }
        for (Expense expense : groupExpenses) {
            System.out.println("  " + expense);
        }
    }

    public void showPersonalExpenses() {
        System.out.println("--- Personal Expenses ---");
        List<Expense> personalExpenses = getPersonalExpenses();
        if (personalExpenses.isEmpty()) {
            System.out.println("No personal expenses yet.");
            return;
        }
        for (Expense expense : personalExpenses) {
            System.out.println("  " + expense);
        }
    }

    public void showPersonalBalanceSheet(String userId) {
        User user = getUser(userId);
        user.getPersonalBalanceSheet().showBalances("Personal Balance Sheet for " + user.getName());
    }

    public void showGroupBalanceSheet(String groupId) {
        Group group = getGroup(groupId);
        group.getBalanceSheet().showBalances("Group Balance Sheet for " + group.getName());
    }

    public List<Transaction> simplifyGroupDebts(String groupId) {
        Group group = getGroup(groupId);
        Map<User, Double> netBalances = copyGroupBalances(group);

        List<UserBalance> creditors = new ArrayList<>();
        List<UserBalance> debtors = new ArrayList<>();

        for (Map.Entry<User, Double> entry : netBalances.entrySet()) {
            double balance = entry.getValue();
            if (balance > 0.01) {
                creditors.add(new UserBalance(entry.getKey(), balance));
            } else if (balance < -0.01) {
                debtors.add(new UserBalance(entry.getKey(), -balance));
            }
        }

        sortByAmountDescending(creditors);
        sortByAmountDescending(debtors);

        List<Transaction> transactions = new ArrayList<>();
        int creditorIndex = 0;
        int debtorIndex = 0;

        while (creditorIndex < creditors.size() && debtorIndex < debtors.size()) {
            UserBalance creditor = creditors.get(creditorIndex);
            UserBalance debtor = debtors.get(debtorIndex);

            double settleAmount = Math.min(creditor.amount, debtor.amount);
            transactions.add(new Transaction(debtor.user, creditor.user, settleAmount));

            creditor.amount = creditor.amount - settleAmount;
            debtor.amount = debtor.amount - settleAmount;

            if (creditor.amount < 0.01) {
                creditorIndex++;
            }
            if (debtor.amount < 0.01) {
                debtorIndex++;
            }
        }

        return transactions;
    }

    private void sortByAmountDescending(List<UserBalance> balances) {
        Collections.sort(balances, (UserBalance first, UserBalance second) -> Double.compare(second.amount, first.amount));
    }

    private Map<User, Double> copyGroupBalances(Group group) {
        return new HashMap<>(group.getBalanceSheet().getBalances());
    }

    private static class UserBalance {
        private final User user;
        private double amount;

        private UserBalance(User user, double amount) {
            this.user = user;
            this.amount = amount;
        }
    }

    private void applyGroupExpense(Expense expense) {
        Group group = expense.getGroup();
        validateGroupParticipants(group, expense);

        User paidBy = expense.getPaidBy();
        for (Split split : expense.getSplits()) {
            group.getBalanceSheet().applyGroupSplit(paidBy, split.getUser(), split.getAmount());
        }
    }

    private void applyPersonalExpense(Expense expense) {
        User paidBy = expense.getPaidBy();
        for (Split split : expense.getSplits()) {
            User participant = split.getUser();
            double amount = split.getAmount();

            if (!paidBy.equals(participant)) {
                paidBy.getPersonalBalanceSheet().adjustBalance(participant, amount);
                participant.getPersonalBalanceSheet().adjustBalance(paidBy, -amount);
            }
        }
    }

    private void validateGroupParticipants(Group group, Expense expense) {
        if (!group.isMember(expense.getPaidBy())) {
            throw new IllegalArgumentException("Payer must be a group member.");
        }
        for (Split split : expense.getSplits()) {
            if (!group.isMember(split.getUser())) {
                throw new IllegalArgumentException("All participants must be group members.");
            }
        }
    }

    private void validateGroupMembers(Group group, User... members) {
        for (User member : members) {
            if (!group.isMember(member)) {
                throw new IllegalArgumentException("User must be a group member: " + member.getName());
            }
        }
    }
}
