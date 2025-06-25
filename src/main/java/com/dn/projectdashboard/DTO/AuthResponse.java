package com.dn.projectdashboard.DTO;

public class AuthResponse {
    private String token;
    private String username;
    private String message;

    // Constructors, getters, setters
    public AuthResponse() {}

    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public AuthResponse(String message) {
        this.message = message;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
