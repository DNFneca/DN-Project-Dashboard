package com.dn.projectdashboard.Person;

import com.dn.projectdashboard.Task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "person")
public class Person {
    public @Id @GeneratedValue int id;
    public String position;
    public String name;

    @ManyToOne
    @JoinColumn(name = "manager_id") // This column refers to another Person
    @JsonBackReference
    public Person manager;

    @OneToMany(mappedBy = "manager")
    public List<Person> subordinates = new ArrayList<>(0);

    @ManyToOne()
    public Task task;
}
