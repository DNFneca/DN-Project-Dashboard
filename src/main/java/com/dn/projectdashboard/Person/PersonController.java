package com.dn.projectdashboard.Person;

import com.dn.projectdashboard.DTO.PersonDTO;
import com.dn.projectdashboard.Mapper.PersonMapper;
import com.dn.projectdashboard.Task.TaskNotFoundException;
import com.dn.projectdashboard.Task.TaskRepository;
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
    private final PersonMapper personMapper;
    private final TaskRepository taskRepository;


    // Aggregate root
    // tag::get-aggregate-root[]
    @QueryMapping
    public List<Person> allPeople() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @QueryMapping
    public Optional<Person> person(@Argument Integer id) {
        return repository.findById(id);
    }

    // Single item

    @MutationMapping
    public Person addPerson(@Argument String name, @Argument String position, @Argument Integer managerId, @Argument Integer taskId) {
        Person person = new Person();
        person.setName(name);
        person.setPosition(position);
        if (managerId != null)
            person.setManager(repository.findById(managerId).orElseThrow(() -> new PersonNotFoundException(managerId)));
        if (taskId != null)
            person.setTask(taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId)));
        return repository.save(person);
    }

    @GetMapping("/employees/{id}")
    PersonDTO one(@PathVariable Integer id) {

        return repository.findById(id).map(personMapper::toDto)
                .orElseThrow(() -> new PersonNotFoundException(id));
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