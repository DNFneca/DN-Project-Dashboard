package com.dn.projectdashboard.Person;

public class PersonNotFoundException extends RuntimeException {

    PersonNotFoundException(Integer id) {
        super("Could not find employee " + id);
    }
}