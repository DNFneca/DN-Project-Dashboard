package com.dn.projectdashboard.GraphQL.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
public class AuthContext {
    private String userId;
    private String username;
    private List<String> roles;
    private boolean authenticated;

    public AuthContext(String userId, String username, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles != null ? roles : new ArrayList<>();
        this.authenticated = true;
    }

    public AuthContext() {
        this.authenticated = false;
        this.roles = new ArrayList<>();
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public boolean hasAnyRole(String... roles) {
        return Arrays.stream(roles).anyMatch(this.roles::contains);
    }
}