package com.dn.projectdashboard.Person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    public List<Person> findAllByIdIn(List<Integer> ids);

    Optional<Person> findByUsername(String username);
}
