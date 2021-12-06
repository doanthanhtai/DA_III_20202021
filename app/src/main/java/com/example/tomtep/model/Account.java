package com.example.tomtep.model;

public class Account {
    private String id;
    private String email;
    private boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Account() {
    }

    public Account(String id, String email, boolean deleted) {
        this.id = id;
        this.email = email;
        this.deleted = deleted;
    }
}
