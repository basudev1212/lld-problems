package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Group {
    private final String id;
    private final String name;
    private final List<User> members;
    private final BalanceSheet balanceSheet;

    public Group(String name, List<User> members) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.members = new ArrayList<>(members);
        this.balanceSheet = BalanceSheet.forGroup();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public BalanceSheet getBalanceSheet() {
        return balanceSheet;
    }

    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public boolean isMember(User user) {
        return members.contains(user);
    }
}
