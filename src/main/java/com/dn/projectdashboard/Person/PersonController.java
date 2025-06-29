package com.dn.projectdashboard.Person;

import com.dn.projectdashboard.DTO.AuthResponse;
import com.dn.projectdashboard.Service.AuthService;
import com.dn.projectdashboard.Task.TaskNotFoundException;
import com.dn.projectdashboard.Task.TaskRepository;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class PersonController {

    private final PersonRepository repository;
    private final TaskRepository taskRepository;
    private final AuthService authService;

    // Aggregate root
    // tag::get-aggregate-root[]
    @QueryMapping
    public List<Person> allPeople() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @MutationMapping
    public AuthResponse register(@Argument String username, @Argument String password, @Argument String email) {
        try {
            Person user = authService.register(username, password, email);
            String token = authService.login(username, password);
            return new AuthResponse(token, user.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    @QueryMapping
    public Optional<Person> person(@Argument Integer id) {
        return repository.findById(id);
    }

    // Single item

    @MutationMapping
    public Person newPerson(@Argument String name, @Argument String position, @Argument Integer managerId, @Argument Integer taskId) {
        Person person = new Person();
        person.setName(name);
        person.setPosition(position);
        if (managerId != null)
            person.setManager(repository.findById(managerId).orElseThrow(() -> new PersonNotFoundException(managerId)));
        if (taskId != null)
            person.setTask(taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId)));
        return repository.save(person);
    }

    @MutationMapping
    public AuthResponse login(@Argument String username, @Argument String password) {
        try {
            String token = authService.login(username, password);
            return new AuthResponse(token, username);
        } catch (Exception e) {
            return new AuthResponse("Login failed: " + e.getMessage());
        }
    }

    @QueryMapping
    public Person me(DataFetchingEnvironment env) {
        Integer userId = env.getGraphQlContext().get("userId");
        return repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PutMapping("/employees/{id}")
    Person replacePerson(@RequestBody Person newPerson, @PathVariable Integer id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setPosition(newPerson.getPosition());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    return repository.save(newPerson);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deletePerson(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}