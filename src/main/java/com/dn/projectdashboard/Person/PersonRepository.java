package com.dn.projectdashboard.Person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    public Person findAllByIdIn(List<Integer> ids);
}
