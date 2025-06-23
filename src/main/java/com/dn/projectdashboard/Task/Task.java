package com.dn.projectdashboard.Task;

import com.dn.projectdashboard.Person.Person;
import com.dn.projectdashboard.Sprint.Sprint;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {
    public @Id @GeneratedValue int id;
    public String title;
    public String description;
    public boolean isDone;
    public LocalDateTime deadline;

    @ManyToOne
    public Sprint sprint;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    public List<Person> assignees = new ArrayList<>(0);
}
