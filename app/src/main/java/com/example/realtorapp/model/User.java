package com.example.realtorapp.model;

public class User {
    String userId, name, email, type;
    boolean verified;

    public User(String userId, String name, String email, String type, boolean verified) {
        setUserId(userId);
        setName(name);
        setEmail(email);
        setType(type);
        setVerified(verified);
    }

    public String getUserID() {
        return userId;
    }
    public String getUserName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }
    public boolean verified() {
        return verified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean setEmail(String email) {
        if (isEmailValid(email)) {
            this.email = email;
            return true;
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public void setType(String type) {
        if (type.equals("buyer") || type.equals("student") || type.equals("agent") || type.equals("landlord")) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean changeEmail(String newEmail) {
        return setEmail(email);
    }

}
