package com.dn.projectdashboard.Task;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Integer id) {
        super("Could not find employee " + id);
    }
}