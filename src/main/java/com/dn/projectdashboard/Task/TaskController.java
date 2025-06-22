package com.dn.projectdashboard.Task;

import com.dn.projectdashboard.Mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
class TaskController {

    private final TaskRepository repository;
    private final TaskMapper taskMapper;



    // Aggregate root
    // tag::get-aggregate-root[]
    @QueryMapping
    List<Task> allTasks() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @MutationMapping
    Task newTask(@Argument String title, @Argument String description, @Argument Double donePercentage) {
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setDescription(description);
        if (donePercentage == null) {
            newTask.setDone(0);
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

    @GetMapping("/tasks/{id}")
    Task one(@PathVariable Integer id) {

        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
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


    @MutationMapping
    Task addToPercentage(@Argument Integer id, @Argument Integer taskId) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setTitle(newTask.getTitle());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    return repository.save(newTask);
                });
    }

    @DeleteMapping("/tasks/{id}")
    void deleteTask(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}