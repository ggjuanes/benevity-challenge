package com.ggjuanes.benevity_challenge.server.user.domain;

import java.util.Objects;

public class User {
    private final String username;
    private final String password;

    private User(String username, String password) {

        this.username = username;
        this.password = password;
    }
    public static User create(String username, String password) {
        return new User(username, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
