package stockbroker.model;

public class User {
    private final String id;
    private final Account account;
    private final String name;

    public User(String id, String name, Double initialBalance) {
        this.id = id;
        this.name = name;
        this.account = new Account(initialBalance);
    }

    public String getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }
}
