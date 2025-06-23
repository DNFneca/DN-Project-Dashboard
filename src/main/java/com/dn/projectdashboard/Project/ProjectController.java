package com.dn.projectdashboard.Project;

import com.dn.projectdashboard.Person.Person;
import com.dn.projectdashboard.Person.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ProjectController {

    private ProjectRepository repository;
    private PersonRepository personRepository;

    @QueryMapping
    public List<Project> allProjects() {
        return repository.findAll();
    }

    @MutationMapping
    public Project newProject(@Argument String name, @Argument String description) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        return repository.save(project);
    }

    @MutationMapping
    public Project addPersonToProject(@Argument Integer personId, @Argument Integer projectId) {
        Optional<Project> project = repository.findById(projectId);
        Optional<Person> person = personRepository.findById(personId);

        if (project.isEmpty() || person.isEmpty()) return null;

        project.get().getEmployees().add(person.get());
        return repository.save(project.get());
    }
}
