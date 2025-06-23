package com.dn.projectdashboard.Sprint;

import com.dn.projectdashboard.Project.ProjectNotFoundException;
import com.dn.projectdashboard.Project.ProjectRepository;
import com.dn.projectdashboard.Task.TaskNotFoundException;
import com.dn.projectdashboard.Task.TaskRepository;
import com.dn.projectdashboard.Team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class SprintController {

    private SprintRepository repository;
    private ProjectRepository projectRepository;
    private TeamRepository teamRepository;
    private TaskRepository taskRepository;

    @QueryMapping
    public List<Sprint> allSprints() {
        return repository.findAll();
    }

    @QueryMapping
    public Optional<Sprint> sprint(@Argument Integer id) {
        return repository.findById(id);
    }

    @MutationMapping
    public Sprint newSprint(@Argument String name, @Argument String description, @Argument String goal, @Argument Integer projectId, @Argument Long startDate, @Argument Long endDate, @Argument List<Integer> teamIds, @Argument List<Integer> taskIds) {
        Sprint sprint = new Sprint();
        sprint.setName(name);
        sprint.setDescription(description);
        sprint.setGoal(goal);
        sprint.setProject(projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project not found")));
        if (teamIds != null) {
            for (Integer teamId : teamIds) {
                sprint.getTeams().add(teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found")));
            }
        }
        if (taskIds != null) {
            for (Integer taskId : taskIds) {
                sprint.getTasks().add(taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId)));
            }
        }
        if (startDate != null) {
            sprint.setStartDate(LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(startDate), ZoneId.systemDefault()));
        } else {
            sprint.setStartDate(LocalDateTime.now());
        }
        if (endDate != null) {
            sprint.setEndDate(LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(endDate), ZoneId.systemDefault()));
        } else {
            sprint.setEndDate(LocalDateTime.now());
        }
        return repository.save(sprint);
    }

}
