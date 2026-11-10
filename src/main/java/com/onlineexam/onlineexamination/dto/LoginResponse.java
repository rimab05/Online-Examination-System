package com.onlineexam.onlineexamination.dto;

public class LoginResponse {

    private Long id;
    private String username;
    private String displayName;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(Long id, String username,
                         String displayName, String message) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
