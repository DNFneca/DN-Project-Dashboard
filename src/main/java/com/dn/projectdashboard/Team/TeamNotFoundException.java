package com.dn.projectdashboard.Team;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(Integer id) {
        super("Could not find employee " + id);
    }
}