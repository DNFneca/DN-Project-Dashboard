package com.dn.projectdashboard.Task;

public class TaskNotFoundException extends RuntimeException {

    TaskNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}