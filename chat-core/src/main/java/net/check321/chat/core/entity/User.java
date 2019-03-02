package net.check321.chat.core.entity;

import lombok.Data;

@Data
public class User {

    private int userId;

    private String userName;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public User(String userName) {
        this.userName = userName;
    }
}
