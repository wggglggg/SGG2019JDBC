package com.jdbc02.statement.crud;

/**
 * ClassName: User
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/6/28 19:32
 * @Version 1.0
 */
public class User {

    private String user;
    private String password;

    public User() {
    }

    public User(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
