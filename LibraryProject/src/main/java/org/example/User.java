package org.example;

public class User {

    private static int nextId = 1;

    private final int userId;
    private String password;
    private Role role;

    public User(String password, Role role) {
        this.userId = nextId++;
        this.password = password;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }

    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }

    @Override
    public String toString() {
        return "ID: " + userId + " | Role: " + role;
        // ⚠️ NÃO imprimir senha em toString — boa prática
    }
}