package com.dn.projectdashboard.Task;

import com.dn.projectdashboard.Person.Person;
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
@Table(name = "task")
public class Task {
    public @Id @GeneratedValue int id;
    public String title;
    public String description;
    public boolean isDone;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    public List<Person> assignees = new ArrayList<>(0);
}
