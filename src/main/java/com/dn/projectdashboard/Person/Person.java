package com.dn.projectdashboard.Person;

import com.dn.projectdashboard.Project.Project;
import com.dn.projectdashboard.Task.Task;
import com.dn.projectdashboard.Team.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {
    public @Id @GeneratedValue int id;
    public String position;
    public String username;
    public String email;
    public String password;
    public String name;

    @ManyToOne
    @JoinColumn(name = "manager_id") // This column refers to another Person
    public Person manager;

    @OneToMany(mappedBy = "manager")
    public List<Person> subordinates = new ArrayList<>(0);

    @ManyToOne()
    public Task task;

    @ManyToOne
    public Team team;

    @ManyToMany
    public List<Project> projects = new ArrayList<>(0);
}
