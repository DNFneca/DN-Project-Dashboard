package com.dn.projectdashboard.Sprint;

import com.dn.projectdashboard.Project.Project;
import com.dn.projectdashboard.Task.Task;
import com.dn.projectdashboard.Team.Team;
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
public class Sprint {
    @Id @GeneratedValue
    private int id;
    private String name;
    private String description;
    private String goal;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Float pointsDone;
    private Float storyPoints;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>(0);

    @OneToMany(mappedBy = "sprint")
    private List<Team> teams = new ArrayList<>(0);

    @ManyToOne
    private Project project;
}
