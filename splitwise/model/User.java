package model;

import java.util.UUID;

public class User {
    private final String id;
    private final String name;
    private final BalanceSheet personalBalanceSheet;

    public User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.personalBalanceSheet = BalanceSheet.forUser(this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BalanceSheet getPersonalBalanceSheet() {
        return personalBalanceSheet;
    }
}
