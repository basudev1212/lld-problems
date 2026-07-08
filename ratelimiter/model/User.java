package model;

import enums.UserType;

public class User {
    public String userId;
    public UserType userType;

    public User(String userId, UserType userType) {
        this.userId = userId;
        this.userType = userType;
    }
}
