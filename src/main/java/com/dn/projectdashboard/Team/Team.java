package com.dn.projectdashboard.Team;

import com.dn.projectdashboard.Person.Person;
import com.dn.projectdashboard.Sprint.Sprint;
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
public class Team {
    @Id @GeneratedValue
    private int id;
    private String name;
    private String description;

    @ManyToOne
    private Person teamLead;

    @ManyToOne
    private Sprint sprint;

    @ManyToMany
    private List<Person> members = new ArrayList<>(0);

}
