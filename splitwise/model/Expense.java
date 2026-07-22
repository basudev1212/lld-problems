package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import strategy.SplitStrategy;

public class Expense {
    private final String id;
    private final String description;
    private final double amount;
    private final User paidBy;
    private final List<Split> splits;
    private final Group group;
    private final LocalDateTime timestamp;

    private Expense(ExpenseBuilder builder) {
        this.id = UUID.randomUUID().toString();
        this.description = builder.description;
        this.amount = builder.amount;
        this.paidBy = builder.paidBy;
        this.group = builder.group;
        this.timestamp = LocalDateTime.now();
        this.splits = builder.splitStrategy.calculateSplits(
                builder.amount, builder.paidBy, builder.participants, builder.splitValues);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public Group getGroup() {
        return group;
    }

    public boolean isGroupExpense() {
        return group != null;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        String groupName = group != null ? group.getName() : "Personal";
        return description + " | Rs " + String.format("%.2f", amount)
                + " | paid by " + paidBy.getName()
                + " | " + groupName;
    }

    public static class ExpenseBuilder {
        private String description;
        private double amount;
        private User paidBy;
        private List<User> participants;
        private Group group;
        private SplitStrategy splitStrategy;
        private List<Double> splitValues;

        public ExpenseBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ExpenseBuilder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public ExpenseBuilder setPaidBy(User paidBy) {
            this.paidBy = paidBy;
            return this;
        }

        public ExpenseBuilder setParticipants(List<User> participants) {
            this.participants = participants;
            return this;
        }

        public ExpenseBuilder setGroup(Group group) {
            this.group = group;
            return this;
        }

        public ExpenseBuilder setSplitStrategy(SplitStrategy splitStrategy) {
            this.splitStrategy = splitStrategy;
            return this;
        }

        public ExpenseBuilder setSplitValues(List<Double> splitValues) {
            this.splitValues = splitValues;
            return this;
        }

        public Expense build() {
            if (splitStrategy == null) {
                throw new IllegalStateException("Split strategy is required.");
            }
            if (paidBy == null || participants == null || participants.isEmpty()) {
                throw new IllegalStateException("Payer and participants are required.");
            }
            return new Expense(this);
        }
    }
}
