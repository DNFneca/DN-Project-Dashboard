package com.dn.projectdashboard.Task;

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
class TaskController {

    private final TaskRepository repository;



    // Aggregate root
    // tag::get-aggregate-root[]
    @QueryMapping
    List<Task> allTasks() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @MutationMapping
    Task newTask(@Argument String title, @Argument String description, @Argument Boolean donePercentage) {
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setDescription(description);
        if (donePercentage == null) {
            newTask.setDone(false);
        } else {
            newTask.setDone(donePercentage);
        }
        return repository.save(newTask);
    }

    @QueryMapping
    Integer totalTasks() {
        return repository.countAllByIdNotNull();
    }

    // Single item

    @QueryMapping
    Optional<Task> task(@Argument Integer id) {

        return repository.findById(id);
    }

    @MutationMapping("/tasks/{id}")
    Task replaceTask(@RequestBody Task newTask, @PathVariable Integer id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setTitle(newTask.getTitle());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    return repository.save(newTask);
                });
    }
}