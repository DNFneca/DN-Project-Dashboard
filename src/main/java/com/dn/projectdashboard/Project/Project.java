package com.dn.projectdashboard.Project;

import com.dn.projectdashboard.Person.Person;
import com.dn.projectdashboard.Sprint.Sprint;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Project {

    public @Id
    @GeneratedValue int id;
    public String name;
    public String description;
    public Integer ownerId;

    @ManyToMany
    public List<Person> employees = new ArrayList<>(0);

    @OneToMany(mappedBy = "project")
    public List<Sprint> sprints = new ArrayList<>(0);
}
